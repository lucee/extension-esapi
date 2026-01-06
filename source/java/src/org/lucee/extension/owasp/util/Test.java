package org.lucee.extension.owasp.util;

public class Test {

	public static void main(String[] args) {
		String exceptionMessage = "Database connection failed with user 'admin' and password 'secretPassword123'.";

		// Custom method to mask sensitive data
		String obfuscatedMessage = maskSensitiveData(exceptionMessage);

		System.out.println("Original Message: " + exceptionMessage);
		System.out.println("Obfuscated Message: " + obfuscatedMessage);
	}

	public static String maskSensitiveData(String message) {
		// Simple regex to find and mask passwords
		return message.replaceAll("password '[^']+'", "password '***'");
	}
}