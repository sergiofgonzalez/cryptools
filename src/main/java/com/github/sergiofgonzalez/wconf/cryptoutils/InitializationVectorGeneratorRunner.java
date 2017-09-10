package com.github.sergiofgonzalez.wconf.cryptoutils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/*
 * Simple class that performs the encryption of the given
 * value according to the given settings
 * 
 * 
 */
public class InitializationVectorGeneratorRunner {
	
	private static String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
		SecureRandom random = new SecureRandom();
		byte[] ivBytes = new byte[Cipher.getInstance(ENCRYPTION_ALGORITHM).getBlockSize()];
		random.nextBytes(ivBytes);

		System.out.println("Successfully generated iv for " + ENCRYPTION_ALGORITHM + ": ");
		System.out.println(Base64.getEncoder().encodeToString(ivBytes));
	}
}
