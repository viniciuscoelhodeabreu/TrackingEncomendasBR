package br.com.vinicius.trackingencomendasbr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.github.instagram4j.instagram4j.IGClient;

import br.com.vinicius.trackingencomendasbr.service.ITrackingService;
import br.com.vinicius.trackingencomendasbr.util.InstagramUtil;
import br.com.vinicius.trackingencomendasbr.util.Util;

@SpringBootApplication
public class TrackingEncomendasBrApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(TrackingEncomendasBrApplication.class, args);
		startIntegration(applicationContext);
	}
	
	@Bean
	@Scope("singleton")
	public IGClient getIgClient() {
		return InstagramUtil.establishClient();
	}
	
	private static void startIntegration(ApplicationContext applicationContext) {
		ITrackingService trackingService = applicationContext.getBean(ITrackingService.class); 
		
		new Thread(() -> {
			while(true) {
				trackingService.startInstagramInboxReceiver();
				Util.sleepMinutes(5);
			}
		}, "Instagram Inbox Receiver Thread").start();
		
		new Thread(() -> {
			while(true) {
				trackingService.sendInstagramQueuedMessages();
				Util.sleepMinutes(1);
			}
		}, "Instagram Inbox Receiver Thread").start();
		
	}
}
