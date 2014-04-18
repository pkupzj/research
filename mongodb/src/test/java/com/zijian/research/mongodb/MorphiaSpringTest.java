package com.zijian.research.mongodb;

import com.zijian.research.mongodb.model.Address;
import com.zijian.research.mongodb.model.Hotel;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MorphiaSpringTest {

	final static public Logger LOG = LoggerFactory.getLogger(MorphiaSpringTest.class);

	@Autowired
	HotelDAO hotelDao;

	private List<Hotel> createHotelList(int num) {
		List<Hotel> list = new ArrayList<Hotel>();
		for (int i = 0; i < num; i++) {
			Hotel hotel = new Hotel();
			hotel.setName("编号为[" + i + "]的旅店");
			hotel.setStars(i % 10);
			Address address = new Address();
			address.setCountry("中国");
			address.setCity("北京");
			address.setStreet("上帝南路");
			address.setPostCode("10000" + (i % 10));
			hotel.setAddress(address);
			list.add(hotel);
		}
		return list;
	}

	@Test
	public void saveTest() {
		List<Hotel> hotelList = this.createHotelList(10);
		for (Hotel hotel : hotelList) {
			Key<Hotel> key = hotelDao.save(hotel);
			LOG.info("id为[" + key.getId() + "]的记录已被插入");
		}
	}

	/**
	 * 更新操作测试
	 * @throws Exception
	 */
	@Test
	public void updateTest() throws Exception {
		// 生成查询条件
		Query<Hotel> q = hotelDao.createQuery().field("stars").greaterThanOrEq(9);
		// 生成更新操作
		UpdateOperations<Hotel> ops = hotelDao.createUpdateOperations().set("address.city", "shanghai").inc("stars");
		// UpdateResults<Hotel> ur=hotelDao.update(q, ops);
		UpdateResults<Hotel> ur = hotelDao.updateFirst(q, ops);
		if (ur.getHadError()) {
			LOG.error(ur.getError());
			throw new Exception("更新时发生错误");
		}
		if (ur.getUpdatedExisting()) {
			LOG.info("更新成功，更新条数为[" + ur.getUpdatedCount() + "]，插入条数为[" + ur.getInsertedCount() + "]");
		}
		else {
			System.out.println("没有记录符合更新条件");
		}
	}

	/**
	 * 删除操作测试
	 * <p>
	 * @Title: deleteTest
	 * </p>
	 */
	@Test
	public void deleteTest() {
		ObjectId id = hotelDao.findIds().get(0);
		hotelDao.deleteById(id);
		Query<Hotel> q = hotelDao.createQuery().field("stars").greaterThanOrEq(8);
		hotelDao.deleteByQuery(q);
	}

	/**
	 * 查询测试
	 * <p>
	 * @Title: queryHotel
	 * </p>
	 */
	@Test
	public void queryHotel() {
		// 显示所有记录
		System.out.println("\nhotelDao.find()=");
		for (Hotel hotel : hotelDao.find()) {
			LOG.info(hotel.toString());
		}

		// 统计star大于等于9的数目
		LOG.info("\nhotelDao.count(hotelDao.createQuery().field(\"stars\").greaterThanOrEq(9))="
				+ hotelDao.count(hotelDao.createQuery().field("stars").greaterThanOrEq(9)));

		// 显示符合条件的记录ID
		List<ObjectId> ids = hotelDao.findIds("stars", 8);
		LOG.info("\nhotelDao.findIds(\"stars\", 8)=");
		for (ObjectId id : ids) {
			System.out.println(id);
		}
	}
}
