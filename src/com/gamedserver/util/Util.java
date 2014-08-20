package com.gamedserver.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

public class Util {

	public static String getRandomIdentifier() {
		int blocksNumber = 5;
		int charsPerBlock = 5;

		Random random = new Random();

		char[] identifierBuffer = new char[blocksNumber * charsPerBlock
				+ blocksNumber - 1];

		int letterUpperCaseLowerBound = (int) 'A';
		int letterUpperCaseUpperBound = (int) 'Z';

		int letterLowerCaseLowerBound = (int) 'a';

		int numericLowerBound = (int) '0';
		int numericUpperBound = (int) '9';

		int letterRange = letterUpperCaseUpperBound - letterUpperCaseLowerBound;
		int numericRange = numericUpperBound - numericLowerBound;

		int lowerBounds[] = { letterUpperCaseLowerBound,
				letterLowerCaseLowerBound, numericLowerBound };
		int ranges[] = { letterRange, letterRange, numericRange };

		int index = 0;
		int blocks = 0;
		do {
			int d = random.nextInt(3);

			int rn = random.nextInt(ranges[d]);
			int cn = lowerBounds[d] + rn;

			identifierBuffer[index++] = (char) cn;

			if (blocks < blocksNumber - 1
					&& (index - blocks) % charsPerBlock == 0) {
				identifierBuffer[index++] = '-';
				blocks++;
			}
		} while (index < identifierBuffer.length);

		return new String(identifierBuffer);
	}
	
	final static int secretSize = 10;
	final static int numOfScratchCodes = 5;
	final static int scratchCodeSize = 8;

	public static String generateSecretKey() {
		// Allocating the buffer
		byte[] buffer = new byte[secretSize + numOfScratchCodes * scratchCodeSize];

		// Filling the buffer with random numbers.
		// Notice: you want to reuse the same random generator
		// while generating larger random number sequences.
		new Random().nextBytes(buffer);

		// Getting the key and converting it to Base32
		Base32 codec = new Base32();
		byte[] secretKey = Arrays.copyOf(buffer, secretSize);
		byte[] bEncodedKey = codec.encode(secretKey);
		String encodedKey = new String(bEncodedKey);
		
		return encodedKey;
	}
	
	public static String getQRBarcodeURL(String user, String host, String secret) {
		String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
		return String.format(format, user, host, secret);
	}

	public static boolean check_code(String secret, long code, long timeMsec) throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);
		
		// convert unix msec time into a 30 second "window" 
		// this is per the TOTP spec (see the RFC for details)
		long t = (timeMsec / 1000L) / 30L;

		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		int window = 3;
		
		for (int i = -window; i <= window; ++i) {
			long hash = verify_code(decodedKey, t + i);
			
			if (hash == code) {
				return true;
			}
		}

		// The validation code is invalid.
		return false;
	}

	private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}

		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);

		int offset = hash[20 - 1] & 0xF;

		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}

		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;

		return (int) truncatedHash;
	}
}
