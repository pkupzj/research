package com.zijian.research.netty.time;

import com.zijian.research.netty.Client;

public class TimeClient {

	public static void main(String[] args) throws Exception {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		new Client(host,port,new TimeClientHandler()).run();
	}
}
