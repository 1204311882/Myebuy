package com.mage.util;

import java.security.SecureRandom;
import org.apache.tomcat.util.codec.binary.Base64;


/**
 * hash:ʹ��SecureRandom���Σ�ʹ��SHA-256������hash
 * ��hashֵ���ξ��洢���û���Ӧ�����ݿ�
 * @author 35250
 *
 */
public class passwdEncrpt {
	
	
	public static void  Encrypt(String passwd) {
		SecureRandom random = new java.security.SecureRandom();
		byte bytes[] = new byte[20];
	    random.nextBytes(bytes);
	    String salt = Base64.encodeBase64String(bytes);
	    //System.out.println("salt:" + salt);
	    //String Hash=Sha256Util.getSHA256(passwd);//ֱ��hash
	    String saltHash=Sha256Util.getSHA256(passwd+salt);//����֮��hash
	    //TODO  1.��hash���μ������ݿ�---ע��
	    
	    //System.out.println("Hash:"+Hash);
	    //System.out.println("saltHash:"+saltHash);
	    
		
	}
	/*public static void main(String []args) {
		Encrypt("123456");
		String Hash = Sha256Util.getSHA256("123456"+"gxtrvBdKaAxznYonYb/avHRwBzE=");
		System.out.println(Hash.equals("b588393e05592d8a5f549770583666b2af530d1d07e5ebe0c97c5f1c2e40c454"));
		
	}*/
}

