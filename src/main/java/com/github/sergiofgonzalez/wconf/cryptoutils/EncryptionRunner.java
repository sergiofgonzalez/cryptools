package com.github.sergiofgonzalez.wconf.cryptoutils;

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

/*
 * Simple class that displays on the console 
 * an Initialization Vector for the encryption
 * operations.
 * 
 * Note that the algorithm is fixed to AES/CBC/PKCS5Padding
 * 
 */
public class EncryptionRunner {

	private static String CLEAR_TEXT_TO_ENCRYPT = "This value has been encrypted and put in a Java system property";
	
	private static String KEYSTORE_PATH = "./src/test/resources/config/wconf-keystore.jceks";
	private static String KEYSTORE_TYPE = "JCEKS";
	private static String KEYSTORE_PASS = "mystorepasswd";
	private static String KEYSTORE_KEY_ALIAS = "wconf-secret-key";
	private static String KEYSTORE_KEY_PASS = "mykeypasswd";
	private static String INITIALIZATION_VECTOR = "D3IwGkX2iRtIVE46CwdOEg==";
	
	private static String ENCRYPTION_KEYTYPE = "AES";
	private static String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionRunner.class);
	
	
	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// Loading key from key store
		InputStream keystoreStream = new FileInputStream(KEYSTORE_PATH);
		KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
		keystore.load(keystoreStream, KEYSTORE_PASS.toCharArray());
		if (!keystore.containsAlias(KEYSTORE_KEY_ALIAS)) {
			LOGGER.error("Could not found the key with alias {} in the keystore {}", KEYSTORE_KEY_ALIAS, KEYSTORE_PATH);
			throw new IllegalStateException("Couldn't found the given key in the provided keystore");
		}
		
		Key key = keystore.getKey(KEYSTORE_KEY_ALIAS, KEYSTORE_KEY_PASS.toCharArray());
		
		byte[] ivBytes = Base64.getDecoder().decode(INITIALIZATION_VECTOR);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
		
		// Encrypt
		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), ENCRYPTION_KEYTYPE);
		Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		
		byte[] inputBytes = CLEAR_TEXT_TO_ENCRYPT.getBytes(StandardCharsets.UTF_8);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] output = cipher.doFinal(inputBytes);
		
		String cipherText = Base64.getEncoder().encodeToString(output);
		System.out.println("Successfully encrypted `" + CLEAR_TEXT_TO_ENCRYPT + "` according to the given parameters:\n"
				+ "\"cipher(" + cipherText + ")\"");
	}
}
