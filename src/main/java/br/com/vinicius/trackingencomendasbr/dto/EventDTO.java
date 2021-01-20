package br.com.vinicius.trackingencomendasbr.dto;

import java.util.List;

import lombok.Data;

@Data
public class EventDTO {

	private final String data;
	private final String hora;
	private final String local;
	private final String status;
	private final List<String> subStatus;
	
}
