package com.mage.util;


import javax.crypto.Cipher;

import org.apache.tomcat.util.codec.binary.Base64;
//import java.util.Base64;

import java.security.KeyFactory;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAEncrypt {
	 public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
	        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
	        //PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyText));
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
	        Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
	        byte[] result = cipher.doFinal(text.getBytes());
	        return Base64.encodeBase64String(result);
	        //return Base64.getEncoder().encodeToString(result);
	    }
	 
	 
	 public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
	        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
	        //X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyText));
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
	        Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.DECRYPT_MODE, publicKey);
	        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
	        //byte[] result = cipher.doFinal(Base64.getDecoder().decode(text));
	        return new String(result);
	    }
	 
	 public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
	        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
	        //PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyText));
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
	        Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);
	        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
	        //byte[] result = cipher.doFinal(Base64.getDecoder().decode(text));
	        return new String(result);
	    }

	 
	 public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
	        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
	        //X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyText));
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
	        Cipher cipher = Cipher.getInstance("RSA");
	        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	        byte[] result = cipher.doFinal(text.getBytes());
	        return Base64.encodeBase64String(result);
	        //return Base64.getEncoder().encodeToString(result);
	    }


}


