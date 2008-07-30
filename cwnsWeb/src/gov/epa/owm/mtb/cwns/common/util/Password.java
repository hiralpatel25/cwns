package gov.epa.owm.mtb.cwns.common.util;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
 
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
 
/**
 * An utility class used to encrypt and decrypt passwords. The system store a
 * set of parameters in the web.xml file. Some of these parameters are passwords
 * and should be encrypted to avoid server inspection by non-authorized people.
 * The DES algorithm used here is not a completly safe strategy, but it just add
 * more safety to the configuration file. The more important aspect of the
 * security here designed is to change the salt word before release the software
 * to the server. //<br>
 * <br>
 * <i>this code was based on the one found at <a
 * ref="http://javaalmanac.com/egs/javax.crypto/PassKey.html"> The Java
 * Developers Almanac </a>- very thanks to the original author.
 * 
 * @author $Author$
 * @version $Revision$
 * @see http://java.sun.com/developer/technicalArticles/Security/JCE/
 */
public final class Password {
	/** That�s the system backdoor. */	
	private static String phraseword = "mitXFLNYbqn";	
	
	/** Encrypt algorithm */
	public final static String DES = "DES";
 
	/** Encrytp encoding */
	public final static String UTF8 = "UTF8";
 
	/**
	 * The key format: password-based encrypt with MD5 and DES.
	 * 
	 * @value
	 * @see javax.crypto.spec.PBEKeySpec
	 */
	public static final String PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES";
 
	/**
	 * 8-byte Salt - <b>IMPORTANT </b>: change these bytes before release the
	 * software to the server. If someone with server access knows about this
	 * salt, he or she can easly hack your passwords.
	 */
	private static byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8,
			(byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };
 
	/** Number of interaction used to prepare the parameter to the ciphers. */
	private static final int INTERACTION = 19;
 
	/**
	 * <ul>
	 * <li>Create the key</li>
	 * <li>Prepare the parameter to the ciphers</li>
	 * <li>Create the ciphers</li>
	 * <li>Encode the string into bytes using utf-8</li>
	 * <li>Encrypt</li>
	 * <li>Encode bytes to base64 to get a string</li>
	 * </ul>
	 * 
	 * @param secret
	 *            The password to be decrypted.
	 * @return a plain password.
	 * @throws Exception
	 *             a generic exception caused during the decrypt process.
	 */
	public static String encrypt(String plain) throws Exception {
		KeySpec keySpec = new PBEKeySpec(phraseword.toCharArray(), salt, INTERACTION);
		SecretKey key = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES)
				.generateSecret(keySpec);
		Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
				INTERACTION);
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		byte[] utf8 = plain.getBytes(UTF8);
		byte[] enc = ecipher.doFinal(utf8);
		return new sun.misc.BASE64Encoder().encode(enc);
	}
 
	/**
	 * <ul>
	 * <li>Create the key</li>
	 * <li>Decode base64 to get bytes of the secret word</li>
	 * <li>Prepare the parameter to the ciphers</li>
	 * <li>Decrypt</li>
	 * <li>Decode using utf-8</li>
	 * </ul>
	 * 
	 * @param secret
	 *            The password to be decrypted.
	 * @return a plain password.
	 * @throws Exception
	 *             a generic exception caused during the decrypt process.
	 */
	public static String decrypt(String secret) throws Exception {
		KeySpec keySpec = new PBEKeySpec(phraseword.toCharArray(), salt, INTERACTION);
 
		SecretKey key = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES)
				.generateSecret(keySpec);
		byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(secret);
		Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
				INTERACTION);   
		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		byte[] utf8 = dcipher.doFinal(dec);
		return new String(utf8, UTF8);
	}
 
	public static void main(String[] args) {
		try {
			String pass = "cwnsmanager1";
			String enc = Password.encrypt(pass);
			System.out.println(enc);
			String dec = Password.decrypt(enc);
			System.out.println(pass + "\nenc: " + enc + "\ndec: " + dec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

