package br.com.vinicius.trackingencomendasbr.util;

public class Constants {

	public static final String LINK_AND_TRACK_URL = "https://api.linketrack.com/track/json?user=%s&token=%s&codigo=%s";
	
	public static final String getLinkAndTrackUrl(String user, String token, String trackCode) {
		return String.format(LINK_AND_TRACK_URL, user, token, trackCode);
	}
}
