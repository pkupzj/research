package com.zijian.research.netty.echo;

import com.zijian.research.netty.Server;

public class EchoServer {
	
	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new Server(port,new EchoServerHandler()).run();
	}
}
