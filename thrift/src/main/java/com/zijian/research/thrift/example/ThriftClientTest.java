package com.zijian.research.thrift.example;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class ThriftClientTest {
	/**
	 * @param args
	 * @throws TException
	 */
	public static void main(String[] args) throws TException {
		TTransport transport = new TSocket("127.0.0.1", 9813);
		long start = System.currentTimeMillis();
		TProtocol protocol = new TBinaryProtocol(transport);
		Twitter.Client client = new Twitter.Client(protocol);
		transport.open();

		client.ping();
		Tweet tweet = new Tweet();
		tweet.userName = "userName";
		tweet.userId = 100;
		tweet.text = "text";
		tweet.loc = new Location(1, 2);
		client.postTweet(tweet);
		client.searchTweets("query");
		client.zip();
		transport.close();
		System.out.println((System.currentTimeMillis() - start));
		System.out.println("client sucess!");
	}
}
