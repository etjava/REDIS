package com.etjava.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 封装jedis连接池
 * @author etjav
 *
 */
public class JedisPoolTest {

	private JedisPoolTest() {}
	
	// volatile 关键字保证可见性 但不保证原子性
	private static JedisPool jedisPoll = null;
	
	// 单例模式创建jedis连接池
	public static JedisPool instance() {
		if(jedisPoll==null) {
			synchronized (JedisPoolTest.class) {
				if(jedisPoll==null) {
					// jedis连接池的配置
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(1000);// 最大连接数
					config.setMaxIdle(32); // 最大空闲
					config.setMaxWaitMillis(3000);// 最大等待时间
					jedisPoll = new JedisPool(config,"192.168.199.125",6379);
				}
			}
		}
		
		return jedisPoll;
	}
	
	// 用完连接后在放回连接池中
	/*
	 * jedis2.9以上的版本的close方法把  jedispool.returnBrokenResource();jedispool.returnResource()包括了，
	 * 所以直接jedis.close 就可以释放连接了,
	 * 但如果你在使用redis的时侯，想要close，就不能释放，所以你先要退出client，
	 * 就要用jedis.quit(),这样就完事了
		public void close(jedis){
			jedis.quit();
			jedis.disconnect();
		}
	 */
	public static void release(Jedis jedis) {
		if(jedis!=null) {
			jedis.quit();
			jedis.disconnect();
			jedis.close();
		}
	}
	
}
