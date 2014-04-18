package com.zijian.research.mongodb.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class MyEntity {
	@Id
    ObjectId id;

	String name;

	public MyEntity() {
	}

	public MyEntity(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("[id:%s,name:%s]", this.id, this.name);
	}

}