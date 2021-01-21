package br.com.vinicius.trackingencomendasbr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.direct.IGThread;
import com.github.instagram4j.instagram4j.models.direct.item.ThreadItem;
import com.github.instagram4j.instagram4j.requests.direct.DirectInboxRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectPendingInboxRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsApproveRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsBroadcastRequest.BroadcastTextPayload;
import com.github.instagram4j.instagram4j.requests.direct.DirectThreadsRequest;
import com.github.instagram4j.instagram4j.responses.IGResponse;
import com.github.instagram4j.instagram4j.responses.direct.DirectInboxResponse;

import br.com.vinicius.trackingencomendasbr.service.IInstagramService;
import br.com.vinicius.trackingencomendasbr.util.Util;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstagramServiceImpl implements IInstagramService {

	private final IGClient igClient;
	
	@Override
	public List<IGThread> getAllThreads() {
		 DirectInboxResponse response = new DirectInboxRequest().execute(igClient).join();
		 return response.getInbox().getThreads();
	}
	
	@Override
	public void acceptAllPendindsThreads() {
		DirectInboxResponse response = new DirectPendingInboxRequest().execute(igClient).join();
		List<IGThread> threads = response.getInbox().getThreads();
		
		threads.forEach(thread -> {
			DirectThreadsApproveRequest directThreadsApproveRequest = new DirectThreadsApproveRequest(thread.getThread_id());
			directThreadsApproveRequest.execute(igClient).join();
		});
	}

	@Override
	public Boolean sendThreadMessage(String threadId, String message) {
		 IGResponse response = new DirectThreadsBroadcastRequest(new BroadcastTextPayload(message, threadId)).execute(igClient).join();
		 return response.getStatusCode() == 200;
	}

	@Override
	public List<ThreadItem> getThreadItems(String threadId) {
		 IGResponse response = igClient.sendRequest(new DirectThreadsRequest(threadId)).join();
		 IGThread thread = (IGThread) Util.getFieldValue("thread", response);
		 return thread.getItems();
	}

}
