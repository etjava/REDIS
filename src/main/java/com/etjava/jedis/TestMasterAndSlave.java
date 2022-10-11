package com.etjava.jedis;

import redis.clients.jedis.Jedis;

/**
 * 测试主从复制
 * 通常不会在Java程序中使用主从复制(直接在系统中配置好主从)
 * @author etjav
 *
 */
public class TestMasterAndSlave {

	public static void main(String[] args) {
		Jedis master = new Jedis("192.168.199.125",6379);
		Jedis slave = new Jedis("192.168.199.125",6380);
		
		// 配置主从
		slave.slaveof("192.168.199.125",6379);
		
		// 主机负责写操作
		master.set("k1","v1");
		master.set("k2","v2");
		// 从机负责读操作 - 如果需要从机写 需要在配置文件中将replica-read-only 设置为no 但不推荐
		String res = slave.get("k1");
		System.out.println(res);
	}
}
