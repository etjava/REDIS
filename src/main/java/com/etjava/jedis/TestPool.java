package com.etjava.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 测试Jedis的连接池
 * @author etjav
 *
 */
public class TestPool {

	public static void main(String[] args) {
		JedisPool pool1 = JedisPoolTest.instance();
//		JedisPool pool2 = JedisPoolTest.instance();
//		System.out.println(pool1==pool2);
		Jedis jedis = pool1.getResource();
		jedis.set("aa", "bb");
		System.out.println(jedis.get("aa"));
		
		JedisPoolTest.release(jedis);
	}
}
