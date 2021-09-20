package com.narko.project.resources.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 
 * @author surra arjun utomo
 *
 */
@Service
public class AESUtil {
	private static final Logger log = LogManager.getLogger(AESUtil.class);

	public static String decrypt(String strToDecrypt, String SECRET_KEY, String SALTVALUE) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		String message = "";
		try {

			IvParameterSpec iv = new IvParameterSpec(SALTVALUE.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] result = Base64.getDecoder().decode(strToDecrypt);
			byte[] decValue = cipher.doFinal(result);

			return new String(decValue);
		} catch (Exception e) {
			e.printStackTrace(ps);
			ps.close();
			message = e.getMessage();

			e.printStackTrace();

			return null;

		} finally {
			if (!message.isEmpty() || !message.equals("") || baos.size() > 1) {
				log.trace(AESUtil.class);
				log.error(message);
				log.error("stackTrace : " + baos.toString());
			}

		}
	}

}