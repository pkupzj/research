package com.zijian.research.mongodb;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class UserCredentials {
	public static final UserCredentials NO_CREDENTIALS = new UserCredentials(null, null);

	private final String username;

	private final String password;

	/**
	 * Creates a new {@link UserCredentials} instance from the given username
	 * and password. Empty {@link String}s provided will be treated like no
	 * username or password set.
	 * 
	 * @param username
	 * @param password
	 */
	public UserCredentials(String username, String password) {
		this.username = StringUtils.hasText(username) ? username : null;
		this.password = StringUtils.hasText(password) ? password : null;
	}

	/**
	 * Get the username to use for authentication.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get the password to use for authentication.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns whether the credentials contain a username.
	 * 
	 * @return
	 */
	public boolean hasUsername() {
		return this.username != null;
	}

	/**
	 * Returns whether the credentials contain a password.
	 * 
	 * @return
	 */
	public boolean hasPassword() {
		return this.password != null;
	}

	/**
	 * Returns the password in obfuscated fashion which means everthing except
	 * the first and last character replaced by stars. If the password is one or
	 * two characters long we'll obfuscate it entirely.
	 * 
	 * @return the obfuscated password
	 */
	public String getObfuscatedPassword() {

		if (!hasPassword()) {
			return null;
		}

		StringBuilder builder = new StringBuilder();

		if (password.length() < 3) {

			for (int i = password.length(); i != 0; i--) {
				builder.append("*");
			}

			return builder.toString();
		}

		builder.append(password.charAt(0));

		for (int i = password.length() - 2; i != 0; i--) {
			builder.append("*");
		}

		return builder.append(password.substring(password.length() - 1)).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("username = [%s], password = [%s]", username, getObfuscatedPassword());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}

		UserCredentials that = (UserCredentials) obj;

		return ObjectUtils.nullSafeEquals(this.username, that.username)
				&& ObjectUtils.nullSafeEquals(this.password, that.password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;

		result += 31 * ObjectUtils.nullSafeHashCode(username);
		result += 31 * ObjectUtils.nullSafeHashCode(password);

		return result;
	}
}
