package com.zijian.research.mongodb;

import com.mongodb.MongoClient;
import com.zijian.research.mongodb.model.MyEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MorphiaTest {

	final static public Logger LOG = LoggerFactory.getLogger(MorphiaTest.class);

	@Autowired
	@Qualifier("mongo")
    MongoClient mongo;

	@Test
	public void test() {
		Datastore ds = new Morphia().createDatastore(mongo, "myDB");//
		// ds.ensureIndexes();
		// ds.ensureCaps();
		MyEntity e = new MyEntity("jerry");
		ds.save(e);
		ds.save(new MyEntity("mary"));
		MyEntity e2 = ds.find(MyEntity.class).get();
		LOG.info(e2.toString());
		MyEntity e3 = ds.find(MyEntity.class).field("name").equal("mary").get();
		LOG.info(e3.toString());
	}
}
