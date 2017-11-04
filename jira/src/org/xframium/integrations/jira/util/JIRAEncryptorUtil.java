package org.xframium.integrations.jira.util;

import java.io.ByteArrayInputStream;

import com.xframium.serialization.SerializationManager;
/**
 * To encrypt and decrypt String values
 * 
 * 
 *
 */
public class JIRAEncryptorUtil {
/**
 * Used to decrypt the String value
 * @param currentValue
 * @return decrypted String
 */

	public static String decryptValue(String currentValue) {

		try {
			String fullValue = new StringBuilder(currentValue).reverse().toString();
			String version = fullValue.substring(0, 2);
			String actualValue = fullValue.substring(2);

			if (version.equals("01")) {
				ByteArrayInputStream inputStream = new ByteArrayInputStream(actualValue.getBytes());
				return (String) SerializationManager.instance().readData(inputStream);
			} else {
				return currentValue;
			}
		} catch (Exception e) {
			return currentValue;
		}
	}
/**
 * Used to encrypt a particular String
 * @param currentValue
 * @return encrypted String
 */
	public static String encryptValue(String currentValue) {
		return new StringBuilder(
				new String(
						SerializationManager.instance()
								.toByteArray(SerializationManager.instance()
										.getAdapter(SerializationManager.EXTENDED_SERIALIZATION), currentValue, 9)))
												.insert(0, "01").reverse().toString();
		
	}
	

}
