package com.zijian.research.mongodb.exception;

import com.zijian.research.mongodb.UserCredentials;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * Exception being thrown in case we cannot connect to a MongoDB instance.
 * 
 * @author Oliver Gierke
 */
public class CannotGetMongoDbConnectionException extends DataAccessResourceFailureException {

	private final UserCredentials credentials;

	private final String database;

	private static final long serialVersionUID = 1172099106475265589L;

	public CannotGetMongoDbConnectionException(String msg, Throwable cause) {
		super(msg, cause);
		this.database = null;
		this.credentials = UserCredentials.NO_CREDENTIALS;
	}

	public CannotGetMongoDbConnectionException(String msg) {
		this(msg, null, UserCredentials.NO_CREDENTIALS);
	}

	public CannotGetMongoDbConnectionException(String msg, String database, UserCredentials credentials) {
		super(msg);
		this.database = database;
		this.credentials = credentials;
	}

	/**
	 * Returns the {@link UserCredentials} that were used when trying to connect
	 * to the MongoDB instance.
	 * 
	 * @return
	 */
	public UserCredentials getCredentials() {
		return this.credentials;
	}

	/**
	 * Returns the name of the database trying to be accessed.
	 * 
	 * @return
	 */
	public String getDatabase() {
		return database;
	}
}
