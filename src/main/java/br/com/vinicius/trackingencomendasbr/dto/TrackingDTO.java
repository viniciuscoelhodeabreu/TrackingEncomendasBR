package br.com.vinicius.trackingencomendasbr.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class TrackingDTO {

	@SerializedName(value = "codigo")
	private final String code;
	@SerializedName(value = "servico")
	private final String service;
	private final String host;
	@SerializedName(value = "quantidade")
	private final Integer amount;
	@SerializedName(value = "eventos")
	private final List<EventDTO> events;
	private final Float time;
	@SerializedName(value = "ultimo")
	private final String last;
	
	
}
