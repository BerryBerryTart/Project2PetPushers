package com.pets.test.util;

import java.util.Base64;

public class Base64Conversion {
	// helper method for creating base 64 byte arrays
	public static byte[] base64toByteArray(String base64String) {
		byte[] arr = null;

		arr = Base64.getDecoder().decode(base64String.getBytes());

		return arr;
	}
}
