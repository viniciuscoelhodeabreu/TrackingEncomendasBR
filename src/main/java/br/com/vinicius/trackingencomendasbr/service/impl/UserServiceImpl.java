package br.com.vinicius.trackingencomendasbr.service.impl;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.vinicius.trackingencomendasbr.entity.UserEntity;
import br.com.vinicius.trackingencomendasbr.repository.IUserRepository;
import br.com.vinicius.trackingencomendasbr.service.IUserService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
	
	private final IUserRepository userRepository;
	
	
	@Override
	@SneakyThrows
	public UserEntity findByIdentification(String identification) {
		return userRepository.findByIdentification(identification)
							 .orElseThrow(() -> new NotFoundException("User not found."));
	}
	
	@Override
	@Transactional
	public UserEntity save(UserEntity user) {
		return userRepository.save(user);
	}
}
