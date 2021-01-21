package br.com.vinicius.trackingencomendasbr.service;

import java.util.Optional;

import br.com.vinicius.trackingencomendasbr.dto.TrackingDTO;

public interface ILinkAndTrackService {

	public Optional<TrackingDTO> getTrackingByTrackCode(String trackCode);
	
}
