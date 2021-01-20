package br.com.vinicius.trackingencomendasbr.service;

import br.com.vinicius.trackingencomendasbr.dto.TrackingDTO;

public interface ILinkAndTrackService {

	public TrackingDTO getTrackingByTrackCode(String trackCode);
	
}
