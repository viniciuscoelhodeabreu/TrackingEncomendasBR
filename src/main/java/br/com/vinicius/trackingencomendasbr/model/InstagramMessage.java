package br.com.vinicius.trackingencomendasbr.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class InstagramMessage {

	private final String message;
	private final String threadId;
	
}
