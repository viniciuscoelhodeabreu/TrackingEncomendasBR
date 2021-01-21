package br.com.vinicius.trackingencomendasbr.dto;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class EventDTO {

	@SerializedName(value = "data")
	private final String date;
	@SerializedName(value = "hora")
	private final String hour;
	@SerializedName(value = "local")
	private final String place;
	private final String status;
	private final List<String> subStatus;
	
}
