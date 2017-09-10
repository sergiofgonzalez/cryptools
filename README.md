# cryptools
> simplifying the encryption and decryption of strings

## Description
Use *cryptools* application to:
+ Generate an initialization vector for a symmetric encription scheme (e.g. AES256)
+ Obtain the encrypted and base64 hash for a clear text 
+ Obtain the clear text from an encoded in base64 and encrypted hash

The tool was specifically designed to simplify the encryption of configuration properties for [wconf](https://github.com/sergiofgonzalez/waterfall-config).

## Usage
Once the repository has been cloned, create a runnable jar by using:
```bash
$ mvn clean package
```

Then, create an `application.conf` file with the details:

```

modes {
  available: [encrypt, decrypt, geniv]
  active: <type the action you want to run>
}

# Required data for generating an IV
geniv {
  algorithm: "<the encryption algorithm to use, e.g. AES/CBC/PKCS5Padding>"
}

# Required data for generating encryption
encrypt {
  algorithm: "<the encryption algorithm to use, e.g. AES/CBC/PKCS5Padding>"
  keystore {
    path: <the key store containing the secret key for the encryption, e.g. wconf-keystore.jceks>
    type: <the key store type, e.g. JCEKS>
    passwd: <the password for the key store, e.g. mystorepasswd>
    key {
      alias: <the alias for the secret key within the key store, e.g. wconf-secret-key>
      type:  <the type of the key, e.g. AES>
      passwd: <the password for the key, e.g. mykeypasswd>
    }
  }
  iv: <the initialization vector for encryption process, e.g. "D3IwGkX2iRtIVE46CwdOEg==">
  
  clear_text_to_encrypt: <the clear text you want to encrypt, e.g. "This is some value I want to encrypt">
}

# Required data for decryption
decrypt {
  algorithm: "<the encryption algorithm to use, e.g. AES/CBC/PKCS5Padding>"
  keystore {
    path: <the key store containing the secret key for the encryption, e.g. wconf-keystore.jceks>
    type: <the key store type, e.g. JCEKS>
    passwd: <the password for the key store, e.g. mystorepasswd>
      alias: <the alias for the secret key within the key store, e.g. wconf-secret-key>
      type:  <the type of the key, e.g. AES>
      passwd: <the password for the key, e.g. mykeypasswd>
    }
  }
  iv: <the initialization vector for encryption process, e.g. "D3IwGkX2iRtIVE46CwdOEg==">
  
  encrypted_text_to_decrypt: <the cipher text encoded in base64 you want to decrypt, e.g. "g/A4P+cWMl1bOEcCiYSmEVxr77oknf4RhtTHVpws2tl29YBAat7bsQZer8ypg7BL">
}
```   

Note that it is assumed that you already have a key store with a key for the encryption and decryption modes.

Then, you can run:
java -jar cryptools-1.0.0-SNAPSHOT.jar

## Unlimited Strength Considerations
Some encryption schemes are only enabled when the Java Cryptography Extension (JCE) Unlimited Strength package has been enabled (e.g. AES256).

An exception of type `java.security.InvalidKeyException: Illegal key size` will be raised when trying to use a strong security scheme without having enabled that package.

## License
The license is MIT, see [LICENSE](./LICENSE) for the details.

## Contributing
Contributions will be gladly accepted, but this section is currently in progress.
For general information see [general guidelines for contributing on GitHub](https://guides.github.com/activities/contributing-to-open-source/).