package com.zijian.research.thrift.demo;

import org.apache.thrift.TException;

public class IndexNewsOperatorServicesImpl implements IndexNewsOperatorServices.Iface {

	@Override
	public boolean indexNews(NewsModel indexNews) throws TException {
		System.out.println("this is a thrift server, invoking IndexNewsOperatorServices.indexNews interface");
		System.out.println("indexNews title is " + indexNews.title);
		return false;
	}

	@Override
	public boolean deleteArtificiallyNews(int id) throws TException {
		System.out
				.println("this is a thrift server, invoking IndexNewsOperatorServices.deleteArtificiallyNews interface");
		return false;
	}

}
