package com.zijian.research.mongodb;

import com.zijian.research.mongodb.model.Hotel;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class HotelDAO extends BasicDAO<Hotel, ObjectId> {

	protected HotelDAO(Datastore ds) {
		super(ds);
	}
}
