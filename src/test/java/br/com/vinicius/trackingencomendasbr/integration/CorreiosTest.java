package br.com.vinicius.trackingencomendasbr.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import br.com.vinicius.trackingencomendasbr.dto.TrackingDTO;
import br.com.vinicius.trackingencomendasbr.util.Constants;
import br.com.vinicius.trackingencomendasbr.util.HttpUtil;
import lombok.SneakyThrows;

public class CorreiosTest {
	
	private static final String CORREIOS_DEFAULT_PACKAGE_CODE = "AA123456789BR";
	
	@Test
	@Order(1)
	public void shouldLinkAndTrackCredentialsSystemVariableNotNull() {
		
		assertNotNull(System.getenv("LINKANDTRACK_USER"),  "Usuário do Link&Track não é nulo.");
		assertNotNull(System.getenv("LINKANDTRACK_TOKEN"), "Token do Link&Track não é nulo.");
	}
	
	@Test
	@Order(2)
	@SneakyThrows(IOException.class)
	public void shouldReturnOkStatusCodeAndValidService() {
		String linkAndTrackUser = System.getenv("LINKANDTRACK_USER");
		String linkAndTrackToken = System.getenv("LINKANDTRACK_TOKEN");
		
		Optional<HttpResponse> httpResponse = HttpUtil.doGet(Constants.getLinkAndTrackUrl(linkAndTrackUser, linkAndTrackToken, CORREIOS_DEFAULT_PACKAGE_CODE));
		String response = EntityUtils.toString(httpResponse.get().getEntity());
		
		TrackingDTO tracking = new Gson().fromJson(response, TrackingDTO.class);
		
		assertTrue(httpResponse.isPresent());
		assertEquals(200, httpResponse.get().getStatusLine().getStatusCode(), "O retorno do Link&Track é Status Code =  OK");
		assertNotNull(tracking.getEvents(), "A lista de eventos do pacote código default não é nula.");
		assertNotNull(tracking.getService(), "O serviço do pacote código default não é nulo.");
		assertFalse(tracking.getService().isEmpty(), "O serviço do pacote código default não é vazio.");
	}
	
}
