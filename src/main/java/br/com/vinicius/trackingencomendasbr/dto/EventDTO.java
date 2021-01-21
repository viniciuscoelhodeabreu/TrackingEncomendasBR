package br.com.vinicius.trackingencomendasbr.dto;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.com.vinicius.trackingencomendasbr.util.Util;
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
	
	
	public Date toDate() {
		return Util.dateFromString("dd/MM/yyyy HH:mm", date.concat(" ").concat(hour));
	}
}
