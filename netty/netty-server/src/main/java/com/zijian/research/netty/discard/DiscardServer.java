package com.zijian.research.netty.discard;

import com.zijian.research.netty.Server;

public class DiscardServer {
	
	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new Server(port,new DiscardServerHandler()).run();
	}
}
