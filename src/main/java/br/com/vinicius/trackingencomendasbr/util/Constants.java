package br.com.vinicius.trackingencomendasbr.util;

public class Constants {

	public static final String LINK_AND_TRACK_URL = "https://api.linketrack.com/track/json?user=%s&token=%s&codigo=%s";
	
	public static final String WELCOME_MESSAGE_INSTAGRAM = "Bem-vindo, %s. "
			+ "												\nPara me usar:"
			+ "												\nMe envie uma mensagem com o código de rastreio da sua encomenda, que vou começar a te notificar quando houver atualização no status!";
	
	public static final String TRACKING_NEW_PACKAGE_MESSAGE = "Agora estou de olho no pacote código: %s. "
			+ "										   \nA última atualização de status dele foi:"
			+ "										    \n%s";
	
	public static final String INVALID_PACKAGE_TRACKING_CODE = "O código informado de rastreio é inválido ou já está sendo rastreado por você."
			+ "													\nLembrando que no momento só aceitamos código de rastreio dos Correios.";
	
	public static final String TRACKING_MESSAGE = "(%s às %s %s) - %s \n %s";
	
	public static final String getLinkAndTrackUrl(String user, String token, String trackCode) {
		return String.format(LINK_AND_TRACK_URL, user, token, trackCode);
	}
	
	public static final String getWelcomeMessageInstagram(String username) {
		return String.format(WELCOME_MESSAGE_INSTAGRAM, username);
	}
	
	public static final String getNewTrackingPackageMessage(String trackingCode, String lastEvent) {
		return String.format(TRACKING_NEW_PACKAGE_MESSAGE, trackingCode, lastEvent);
	}
	
	public static final String getTrackingMessage(String date, String hour, String place, String status, String subStatus) {
		return String.format(TRACKING_MESSAGE, date, hour, place, status, subStatus);
	}
	
}
