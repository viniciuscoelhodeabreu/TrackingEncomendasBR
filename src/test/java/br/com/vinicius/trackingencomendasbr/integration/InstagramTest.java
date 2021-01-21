package br.com.vinicius.trackingencomendasbr.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.users.UserAction;

import br.com.vinicius.trackingencomendasbr.util.InstagramUtil;

public class InstagramTest {

	
	@Test
	public void shouldInstagramCredentialsSystemVariableNotNull() {
		assertNotNull(System.getenv("INSTAGRAM_USERNAME"), "Usuário do Instagram não é nulo.");
		assertNotNull(System.getenv("INSTAGRAM_PASSWORD"), "Senha do Instagram não é nulo.");
	}
	
	@Test
	public void shouldReturnTheDefaultUserProfile() {
		IGClient igClient = InstagramUtil.establishClient();
		CompletableFuture<UserAction> user = igClient.getActions().users().findByUsername(System.getenv("INSTAGRAM_USERNAME"));
		
		assertNotNull(user.join().getUser(), "Busca do usuário padrão do Instagram");
	}
	
}
