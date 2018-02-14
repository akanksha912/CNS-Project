import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collections;

public class Signatures {

	static ArrayList<Long> SignArr = new ArrayList<Long>();
	static ArrayList<Long> VerifyArr = new ArrayList<Long>();

	public static void generateKey() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(4096);
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

	public static byte[] sign(byte[] plainText, PrivateKey privateKey) throws Exception {
		Signature psignature = Signature.getInstance("SHA256withRSA");
		psignature.initSign(privateKey);
		long beginSignTime = System.nanoTime();
		psignature.update(plainText);

		byte[] signature = psignature.sign();
		long endSignTime = System.nanoTime();
		System.out.println("Time elapsed for signing is " + (double) (endSignTime - beginSignTime) / 1000000000);
		SignArr.add(endSignTime - beginSignTime);

		return signature;
	}

	public static boolean verify(byte[] plainText, byte[] signature, PublicKey publicKey) throws Exception {
		Signature pusignature = Signature.getInstance("SHA256withRSA");
		pusignature.initVerify(publicKey);
		long beginVerifyTime = System.nanoTime();
		pusignature.update(plainText);
		boolean verifyresult = pusignature.verify(signature);
		long endVerifyTime = System.nanoTime();
		System.out.println("Time elapsed for verifying is " + (double) (endVerifyTime - beginVerifyTime) / 1000000000);
		VerifyArr.add(endVerifyTime - beginVerifyTime);
		return verifyresult;
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
		System.out.println("Signing");
		System.out.println("median : " + median(SignArr));
		System.out.println("mean : " + mean(SignArr));

		System.out.println("Verifying");
		System.out.println("median : " + median(VerifyArr));
		System.out.println("mean : " + mean(VerifyArr));

	}

	@SuppressWarnings("resource")
	public void SignatureEntry(String filename) {
		try {
			File file = new File(filename);
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);

			byte[] contents = new byte[(int) file.length()];
			while ((bin.read(contents)) != -1) {
			}
			generateKey();
			ObjectInputStream inputStream = null;

			inputStream = new ObjectInputStream(new FileInputStream("./keys/public.key"));
			final PublicKey publicKey = (PublicKey) inputStream.readObject();
			inputStream = new ObjectInputStream(new FileInputStream("./keys/private.key"));
			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();

			for (int i = 0; i < 100; i++) {
				generateKey();
				System.out.print("[" + i + "]");
				byte[] signature = sign(contents, privateKey);
				System.out.print("[" + i + "]");
				verify(contents, signature, publicKey);
			}
			meanAndMedian();
			bin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
