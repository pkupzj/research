package com.zijian.research.mongodb.exception;


import org.springframework.dao.UncategorizedDataAccessException;

public class UncategorizedMongoDbException extends UncategorizedDataAccessException {

	private static final long serialVersionUID = -2336595514062364929L;

	public UncategorizedMongoDbException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
