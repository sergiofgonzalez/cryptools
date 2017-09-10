package com.github.sergiofgonzalez.cryptools;

import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sergiofgonzalez.cryptools.utils.CryptoHelper;
import com.github.sergiofgonzalez.cryptools.utils.InitializationVectorGenerator;
import com.github.sergiofgonzalez.cryptools.utils.domain.KeyInfo;
import com.github.sergiofgonzalez.cryptools.utils.domain.KeystoreInfo;

import static com.github.sergiofgonzalez.wconf.WaterfallConfig.*;

public class CryptoolsApplicationRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoolsApplicationRunner.class);
		
	public static void main(String[] args) {
		Instant start = Instant.now();

		String opMode = wconf().get("modes.active");
		LOGGER.debug("Running cryptools: {}", wconf().get("modes.active"));
		
		switch (opMode) {
			case "geniv":				
				handleInitializationVectorGeneration();
				break;
				
			case "encrypt":
				handleEncryption();
				break;
				
			case "decrypt":
				handleDecryption();
				break;
				
			default:
				LOGGER.error("Unsupported operation exception: {}", opMode);
				throw new UnsupportedOperationException("Unsupported operation exception");
		}
		
		
		Duration duration = Duration.between(start, Instant.now());
		LOGGER.debug("cryptools execution took {}", duration);
	}
	
	private static void handleInitializationVectorGeneration() {
		String algorithm = wconf().get("geniv.algorithm");
		String iv = InitializationVectorGenerator.getInitializationVector(algorithm);
		System.out.println("Successfully generated iv for `" + algorithm + "`:\n" + iv);
	}
	
	private static void handleEncryption() {
		
		KeystoreInfo keystoreInfo = new KeystoreInfo(
											wconf().get("encrypt.keystore.path"), 
											wconf().get("encrypt.keystore.type"), 
											wconf().get("encrypt.keystore.passwd"));
		
		KeyInfo keyInfo = new KeyInfo(keystoreInfo, 
								wconf().get("encrypt.keystore.key.alias"), 
								wconf().get("encrypt.keystore.key.type"), 
								wconf().get("encrypt.keystore.key.passwd"));
		
		String encryptedText = CryptoHelper.encrypt(keyInfo, 
														wconf().get("encrypt.iv"), 
														wconf().get("encrypt.algorithm"), 
														wconf().get("encrypt.clear_text_to_encrypt"));
		
		System.out.println("Successfully encrypted `" + wconf().get("encrypt.clear_text_to_encrypt") + "` according to the given parameters:\n"
				+ "\"" + encryptedText + "\"");
	}
	
	private static void handleDecryption() {
		
		KeystoreInfo keystoreInfo = new KeystoreInfo(
											wconf().get("decrypt.keystore.path"), 
											wconf().get("decrypt.keystore.type"), 
											wconf().get("decrypt.keystore.passwd"));
		
		KeyInfo keyInfo = new KeyInfo(keystoreInfo, 
								wconf().get("decrypt.keystore.key.alias"), 
								wconf().get("decrypt.keystore.key.type"), 
								wconf().get("decrypt.keystore.key.passwd"));
		
		String clearText = CryptoHelper.decrypt(keyInfo, 
														wconf().get("decrypt.iv"), 
														wconf().get("decrypt.algorithm"), 
														wconf().get("decrypt.encrypted_text_to_decrypt"));
		
		System.out.println("Successfully decrypted `" + wconf().get("decrypt.encrypted_text_to_decrypt") + "` according to the given parameters:\n"
				+ "\"" + clearText + "\"");
	}	
}
