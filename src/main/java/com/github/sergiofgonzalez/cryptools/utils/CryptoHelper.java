package com.github.sergiofgonzalez.cryptools.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sergiofgonzalez.cryptools.utils.domain.KeyInfo;
import com.github.sergiofgonzalez.cryptools.utils.domain.KeystoreInfo;

public class CryptoHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoHelper.class);
	
	public static String encrypt(KeyInfo keyInfo, String iv, String algorithm, String clearText) {
		KeyStore keystore = getKeystore(keyInfo.getKeystoreInfo());
		Key key = getKey(keystore, keyInfo);

		try {
			byte[] ivBytes = Base64.getDecoder().decode(iv);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), keyInfo.getType());
			Cipher cipher = Cipher.getInstance(algorithm);
		
			byte[] inputBytes = clearText.getBytes(StandardCharsets.UTF_8);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			byte[] output = cipher.doFinal(inputBytes);
			String cipherText = Base64.getEncoder().encodeToString(output);
			
			return cipherText;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error("Could not perform encryption");
			throw new IllegalStateException("Could not perform encryption", e);
		}		
	}
	
	public static String decrypt(KeyInfo keyInfo, String iv, String algorithm, String cipherText) {
		KeyStore keystore = getKeystore(keyInfo.getKeystoreInfo());
		Key key = getKey(keystore, keyInfo);

		try {
			byte[] ivBytes = Base64.getDecoder().decode(iv);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), keyInfo.getType());
			Cipher cipher = Cipher.getInstance(algorithm);
			
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
			byte[] inputBytes = Base64.getDecoder().decode(cipherText);
			byte[] clearBytes = cipher.doFinal(inputBytes);
			
			String clearText = new String(clearBytes, StandardCharsets.UTF_8);
			
			return clearText;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error("Could not perform encryption");
			throw new IllegalStateException("Could not perform encryption", e);
		}		
	}
	
	private static KeyStore getKeystore(KeystoreInfo keystoreInfo) {
		try {
			InputStream keystoreStream = new FileInputStream(keystoreInfo.getPath());
			KeyStore keystore = KeyStore.getInstance(keystoreInfo.getType());
			keystore.load(keystoreStream, keystoreInfo.getPasswd().toCharArray());
			return keystore;
		} catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException e) {
			LOGGER.error("Could not load the configured keystore: {}", keystoreInfo.getPath());
			throw new IllegalArgumentException("Keystore could not be loaded", e);
		}		
	}
	
	private static Key getKey(KeyStore keystore, KeyInfo keyInfo) {
		try {
			if (!keystore.containsAlias(keyInfo.getAlias())) {
				LOGGER.error("The key alias {} could not be located in the keystore {}", keyInfo.getAlias(), keyInfo.getKeystoreInfo().getPath());
				throw new IllegalArgumentException("key alias could not be located in the keystore");
			}
			Key key = keystore.getKey(keyInfo.getAlias(), keyInfo.getPasswd().toCharArray());
			
			return key;
		} catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
			LOGGER.error("Could not read key {} from loaded keystore", keyInfo.getAlias());
			throw new IllegalArgumentException("Could not read key from keystore", e);
		}
	}
	
}
