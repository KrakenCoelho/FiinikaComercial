package com.ao.fiinikacomercial.funcoes;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.nio.charset.*;
public class RSA {
	
	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
	    Cipher encryptCipher = Cipher.getInstance("RSA");
	    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

	    byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

	    return Base64.getEncoder().encodeToString(cipherText);
	}
	
	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
	    byte[] bytes = Base64.getDecoder().decode(cipherText);

	    Cipher decriptCipher = Cipher.getInstance("RSA");
	    decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

	    return new String(decriptCipher.doFinal(bytes),StandardCharsets.UTF_8);
	}
	
	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
	    Signature privateSignature = Signature.getInstance("SHA1withRSA");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signature = privateSignature.sign();
	    
	    return Base64.getEncoder().encodeToString(signature);
	}
	
	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
	   
		Signature publicSignature = Signature.getInstance("SHA1withRSA");
	    publicSignature.initVerify(publicKey);
	    publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));

	    byte[] signatureBytes = Base64.getDecoder().decode(signature);

	    return publicSignature.verify(signatureBytes);
	}

	
	 public static void mainAux(String[] args) throws Exception {
		       /*
		        RSAKeyPairGenerator pair;
				try {
					pair = new RSAKeyPairGenerator();
					pair.readToFile();
					
					String message = "the answer to life the universe and everything";
		        	//Encrypt the message
		        	String cipherText = encrypt(message, pair.getPublicKey());
		        	System.out.println(cipherText);
		        	//Now decrypt it
		        	String decipheredMessage = decrypt(cipherText, pair.getPrivateKey());
		        	System.out.println(decipheredMessage);
		        	
		        	// Assinar msg
		        	String signature = sign("foobar", pair.getPrivateKey());
		        	System.out.println(signature+" "+signature.length());		        	
		        	//Let's check the signature
		        	boolean isCorrect = verify("foobar", signature, pair.getPublicKey());
		        	System.out.println("Signature correct: " + isCorrect);
		        	
		        	 signature = sign(message, pair.getPrivateKey());
		        	System.out.println(signature+" "+signature.length());		        	
		        	//Let's check the signature
		        	 isCorrect = verify(message, signature, pair.getPublicKey());
		        	System.out.println("Signature correct: " + isCorrect);
		        	
		        	
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        */

	    }

}
