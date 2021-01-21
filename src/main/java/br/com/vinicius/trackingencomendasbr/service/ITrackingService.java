package br.com.vinicius.trackingencomendasbr.service;

public interface ITrackingService {

	public void startInstagramInboxReceiver();
	
	public void sendInstagramQueuedMessages();
	
	public void sendTrackingPendingUpdates();
}
