package com.zijian.research.mongodb.morphia;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class DatastoreFactoryBean extends AbstractFactoryBean {

	private Morphia morphia;

	private MongoClient mongo;

	private String dbName; // 数据库名

	private boolean toEnsureIndexes = false; // 是否确认索引存在，默认false

	private boolean toEnsureCaps = false; // 是否确认caps存在，默认false

	@Override
	protected Datastore createInstance() throws Exception {
		Datastore ds = morphia.createDatastore(mongo, dbName);
		if (toEnsureIndexes) {
			ds.ensureIndexes();
		}
		if (toEnsureCaps) {
			ds.ensureCaps();
		}
		return ds;
	}

	@Override
	public Class<?> getObjectType() {
		return Datastore.class;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (mongo == null) {
			throw new IllegalStateException("mongo is not set");
		}
		if (morphia == null) {
			throw new IllegalStateException("morphia is not set");
		}
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	public void setMongo(MongoClient mongo) {
		this.mongo = mongo;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setToEnsureIndexes(boolean toEnsureIndexes) {
		this.toEnsureIndexes = toEnsureIndexes;
	}

	public void setToEnsureCaps(boolean toEnsureCaps) {
		this.toEnsureCaps = toEnsureCaps;
	}
}
