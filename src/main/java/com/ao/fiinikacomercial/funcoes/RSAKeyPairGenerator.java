package com.ao.fiinikacomercial.funcoes;


	
	import java.io.File;
	import java.io.FileOutputStream;
	import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

	public class RSAKeyPairGenerator {

	    private static PrivateKey privateKey;
	    private static PublicKey publicKey;
/*
	    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
	        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	        keyGen.initialize(1024);
	        KeyPair pair = keyGen.generateKeyPair();
	        this.privateKey = pair.getPrivate();
	        this.publicKey = pair.getPublic();
	    }
	    
*/
	    public void writeToFile(String path, byte[] key) throws IOException {
	        File f = new File(path);
	        f.getParentFile().mkdirs();

	        FileOutputStream fos = new FileOutputStream(f);
	        fos.write(key);
	        fos.flush();
	        fos.close();
	    }
	    
	    // Ler apartir do ficheiro binario
	    public void readToFile(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	   
	    	PublicKey  publicKey  = null;
	    	PrivateKey privateKey = null;
	    	
	        File publicKeyFile = new File(request.getServletContext().getRealPath("RSA/publicKey"));
	      //  File publicKeyFile = new File("RSA/publicKey");
	        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
	        
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
	        publicKey = keyFactory.generatePublic(publicKeySpec);	        
	        setPublicKey(publicKey);
	        
	        File   privateKeyFile =  new File(request.getServletContext().getRealPath("RSA/privateKey"));	        	
	        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
	        
	        KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
	        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
	        privateKey = keyFactory2.generatePrivate(privateKeySpec);
	        setPrivateKey(privateKey);
	        
	       //System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded())); 
	        
	    }

	    public PrivateKey getPrivateKey() {
	        return privateKey;
	    }

	    public PublicKey getPublicKey() {
	        return publicKey;
	    }
	    
	    public void setPrivateKey(PrivateKey privateKey ) {
	     this.privateKey = privateKey;
	    }

	    public void setPublicKey(PublicKey publicKey) {
	    	this.publicKey = publicKey;	       
	    }
	   /*
	    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
	    	
	        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
	       // keyPairGenerator.readToFile();
	       
	        keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
	        keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
	        System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
	        System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
	       	       
	       // System.out.println(Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
	      
	    }
	    */
	}

