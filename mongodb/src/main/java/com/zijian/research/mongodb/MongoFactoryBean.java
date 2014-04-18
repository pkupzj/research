package com.zijian.research.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MongoFactoryBean implements FactoryBean<MongoClient>, InitializingBean, DisposableBean, PersistenceExceptionTranslator {

	private MongoClient mongo;

	private MongoClientOptions mongoOptions;

	private String host;

	private Integer port;

	private WriteConcern writeConcern;

	private List<ServerAddress> replicaSetSeeds;

	private PersistenceExceptionTranslator exceptionTranslator;

	public void setMongoOptions(MongoClientOptions mongoOptions) {
		this.mongoOptions = mongoOptions;
	}

	public void setReplicaSetSeeds(ServerAddress[] replicaSetSeeds) {
		this.replicaSetSeeds = filterNonNullElementsAsList(replicaSetSeeds);
	}

	/**
	 * @param elements the elements to filter <T>
	 * @return a new unmodifiable {@link List#} from the given elements without
	 * nulls
	 */
	private <T> List<T> filterNonNullElementsAsList(T[] elements) {

		if (elements == null) {
			return Collections.emptyList();
		}

		List<T> candidateElements = new ArrayList<T>();

		for (T element : elements) {
			if (element != null) {
				candidateElements.add(element);
			}
		}
		return Collections.unmodifiableList(candidateElements);
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Sets the {@link com.mongodb.WriteConcern} to be configured for the {@link com.mongodb.Mongo}
	 * instance to be created.
	 * 
	 * @param writeConcern
	 */
	public void setWriteConcern(WriteConcern writeConcern) {
		this.writeConcern = writeConcern;
	}

	public void setExceptionTranslator(PersistenceExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	public MongoClient getObject() throws Exception {
		return mongo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<? extends MongoClient> getObjectType() {
		return MongoClient.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.dao.support.PersistenceExceptionTranslator#
	 * translateExceptionIfPossible(java.lang.RuntimeException)
	 */
	public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
		return exceptionTranslator.translateExceptionIfPossible(ex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {

		MongoClient mongo;
		ServerAddress defaultOptions = new ServerAddress();

		if (mongoOptions == null) {
			mongoOptions = new MongoClientOptions.Builder().build();
		}

		if (!isNullOrEmpty(replicaSetSeeds)) {
			mongo = new MongoClient(replicaSetSeeds, mongoOptions);
		}
		else {
			String mongoHost = StringUtils.hasText(host) ? host : defaultOptions.getHost();
			mongo = port != null ? new MongoClient(new ServerAddress(mongoHost, port), mongoOptions) : new MongoClient(
					mongoHost, mongoOptions);
		}

		if (writeConcern != null) {
			mongo.setWriteConcern(writeConcern);
		}

		this.mongo = mongo;

		if (this.exceptionTranslator == null) {
			this.exceptionTranslator = new MongoExceptionTranslator();
		}
	}

	private boolean isNullOrEmpty(Collection<?> elements) {
		return elements == null || elements.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	public void destroy() throws Exception {
		this.mongo.close();
	}
}
