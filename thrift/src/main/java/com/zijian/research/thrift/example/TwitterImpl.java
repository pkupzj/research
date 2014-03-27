package com.zijian.research.thrift.example;

import java.util.ArrayList;

import org.apache.thrift.TException;

public class TwitterImpl implements Twitter.Iface {

	@Override
	public void ping() throws TException {
		System.out.println("twitter server : invoking method ping()");
	}

	@Override
	public boolean postTweet(Tweet tweet) throws TwitterUnavailable, TException {
		System.out.println("twitter server : invoking method postTweet(Tweet tweet)");
		System.out.println("tweet.userName = " + tweet.userName);
		return false;
	}

	@Override
	public TweetSearchResult searchTweets(String query) throws TException {
		System.out.println("twitter server : invoking method searchTweets(String query)");
		TweetSearchResult res = new TweetSearchResult();
		res.tweets = new ArrayList<Tweet>();
		return res;
	}

	@Override
	public void zip() throws TException {
		System.out.println("twitter server : invoking method zip()");
	}

}
