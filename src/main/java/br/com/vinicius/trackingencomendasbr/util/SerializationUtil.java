package br.com.vinicius.trackingencomendasbr.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.utils.IGUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class SerializationUtil {

	private static final String BASE_PATH = Util.getSystemTemporaryPath();
	private static final String CLIENT_FILE_NAME = "igclient.ser";
	private static final String COOKIE_FILE_NAME = "cookie.ser";


	public static void serializeLogin(IGClient client, SerializableCookieJar serializableCookieJar) throws IOException {
		File igClientFile = new File(SerializationUtil.BASE_PATH + CLIENT_FILE_NAME);
		File cookieFile = new File(SerializationUtil.BASE_PATH + COOKIE_FILE_NAME);

		serialize(client, igClientFile);
		serialize(serializableCookieJar, cookieFile);
	}

	public static Optional<IGClient> getClientFromSerialize() {
		try {
			File clientFile = new File(BASE_PATH + CLIENT_FILE_NAME);
			File cookieFile = new File(BASE_PATH + COOKIE_FILE_NAME);
			SerializableCookieJar serializableCookieJar = deserialize(cookieFile, SerializableCookieJar.class);
			
			if(serializableCookieJar.expiresAt().isPresent() && InstagramUtil.needsToRenewCookie(serializableCookieJar.expiresAt().get()))
				return Optional.empty();
			
			InputStream fileIn = new FileInputStream(clientFile);
			IGClient client = IGClient.from(fileIn, formHttpClient(serializableCookieJar));
			fileIn.close();
			
			return Optional.of(client);
		}catch (ClassNotFoundException | IOException e) {
			return Optional.empty();
		}
	}

	public static void serialize(Object object, File file) throws IOException {
		FileOutputStream fileOutput = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOutput);

		if(file.exists())
			file.delete();

		file.createNewFile();
		out.writeObject(object);
		out.close();
		fileOutput.close();
	}

	public static <T> T deserialize(File file, Class<T> clazz) throws IOException, ClassNotFoundException {
		InputStream in = new FileInputStream(file);
		ObjectInputStream oIn = new ObjectInputStream(in);
		T t = clazz.cast(oIn.readObject());

		in.close();
		oIn.close();

		return t;
	}

	public static OkHttpClient formHttpClient() {
		return IGUtils.defaultHttpClientBuilder()
				.build();
	}

	public static OkHttpClient formHttpClient(SerializableCookieJar jar) {
		return IGUtils.defaultHttpClientBuilder().cookieJar(jar).build();
	}

	@AllArgsConstructor
	public static class SerializableCookieJar implements CookieJar, Serializable{

		private static final long serialVersionUID = -837498359387593793l;
		private final Map<String, List<SerializableCookie>> map;
		
		public SerializableCookieJar() {
			this.map = new HashMap<String, List<SerializableCookie>>();
		}

		@Override
		public List<Cookie> loadForRequest(HttpUrl arg0) {
			return map.getOrDefault(arg0.host(), new ArrayList<SerializableCookie>()).stream()
					.map(c -> c.cookie)
					.collect(Collectors.toList());
		}

		@Override
		public void saveFromResponse(HttpUrl arg0, List<Cookie> arg1) {
			final List<SerializableCookie> list = arg1.stream().map(c -> new SerializableCookie(c)).collect(Collectors.toList());
			if (map.putIfAbsent(arg0.host(), list) != null) {
				map.get(arg0.host()).addAll(list);
			}
		}
		
		public Optional<Long> expiresAt() {
			try {
				return Optional.of(map.values().stream().findFirst().get().get(0).getCookie().expiresAt());
			}catch(Exception e) {
				return Optional.empty();
			}
		}
		
		
		@AllArgsConstructor
		class SerializableCookie implements Serializable {

			private static final long serialVersionUID = -8594045714036645534L;

			@Getter
			private transient Cookie cookie;

			private void writeObject(ObjectOutputStream out) throws IOException {
				out.writeObject(cookie.name());
				out.writeObject(cookie.value());
				out.writeLong(cookie.persistent() ? cookie.expiresAt() : -1);
				out.writeObject(cookie.domain());
				out.writeObject(cookie.path());
				out.writeBoolean(cookie.secure());
				out.writeBoolean(cookie.httpOnly());
				out.writeBoolean(cookie.hostOnly());
			}

			private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
				final Cookie.Builder builder = new Cookie.Builder();

				builder.name((String) in.readObject());
				builder.value((String) in.readObject());

				long expiresAt = in.readLong();
				if (expiresAt != -1) {
					builder.expiresAt(expiresAt);
				}

				final String domain = (String) in.readObject();
				builder.domain(domain);

				builder.path((String) in.readObject());

				if (in.readBoolean())
					builder.secure();

				if (in.readBoolean())
					builder.httpOnly();

				if (in.readBoolean())
					builder.hostOnlyDomain(domain);

				cookie = builder.build();
			}
			
			
		}
	}
}
