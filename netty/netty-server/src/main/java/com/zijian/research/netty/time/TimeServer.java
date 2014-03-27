package com.zijian.research.netty.time;

import com.zijian.research.netty.Server;

public class TimeServer {
	
	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new Server(port,new TimeServerHandler()).run();
	}
}
