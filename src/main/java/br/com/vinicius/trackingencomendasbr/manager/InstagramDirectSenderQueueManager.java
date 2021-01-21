package br.com.vinicius.trackingencomendasbr.manager;

import java.util.LinkedList;
import java.util.Queue;

import br.com.vinicius.trackingencomendasbr.model.InstagramMessage;

public class InstagramDirectSenderQueueManager {

	private static final Queue<InstagramMessage> queue;
	
	static {
		queue = new LinkedList<InstagramMessage>();
	}
	
	public static void addMessage(String message, String threadId) {
		queue.add(new InstagramMessage(message, threadId));
	}
	
	public static InstagramMessage pool() {
		return queue.poll();
	}
	
	public static boolean hasElements() {
		return !queue.isEmpty();
	}
	
}
