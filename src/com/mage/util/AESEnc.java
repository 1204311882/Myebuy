package com.mage.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class AESEnc {
	
	
	public static SecretKeySpec getKey(String passwd) {
		KeyGenerator gen;
		try {
		gen = KeyGenerator.getInstance("AES");
		gen.init(128,new SecureRandom(passwd.getBytes()));
		SecretKey theKey = gen.generateKey();
		byte[] enCodeFormat = theKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		return key; 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * AES����
	 * @param msg
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 */
	public static String AESencrypt(String msg,SecretKeySpec key)  {
		
		try {
			
			//System.out.println(new String(Base64.encodeBase64(key.getEncoded())));
			Cipher cipher =Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			String outStr;
			outStr = Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));//�õ�������ʹ��BASE64����
			return outStr;
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
	}
	
	public static String AESdecrypt(String ctext, SecretKeySpec key)  {
		try {
			byte[] inputByte = Base64.decodeBase64(ctext.getBytes("UTF-8"));//����
			/*KeyGenerator gen = KeyGenerator.getInstance("AES");// ����AES��Key������
			gen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = gen.generateKey();// �����û����룬����һ����Կ
			
			byte[] enCodeFormat = secretKey.getEncoded();// ���ػ��������ʽ����Կ
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// ת��ΪAESר����Կ*/
			//System.out.println(new String(Base64.encodeBase64(key.getEncoded())));
			Cipher cipher = Cipher.getInstance("AES");// ����������
			cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��Ϊ����ģʽ��������
			String result = new String(cipher.doFinal(inputByte));  
			return result; // ����  
		}catch (NoSuchAlgorithmException e) {
			 e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			 e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
	        e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String []args)  {
		SecretKeySpec key2 = AESEnc.getKey("123");
		String aaa=new String(Base64.encodeBase64(key2.getEncoded()));//ת��Ϊ�ַ���
		byte[] decodedKey = Base64.decodeBase64(aaa);
		// rebuild key using SecretKeySpec
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");//��ԭΪsecretkey
		System.out.println(aaa);
		System.out.println(new String(Base64.encodeBase64(originalKey.getEncoded())));
		
		String name="123456781";
		String pass="xiangwenqi";
		SecretKeySpec key = AESEnc.getKey(pass);
		String c=AESEnc.AESencrypt(name, key);
		String n=AESEnc.AESdecrypt(c, key);
		System.out.println("key:"+new String(Base64.encodeBase64(key.getEncoded()))); 
		System.out.println("cipher:"+c);
		System.out.println("msg:"+n);
		System.out.println(name.equals(n));
		
	}
}
