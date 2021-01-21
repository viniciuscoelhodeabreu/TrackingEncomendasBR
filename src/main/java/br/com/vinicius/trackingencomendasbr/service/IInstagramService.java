package br.com.vinicius.trackingencomendasbr.service;

import java.util.List;

import com.github.instagram4j.instagram4j.models.direct.IGThread;
import com.github.instagram4j.instagram4j.models.direct.item.ThreadItem;

public interface IInstagramService {

	public List<IGThread> getAllThreads();
	
	public void acceptAllPendindsThreads();
	
	public Boolean sendThreadMessage(String threadId, String message);
	
	public List<ThreadItem> getThreadItems(String threadId);
	
}