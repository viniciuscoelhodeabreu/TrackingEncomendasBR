package br.com.vinicius.trackingencomendasbr.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.users.UserAction;

import br.com.vinicius.trackingencomendasbr.util.InstagramUtil;

public class InstagramTest {

	private IGClient igClient;
	
	public InstagramTest() {
		igClient = InstagramUtil.establishClient();
	}
	
	@Test
	public void shouldReturnTheDefaultUserProfile() {
		CompletableFuture<UserAction> user = igClient.getActions().users().findByUsername(System.getenv("INSTAGRAM_USERNAME"));
		
		assertNotNull(user.join().getUser(), "Busca do usuário padrão do Instagram");
	}
	
}
