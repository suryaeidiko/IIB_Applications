package com.java.EncryptionDecryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionDecryption {
	private static SecretKeySpec secretkey;
	private static byte [] Key;
	
	
	public static void main(String []args) {
		String privateKey = "myKey";
		
		String Encryption = Encryption("make me encrypt decrypt", privateKey);
		System.out.println("Encryption String :: "+Encryption);
		
		String decryption = Decryption(Encryption, privateKey);
		System.out.println("Decryption String :: "+decryption);
		
	}
	//set Key
public static void setkey(String MyKey) {
	try {
		Key = MyKey.getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		Key = sha.digest(Key);
		Key = Arrays.copyOf(Key, 16);
		secretkey = new SecretKeySpec(Key, "AES");
				
	}
	catch (UnsupportedEncodingException e) {}
	catch (NoSuchAlgorithmException e) {}
}


		//Encryption
	
public static String Encryption(String StringToEncrypt,String passwd) {
	try {
		setkey(passwd);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretkey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(StringToEncrypt.getBytes("UTF-8")));
	    
		
	} catch (Exception e) {
		System.out.println("Exception :: "+e);
	}
	return null;
}

		//Decryption

public static String Decryption(String StringToDecrypt, String passwd) {
	
	try {
		setkey(passwd);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretkey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(StringToDecrypt)));
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	return null;
}

}
