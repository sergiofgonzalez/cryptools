package com.github.sergiofgonzalez.cryptools.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Supports the generation of an Initialization Vector using a SecureRandom. 
 * 
 * @author sergio.f.gonzalez
 *
 */
public class InitializationVectorGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationVectorGenerator.class);
	
	public static String getInitializationVector(String algorithm) {
		byte[] ivBytes = getRawBytesIv(algorithm);
		String ivString = Base64.getEncoder().encodeToString(ivBytes);
		return ivString;
	}
	
	private static byte[] getRawBytesIv(String algorithm) {
		SecureRandom random = new SecureRandom();
		byte[] iv;
		try {
			iv = new byte[Cipher.getInstance(algorithm).getBlockSize()];
			random.nextBytes(iv);			
			return iv;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			LOGGER.error("Could not generate a valid initialization vector for algorithm {}", algorithm);
			throw new IllegalArgumentException("Could not generate initialization vector", e);
		}
	}
}
