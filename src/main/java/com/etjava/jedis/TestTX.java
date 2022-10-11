package com.etjava.jedis;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * redis中的事务
 * @author etjav
 *
 */
public class TestTX {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.199.125",6379);
		jedis.select(2);
//		testTransaction(jedis);
		
		// 初始化数据
//		jedis.set("balance","100");
//		jedis.set("debt", "0");
		testWatchTx(jedis);
	}
	
	// 加锁的事务控制 - watch
	private static void testWatchTx(Jedis jedis) {
		int balance; // 可用额度
		int debt; // 欠额
		int amtSubtract = 20; // 每次消费额度
		
		// 开启监控
		jedis.watch("balance","debt");
		// 模拟其它线程修改了balance数据
		jedis.set("balance","200");
		balance = Integer.valueOf(jedis.get("balance"));
		if(balance<amtSubtract) { // 可用额度不能小于每次消费额度
			jedis.unwatch();// 放弃监控
			System.out.println("可用额度不足。。。");
		}else {
			// 开启事务
			Transaction tx = jedis.multi();
			// 模拟操作
			tx.decrBy("balance", amtSubtract);
			tx.incrBy("debt", amtSubtract);
			// 提交事务
			tx.exec();
			/*
			 * 这里由于在开启对balance监控后又对其做了修改 因此监控会自动放弃 这里的balance视作普通修改数据 与事务和监控无关 
			 * debt没有做改动 但在监控和事务中 因此操作被回滚了 
			 */
			System.out.println(jedis.get("balance"));
			System.out.println(jedis.get("debt"));
		}
	}
	
	// 普通事务控制
	private static void testTransaction(Jedis jedis) {
		// 开启事务
		Transaction tx = jedis.multi();
		// 执行各种指令
		tx.set("k1","v1");
		tx.set("k2","v2");
		tx.mset("k2","v2","k3","v3");
		Response<List<String>> mget = tx.mget("k1","k2","k3");
		// 提交事务
		tx.exec();
		// 回滚事务  - 提交与回滚事务不能同时使用在同一个地方
//		tx.discard();
		System.out.println(mget.get());
	}
}
