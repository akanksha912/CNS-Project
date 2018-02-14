
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HMAC {

	static ArrayList<Long> SHA1Array = new ArrayList<Long>();
	static ArrayList<Long> SHA256Array = new ArrayList<Long>();
	static ArrayList<Long> MD5Array = new ArrayList<Long>();

	public void HMACEntry(String filename) throws Exception {
		File file = new File(filename);
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fin);

		byte[] contents = new byte[(int) file.length()];
		while ((bin.read(contents)) != -1) {
		}

		for (int i = 0; i < 100; i++) {

			System.out.print("[" + i + "]");
			timeMac("HmacSHA1", contents);
			System.out.print("[" + i + "]");
			timeMac("HmacSHA256", contents);
			System.out.print("[" + i + "]");
			timeMac("HmacMD5", contents);

		}
		meanAndMedian();

		bin.close();

	}

	public static byte[] keyGen() {
		Random ranGen = new SecureRandom();
		byte[] hmacKey = new byte[32];
		ranGen.nextBytes(hmacKey);

		return hmacKey;
	}

	public static void timeMac(String MacType, byte[] msg) {

		byte[] key = keyGen();
		long startTime = System.nanoTime();

		hmacDigest(msg, key, MacType);

		long endTime = System.nanoTime();

		if (MacType == "HmacSHA1") {
			SHA1Array.add(endTime - startTime);
		}
		if (MacType == "HmacSHA256") {
			SHA256Array.add(endTime - startTime);
		}
		if (MacType == "HmacMD5") {
			MD5Array.add(endTime - startTime);
		}
		System.out.println(MacType + " elapsed time is " + (double) (endTime - startTime) / 1000000000);

	}

	private static double median(ArrayList<Long> arr) {

		Collections.sort(arr);

		long middle = arr.size() / 2;
		if (arr.size() % 2 == 1) {
			middle = (arr.get(arr.size() / 2) + arr.get(arr.size() / 2 - 1)) / 2;
		} else {
			middle = arr.get(arr.size() / 2);
		}
		return (double) middle / 1000000000;
	}

	private static double mean(ArrayList<Long> arr) {
		long sum = 0;
		if (!arr.isEmpty()) {
			for (long mark : arr) {
				sum += mark;
			}
			sum = sum / arr.size();
		}
		return (double) sum / 1000000000;
	}

	public static void meanAndMedian() {
		System.out.println("SHA-1");
		System.out.println("median : " + median(SHA1Array));
		System.out.println("mean : " + mean(SHA1Array));

		System.out.println("SHA-256");
		System.out.println("median : " + median(SHA256Array));
		System.out.println("mean : " + mean(SHA256Array));

		System.out.println("MD5");
		System.out.println("median : " + median(MD5Array));
		System.out.println("mean : " + mean(MD5Array));

	}

	public static byte[] hmacDigest(byte[] msg, byte[] keyString, String algo) {
		byte[] bytes = new byte[256];
		try {

			SecretKeySpec key = new SecretKeySpec(keyString, algo);

			Mac mac = Mac.getInstance(algo);

			mac.init(key);
			bytes = mac.doFinal(msg);

		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return bytes;

	}
}
