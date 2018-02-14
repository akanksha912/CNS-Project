
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;

public class RSAEncryption {
	static ArrayList<Long> Encryption1024 = new ArrayList<Long>();
	static ArrayList<Long> Decryption1024 = new ArrayList<Long>();
	static ArrayList<Long> Encryption4096 = new ArrayList<Long>();
	static ArrayList<Long> Decryption4096 = new ArrayList<Long>();

	static ArrayList<byte[]> chunkedText4096 = new ArrayList<byte[]>();
	static ArrayList<byte[]> chunkedText1024 = new ArrayList<byte[]>();

	public static void generateKey(int keySize) {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(keySize);
			KeyPair key = keyGen.generateKeyPair();

			File privFile = new File("./keys/private.key");
			File pubFile = new File("./keys/public.key");
			if (privFile.getParentFile() != null) {
				privFile.getParentFile().mkdirs();
			}
			privFile.createNewFile();

			if (pubFile.getParentFile() != null) {
				pubFile.getParentFile().mkdirs();
			}
			pubFile.createNewFile();

			ObjectOutputStream puos = new ObjectOutputStream(new FileOutputStream(pubFile));
			puos.writeObject(key.getPublic());
			puos.close();

			// Saving the Private key in a file
			ObjectOutputStream pros = new ObjectOutputStream(new FileOutputStream(privFile));
			pros.writeObject(key.getPrivate());
			pros.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static ArrayList<byte[]> encrypt(ArrayList<byte[]> text, PublicKey key, int keySize) {
		ArrayList<byte[]> cipherText = new ArrayList<byte[]>();
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			long beginEncryptTime = System.currentTimeMillis();
			for (int i = 0; i < text.size(); i++) {
				cipherText.add(cipher.doFinal(text.get(i)));
			}

			long endEncryptTime = System.currentTimeMillis();
			if(keySize == 1024) {
				System.out.println("Time elapsed for RSA-1024 encrypting is " + (double)(endEncryptTime - beginEncryptTime)/1000);
				Encryption1024.add(endEncryptTime - beginEncryptTime);
			}
			if(keySize == 4096) {
				System.out.println("Time elapsed for RSA-4096 encrypting is " + (double)(endEncryptTime - beginEncryptTime)/1000);
				Encryption4096.add(endEncryptTime - beginEncryptTime);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherText;
	}

	public static void decrypt(ArrayList<byte[]> text, PrivateKey key, int keySize) {
		ArrayList<byte[]> decryptedText = new ArrayList<byte[]>();
		try {
			Cipher cipher = Cipher.getInstance("RSA");

			cipher.init(Cipher.DECRYPT_MODE, key);
			long beginDecryptTime = System.currentTimeMillis();
			for (int i = 0; i < text.size(); i++) {

				decryptedText.add(cipher.doFinal(text.get(i)));
			}
			long endDecryptTime = System.currentTimeMillis();
			if(keySize == 1024) {
				System.out.println("Time elapsed for RSA-1024 decrypting is " + (double)(endDecryptTime - beginDecryptTime)/1000);
				Decryption1024.add(endDecryptTime - beginDecryptTime);
			}
			if(keySize == 4096) {
				System.out.println("Time elapsed for RSA-4096 decrypting is " + (double)(endDecryptTime - beginDecryptTime)/1000);
				Decryption4096.add(endDecryptTime - beginDecryptTime);
			}
			

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static double median(ArrayList<Long> arr) {

		Collections.sort(arr);

		long middle = arr.size() / 2;
		if (arr.size() % 2 == 1) {
			middle = (arr.get(arr.size() / 2) + arr.get(arr.size() / 2 - 1)) / 2;
		} else {
			middle = arr.get(arr.size() / 2);
		}
		return (double)middle/1000;
	}

	private static double mean(ArrayList<Long> arr) {
		long sum = 0;
		if (!arr.isEmpty()) {
			for (long mark : arr) {
				sum += mark;
			}
			sum = sum / arr.size();
		}
		return (double)sum/1000;
	}

	public static void meanAndMedian() {
		System.out.println("Encrypting using RSA-1024");
		System.out.println("median : " + median(Encryption1024));
		System.out.println("mean : " + mean(Encryption1024));

		System.out.println("Decrypting using RSA-1024");
		System.out.println("median : " + median(Decryption1024));
		System.out.println("mean : " + mean(Decryption1024));
		
		System.out.println("Encrypting using RSA-4096");
		System.out.println("median : " + median(Encryption4096));
		System.out.println("mean : " + mean(Encryption4096));

		System.out.println("Decrypting using RSA-4096");
		System.out.println("median : " + median(Decryption4096));
		System.out.println("mean : " + mean(Decryption4096));

	}

	@SuppressWarnings("resource")
	public void EncryptionEntry(String filename) {

		try {
			File file = new File(filename);
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			byte[] contents = new byte[(int) file.length()];
			while ((bin.read(contents)) != -1) {

			}
			int chunk1024 = 117; 
			for (int i = 0; i < contents.length; i += chunk1024) {
				chunkedText1024.add(Arrays.copyOfRange(contents, i, Math.min(contents.length, i + chunk1024)));
			}
			for (int i = 0; i < 2; i++) {
				generateKey(1024);
				ObjectInputStream inputStream = null;

				inputStream = new ObjectInputStream(new FileInputStream("./keys/public.key"));
				PublicKey publicKey = (PublicKey) inputStream.readObject();

				inputStream = new ObjectInputStream(new FileInputStream("./keys/private.key"));
				PrivateKey privateKey = (PrivateKey) inputStream.readObject();
				System.out.print("[" + i + "]");
				ArrayList<byte[]> cipherText = encrypt(chunkedText1024, publicKey,1024);
				System.out.print("[" + i + "]");
				decrypt(cipherText, privateKey,1024);

			}
			int chunk4096 = 501; 
			for (int i = 0; i < contents.length; i += chunk4096) {
				chunkedText4096.add(Arrays.copyOfRange(contents, i, Math.min(contents.length, i + chunk4096)));
			}
			
			for (int i = 0; i < 2; i++) {
				generateKey(4096);
				ObjectInputStream inputStream = null;

				inputStream = new ObjectInputStream(new FileInputStream("./keys/public.key"));
				PublicKey publicKey = (PublicKey) inputStream.readObject();

				inputStream = new ObjectInputStream(new FileInputStream("./keys/private.key"));
				PrivateKey privateKey = (PrivateKey) inputStream.readObject();
				System.out.print("[" + i + "]");
				ArrayList<byte[]> cipherText = encrypt(chunkedText4096, publicKey,4096);
				System.out.print("[" + i + "]");
				decrypt(cipherText, privateKey,4096);

			}
			
			meanAndMedian();
			bin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
