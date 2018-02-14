public class MainClass {

	public static void main(String[] args) {
		String filename = args[0];
		// TODO Auto-generated method stub
		HMAC hmac = new HMAC();
		AES aes = new AES();
		Signatures sign = new Signatures();
		RSAEncryption encrypt = new RSAEncryption();
		
		try {
			hmac.HMACEntry(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			aes.AESEntry(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sign.SignatureEntry(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			encrypt.EncryptionEntry(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}