package br.com.vinicius.trackingencomendasbr.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.IGClient.Builder.LoginHandler;
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils;

import br.com.vinicius.trackingencomendasbr.util.SerializationUtil.SerializableCookieJar;

public class InstagramUtil {


	public static IGClient establishClient() {
		final Optional<IGClient> serializedClient = SerializationUtil.getClientFromSerialize();
		
		return serializedClient.isPresent() ? serializedClient.get() : forceEstablishClient().get();
	}

	public static Optional<IGClient> forceEstablishClient() {
		final Scanner scanner = new Scanner(System.in);

		final Callable<String> inputCode = () -> {
			System.out.println("Insira o Código de Autenticação de 2 Etapas: ");
			return scanner.nextLine();
		};

		final LoginHandler twoFactorHandler = (client, response) -> {
			return IGChallengeUtils.resolveTwoFactor(client, response, inputCode);
		};

		SerializableCookieJar serializableCookieJar = new SerializationUtil.SerializableCookieJar();

		try {
			IGClient igClient = IGClient.builder()
					.username(System.getenv("INSTAGRAM_USERNAME"))
					.password(System.getenv("INSTAGRAM_PASSWORD"))
					.onTwoFactor(twoFactorHandler)
					.client(SerializationUtil.formHttpClient(serializableCookieJar))
					.login();

			SerializationUtil.serializeLogin(igClient, serializableCookieJar);

			return Optional.of(igClient);
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}finally {
			scanner.close();
		}
	}
	
	public static Boolean needsToRenewCookie(final long expireTime) {
		final Calendar expireCalendar = Util.getCalendar(new Date(expireTime));
		final Calendar nowCalendar = Util.getCalendar(new Date());
		
		nowCalendar.set(Calendar.YEAR, expireCalendar.get(Calendar.YEAR));
		
		long difference = nowCalendar.getTimeInMillis() - expireCalendar.getTimeInMillis();
		long manyDifferenceDays = TimeUnit.MILLISECONDS.toDays(difference);
		
		return manyDifferenceDays >= 60;
	}

}
