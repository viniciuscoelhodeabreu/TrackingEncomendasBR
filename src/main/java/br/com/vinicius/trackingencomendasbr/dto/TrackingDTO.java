package br.com.vinicius.trackingencomendasbr.dto;

import java.util.List;

import lombok.Data;

@Data
public class TrackingDTO {

	private final String codigo;
	private final String servico;
	private final String host;
	private final Integer quantidade;
	private final List<EventDTO> eventos;
	private final Float time;
	private final String ultimo;
}
