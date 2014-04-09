package com.zijian.research.mongodb;

import com.mongodb.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Created by zijian on 4/9/14.
 */
public class JavaDriver {

    public MongoClient mongoClient = null;
    public DB db = null;

    @Before
    public void before() throws Exception{
        //        MongoClient mongoClient = new MongoClient();

//        MongoClient mongoClient = new MongoClient( "10.2.54.166" );

//        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
        //       MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
        //               new ServerAddress("localhost", 27018),
        //               new ServerAddress("localhost", 27019)));


        mongoClient = new MongoClient("10.2.54.166");
        db = mongoClient.getDB( "mydb" );
    }

    @Test
    public void testGetCollectionNames(){
        Set<String> colls = db.getCollectionNames();
        for (String s : colls) {
            System.out.println(s);
        }
    }

    @Test
    public void testCRUD(){
        DBCollection coll = db.getCollection("testData");
        BasicDBObject doc = new BasicDBObject("name", "MongoDB").
                append("type", "database").
                append("count", 1).
                append("info", new BasicDBObject("x", 203).append("y", 102));
        coll.insert(doc);
        for (int i=0; i < 100; i++) {
            coll.insert(new BasicDBObject("i", i));
        }
        DBObject myDoc = coll.findOne();
        System.out.println(myDoc);
        System.out.println(coll.getCount());
        DBCursor cursor = coll.find();
        printCurosr(cursor);
        BasicDBObject query = new BasicDBObject("j", new BasicDBObject("$ne", 3)).
                append("k", new BasicDBObject("$gt", 10));
        cursor = coll.find(query);
        printCurosr(cursor);
        coll.drop();
    }

    public void printCurosr(DBCursor cursor){
        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }
}
