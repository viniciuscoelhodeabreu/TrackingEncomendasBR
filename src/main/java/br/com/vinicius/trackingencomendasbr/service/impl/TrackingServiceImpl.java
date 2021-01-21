package br.com.vinicius.trackingencomendasbr.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.instagram4j.instagram4j.models.direct.IGThread;
import com.github.instagram4j.instagram4j.models.direct.item.ThreadItem;
import com.github.instagram4j.instagram4j.models.direct.item.ThreadTextItem;
import com.github.instagram4j.instagram4j.models.user.Profile;

import br.com.vinicius.trackingencomendasbr.dto.EventDTO;
import br.com.vinicius.trackingencomendasbr.dto.TrackingDTO;
import br.com.vinicius.trackingencomendasbr.entity.UserEntity;
import br.com.vinicius.trackingencomendasbr.entity.UserMessageEntity;
import br.com.vinicius.trackingencomendasbr.entity.UserPackageEntity;
import br.com.vinicius.trackingencomendasbr.entity.UserPackageEventEntity;
import br.com.vinicius.trackingencomendasbr.exception.NotFoundException;
import br.com.vinicius.trackingencomendasbr.manager.InstagramDirectSenderQueueManager;
import br.com.vinicius.trackingencomendasbr.model.InstagramMessage;
import br.com.vinicius.trackingencomendasbr.repository.IShippingCompanyRepository;
import br.com.vinicius.trackingencomendasbr.repository.ISocialMediaRepository;
import br.com.vinicius.trackingencomendasbr.repository.IUserMessageRepository;
import br.com.vinicius.trackingencomendasbr.repository.IUserPackageEventRepository;
import br.com.vinicius.trackingencomendasbr.repository.IUserPackageRepository;
import br.com.vinicius.trackingencomendasbr.service.IInstagramService;
import br.com.vinicius.trackingencomendasbr.service.ILinkAndTrackService;
import br.com.vinicius.trackingencomendasbr.service.ITrackingService;
import br.com.vinicius.trackingencomendasbr.service.IUserService;
import br.com.vinicius.trackingencomendasbr.util.Constants;
import br.com.vinicius.trackingencomendasbr.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements ITrackingService {

	private final IInstagramService instagramService;
	private final IUserService userService;
	private final ILinkAndTrackService linkAndTrackService;
	
	private final ISocialMediaRepository socialMediaRepository;
	private final IUserMessageRepository userMessageRepository;
	private final IShippingCompanyRepository shippingCompanyRepository;
	private final IUserPackageRepository userPackageRepository;
	private final IUserPackageEventRepository userPackageEventRepository;
	
	@Override
	public void sendTrackingPendingUpdates() {
		for(UserPackageEntity userPackage : userPackageRepository.findAll()) {
			TrackingDTO tracking = linkAndTrackService.getTrackingByTrackCode(userPackage.getTrackingCode());
			Optional<UserPackageEventEntity> lastPackageUpdate = userPackageEventRepository.findLastByUserPackageId(userPackage.getId());
			
			if(lastPackageUpdate.isPresent() && lastPackageUpdate.get().getLast().equals(tracking.getLast()))
				continue;
			
			Collections.reverse(tracking.getEvents());
			
			UserPackageEventEntity lastPackageEvent = lastPackageUpdate.get();
			List<EventDTO> events = tracking.getEvents();
			
			for(EventDTO event : events) {
				if(lastPackageEvent.getTime().after(event.toDate()))
					continue;
				
				userPackageEventRepository.save(new UserPackageEventEntity(event.toDate(), tracking.getLast(), userPackage));
				InstagramDirectSenderQueueManager.addMessage(Constants.getTrackingMessage(event.getDate(), event.getHour(), event.getPlace(), event.getStatus(), String.join("\n", event.getSubStatus())), userPackage.getUser().getIdentification());	
			}
		}
	}
	
	@Override
	public void sendInstagramQueuedMessages() {
		while(InstagramDirectSenderQueueManager.hasElements()) {
			InstagramMessage instagramMessage = InstagramDirectSenderQueueManager.pool();
			
			instagramService.sendThreadMessage(instagramMessage.getThreadId(), instagramMessage.getMessage());
			Util.sleepSeconds(3);
		}
	}
	
	
	@Override
	@SneakyThrows
	public void startInstagramInboxReceiver() {
		instagramService.acceptAllPendindsThreads();
		
		for(IGThread thread : instagramService.getAllThreads()) {
			
			if(thread.getUsers().size() != 1)
				continue;
			
			for(ThreadItem threadItem : instagramService.getThreadItems(thread.getThread_id())) {
				
				if(!(threadItem instanceof ThreadTextItem) || threadItem.getUser_id() != thread.getUsers().get(0).getPk().longValue())
					continue;
				
				Profile profile = thread.getUsers().get(0);
				ThreadTextItem threadTextItem = (ThreadTextItem) threadItem;
				UserEntity user;
				
				try {
					user = userService.findByIdentification(profile.getPk().toString());
					
					if(userMessageRepository.findByIdentification(threadTextItem.getItem_id()).isPresent())
						continue;
					
					String threadMessage = threadTextItem.getText().toUpperCase().replace(" ", "").trim();
					
					// Validate Correios's Tracking Code
					if(validateCorreiosTrackingCode(threadMessage)) {
						TrackingDTO tracking = linkAndTrackService.getTrackingByTrackCode(threadMessage);
						
						// Has Events
						if(!tracking.getEvents().isEmpty() && !userPackageRepository.findByTrackingCodeAndUserId(tracking.getCode(), user.getId()).isPresent()) {
							EventDTO lastEvent = tracking.getEvents().get(0);
							String message = Constants.getTrackingMessage(lastEvent.getDate(), lastEvent.getHour(), lastEvent.getPlace(), lastEvent.getStatus(), String.join("\n", lastEvent.getSubStatus()));
							
							UserPackageEntity userPackage = userPackageRepository.save(new UserPackageEntity(tracking.getCode(), user, shippingCompanyRepository.findById(1).orElseThrow(() -> new NotFoundException("Shipping Company Correios não encontrada."))));
							userPackageEventRepository.save(new UserPackageEventEntity(lastEvent.toDate(), tracking.getLast(), userPackage));
							
							InstagramDirectSenderQueueManager.addMessage(Constants.getNewTrackingPackageMessage(tracking.getCode(), message), thread.getThread_id());
						}else {
							InstagramDirectSenderQueueManager.addMessage(Constants.INVALID_PACKAGE_TRACKING_CODE, thread.getThread_id());
						}
						
					}else {
						// Invalid tracking code
						InstagramDirectSenderQueueManager.addMessage(Constants.INVALID_PACKAGE_TRACKING_CODE, thread.getThread_id());
					}
					
				}catch(Exception e) {
					user = userService.save(new UserEntity(profile.getPk().toString(), thread.getThread_id(), socialMediaRepository.findById(1).orElseThrow(() -> new NotFoundException("Social Media Instagram não encontrada."))));
					
					//Send the welcome message for new users
					InstagramDirectSenderQueueManager.addMessage(Constants.getWelcomeMessageInstagram(profile.getFull_name() == null ? profile.getUsername() : profile.getFull_name()), thread.getThread_id());
				}
				
				userMessageRepository.save(new UserMessageEntity(threadTextItem.getItem_id(), user));
				
			}
		}
	}
	
	
	private Boolean validateCorreiosTrackingCode(String message) {
		
		if(message.isEmpty() || message.isBlank() || message.trim().contains(" ") || message.length() != 13)
			return false;
		
		return message.matches("^[A-Z]{2}[0-9]{9}[A-Z]{2}$");
	}

}
