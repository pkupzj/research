package com.zijian.research.mongodb;

import com.mongodb.MongoException;
import com.mongodb.MongoException.CursorNotFound;
import com.mongodb.MongoException.Network;
import com.mongodb.MongoInternalException;
import com.zijian.research.mongodb.exception.UncategorizedMongoDbException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.dao.support.PersistenceExceptionTranslator;

/**
 * Simple {@link PersistenceExceptionTranslator} for Mongo. Convert the given
 * runtime exception to an appropriate exception from the
 * {@code org.springframework.dao} hierarchy. Return {@literal null} if no
 * translation is appropriate: any other exception may have resulted from user
 * code, and should not be translated.
 * 
 * @author Oliver Gierke
 * @author Michal Vich
 */
public class MongoExceptionTranslator implements PersistenceExceptionTranslator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.dao.support.PersistenceExceptionTranslator#
	 * translateExceptionIfPossible(java.lang.RuntimeException)
	 */
	public DataAccessException translateExceptionIfPossible(RuntimeException ex) {

		// Check for well-known MongoException subclasses.

		// All other MongoExceptions
		// if (ex instanceof DuplicateKey) {
		// return new DuplicateKeyException(ex.getMessage(), ex);
		// }

		if (ex instanceof Network) {
			return new DataAccessResourceFailureException(ex.getMessage(), ex);
		}

		if (ex instanceof CursorNotFound) {
			return new DataAccessResourceFailureException(ex.getMessage(), ex);
		}

		// Driver 2.12 throws this to indicate connection problems. String
		// comparison to avoid hard dependency
		if (ex.getClass().getName().equals("com.mongodb.MongoServerSelectionException")) {
			return new DataAccessResourceFailureException(ex.getMessage(), ex);
		}

		if (ex instanceof MongoInternalException) {
			return new InvalidDataAccessResourceUsageException(ex.getMessage(), ex);
		}

		if (ex instanceof MongoException) {

			int code = ((MongoException) ex).getCode();

			// if (code == 11000 || code == 11001) {
			// throw new DuplicateKeyException(ex.getMessage(), ex);
			// }
			if (code == 12000 || code == 13440) {
				throw new DataAccessResourceFailureException(ex.getMessage(), ex);
			}
			else if (code == 10003 || code == 12001 || code == 12010 || code == 12011 || code == 12012) {
				throw new InvalidDataAccessApiUsageException(ex.getMessage(), ex);
			}
			return new UncategorizedMongoDbException(ex.getMessage(), ex);
		}

		// If we get here, we have an exception that resulted from user code,
		// rather than the persistence provider, so we return null to indicate
		// that translation should not occur.
		return null;
	}
}
