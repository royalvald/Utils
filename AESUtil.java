package AESEncryption;

import java.io.IOException;
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

/**
 * Created by pc on 2018/5/15.
 */

public class AESUtil {
	/*
	 * ���� 1.������Կ������ 2.����ecnodeRules�����ʼ����Կ������ 3.������Կ 4.�����ͳ�ʼ�������� 5.���ݼ��� 6.�����ַ���
	 */
	public static String AESEncode(String encodeRules, String content) {
		try {
			// 1.������Կ��������ָ��ΪAES�㷨,�����ִ�Сд
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.����ecnodeRules�����ʼ����Կ������
			// ����һ��128λ�����Դ,���ݴ�����ֽ�����
			keygen.init(128, new SecureRandom(encodeRules.getBytes()));
			// 3.����ԭʼ�Գ���Կ
			SecretKey original_key = keygen.generateKey();
			// 4.���ԭʼ�Գ���Կ���ֽ�����
			byte[] raw = original_key.getEncoded();
			// 5.�����ֽ���������AES��Կ
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.����ָ���㷨AES�Գ�������
			Cipher cipher = Cipher.getInstance("AES");
			// 7.��ʼ������������һ������Ϊ����(Encrypt_mode)���߽��ܽ���(Decrypt_mode)�������ڶ�������Ϊʹ�õ�KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.��ȡ�������ݵ��ֽ�����(����Ҫ����Ϊutf-8)��Ȼ��������������ĺ�Ӣ�Ļ�����ľͻ����Ϊ����
			byte[] byte_encode = content.getBytes("utf-8");
			// 9.�����������ĳ�ʼ����ʽ--���ܣ������ݼ���
			byte[] byte_AES = cipher.doFinal(byte_encode);
			// 10.�����ܺ������ת��Ϊ�ַ���
			String AES_encode = parseByte2HexStr(byte_AES);
			// 11.���ַ�������
			return AES_encode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// ����д�ͷ���nulll
		return null;
	}

	/*
	 * ���� ���ܹ��̣� 1.ͬ����1-4�� 2.�����ܺ���ַ������ĳ�byte[]���� 3.���������ݽ���
	 */
	public static String AESDncode(String encodeRules, String content) {
		try {
			// 1.������Կ��������ָ��ΪAES�㷨,�����ִ�Сд
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.����ecnodeRules�����ʼ����Կ������
			// ����һ��128λ�����Դ,���ݴ�����ֽ�����
			keygen.init(128, new SecureRandom(encodeRules.getBytes()));
			// 3.����ԭʼ�Գ���Կ
			SecretKey original_key = keygen.generateKey();
			// 4.���ԭʼ�Գ���Կ���ֽ�����
			byte[] raw = original_key.getEncoded();
			// 5.�����ֽ���������AES��Կ
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.����ָ���㷨AES�Գ�������
			Cipher cipher = Cipher.getInstance("AES");
			// 7.��ʼ������������һ������Ϊ����(Encrypt_mode)���߽���(Decrypt_mode)�������ڶ�������Ϊʹ�õ�KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.�����ܲ����������ݽ�����ֽ�����
			byte[] byte_content = parseHexStr2Byte(content);
			/*
			 * ����
			 */
			byte[] byte_decode = cipher.doFinal(byte_content);
			String AES_decode = new String(byte_decode, "utf-8");
			return AES_decode;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		// ����д�ͷ���nulll
		return null;
	}

	/**
	 * ��������ת����16����
	 * 
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * ��16����ת��Ϊ������
	 * 
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

}