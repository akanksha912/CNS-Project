import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
public class AES {
	static ArrayList<Long> AESEnc128Arr = new ArrayList<Long>();
	static ArrayList<Long> AESDec128Arr = new ArrayList<Long>();
	static ArrayList<Long> AESEnc256Arr = new ArrayList<Long>();
	static ArrayList<Long> AESDec256Arr = new ArrayList<Long>();
	public void AESEntry(String filename) {
		try {
			File file = new File(filename);
		      FileInputStream fin = new FileInputStream(file);
		      BufferedInputStream bin = new BufferedInputStream(fin);

		      byte[] contents = new byte[(int) file.length()];
		      while ((bin.read(contents)) != -1) {
		      }
		      for(int i =0; i< 100;i++) {
		    	  	System.out.print("["+i+"]");
		    	  	timeAes(contents,128);
		    	  	timeAes(contents,256);
		      }
		      meanAndMedian();
	    bin.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void timeAes(byte [] contents, int size) {
		final Key aesKey = keyGen(size);
		IvParameterSpec ivSpec = getInitalVector();
		try {
			Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		    cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);  
		    long beginEncryptTime = System.nanoTime();
		    byte[] encrypted = cipher.doFinal(contents);
		    long endEncryptTime = System.nanoTime();
		    
		    if(size == 128) {
		    		System.out.println("Time elapsed for AES-128 encryption is " + (double)(endEncryptTime - beginEncryptTime)/1000000000);
		    		AESEnc128Arr.add(endEncryptTime - beginEncryptTime);
		    }
		    if(size == 256) {
	    		System.out.println("Time elapsed for AES-256 encryption is " + (double)(endEncryptTime - beginEncryptTime)/1000000000);
	    		AESEnc256Arr.add(endEncryptTime - beginEncryptTime);
	    }
		    cipher.init(Cipher.DECRYPT_MODE, aesKey, ivSpec);
		    long beginDecryptTime = System.nanoTime();
			cipher.doFinal(encrypted);
		    long endDecryptTime = System.nanoTime();
		    if(size == 128) {
		    	System.out.println("Time elapsed for AES-128 decryption is " + (double)(endDecryptTime - beginDecryptTime)/1000000000);
			    AESDec128Arr.add(endDecryptTime - beginDecryptTime);
		    }
		    if(size == 256) {
		    	System.out.println("Time elapsed for AES-256 decryption is " + (double)(endDecryptTime - beginDecryptTime)/1000000000);
			    AESDec256Arr.add(endDecryptTime - beginDecryptTime);
		    }
		    
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	public static Key keyGen(int keySize) {
		 SecureRandom prng = new SecureRandom();
	     byte[] aesKeyData  = new byte[keySize / Byte.SIZE];
	    prng.nextBytes(aesKeyData);
	     Key aesKey = new SecretKeySpec(aesKeyData, "AES");
	    return aesKey;
	}
	public static IvParameterSpec getInitalVector() {
		 SecureRandom prng = new SecureRandom();
	     byte[] initialVector = new byte[128 / Byte.SIZE];
	    prng.nextBytes(initialVector);
	    IvParameterSpec ivSpec = new IvParameterSpec(initialVector);
	    return ivSpec;
	    
	    
	}
	 private static double median(ArrayList <Long>arr) {
		  
		  Collections.sort(arr);

		    long middle = arr.size()/2;
		        if (arr.size()%2 == 1) {
		        		middle = (arr.get(arr.size()/2) + arr.get(arr.size()/2 - 1))/2;
		        } else {
		        		middle = arr.get(arr.size()/2);
		        }
		      return (double) middle/1000000000;
	  }
	  private static double mean(ArrayList <Long> arr) {
		  long sum = 0;
		  if(!arr.isEmpty()) {
			    for (long mark : arr) {
			        sum += mark;
			    }
			    sum = sum / arr.size();
			  }
			  return (double )sum /1000000000;
	  }
	  public static void meanAndMedian() {
		  System.out.println("Encryption using AES-128");
		  System.out.println("median : " + median(AESEnc128Arr));
		  System.out.println("mean : " + mean(AESEnc128Arr));
		  
		  System.out.println("Decryption using AES-128");
		  System.out.println("median : " + median(AESDec128Arr));
		  System.out.println("mean : " + mean(AESDec128Arr));
		  
		  System.out.println("Encryption using AES-256");
		  System.out.println("median : " + median(AESEnc256Arr));
		  System.out.println("mean : " + mean(AESEnc256Arr));
		  
		  System.out.println("Decryption using AES-256");
		  System.out.println("median : " + median(AESDec256Arr));
		  System.out.println("mean : " + mean(AESDec256Arr));
		  
		  
		  
		  
	  }

}
