package com.etjava.jedis;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.Tuple;

public class TestAPI {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.199.125",6379);
		String db = jedis.flushDB();
		System.out.println("flushdb "+db);
		
		testHash(jedis);
		// testZSet(jedis);
		//testSet(jedis);
		// testList(jedis);
		// testString(jedis);
	}
	
	// hsah
	private static void testHash(Jedis jedis) {
		// hset 添加到hash
		Map<String,String> map = new HashMap<>();
		map.put("id","1");
		map.put("name","tom");
		map.put("age","12");
		jedis.hset("h1", map);
		// hget 获取hash中的值
		String id = jedis.hget("h1", "id");
		String name = jedis.hget("h1", "name");
		System.out.println("hget - "+id);
		System.out.println("hget - "+name);
		// hmset 同时设置多个值 
		jedis.hmset("h2", map);
		// hmget 同时获取多个值
		List<String> hmget = jedis.hmget("h2", "id","name","age");
		System.out.println("hmget - "+hmget);
		
		// hgetall 获取全部元素key 和value
		Map<String, String> hgetAll = jedis.hgetAll("h2");
		System.out.println("getAll - "+hgetAll);
		
		// hdel 根据key删除hash中的元素
		Long hdel = jedis.hdel("h2","id");
		Map<String, String> hgetAll2 = jedis.hgetAll("h2");
		System.out.println("del getAll - "+hgetAll2);
		
		// hlen 获取hash中元素个数
		Long hlen = jedis.hlen("h2");
		System.out.println("len - "+hlen);
		
		// hexists 判断是否存在
		Boolean hexists = jedis.hexists("h2", "name");
		System.out.println("exists - "+hexists);
		// hkeys 获取全部的key
		System.out.println("all keys - "+jedis.hkeys("h2"));
		
		// hvals 获取全部的value
		System.out.println("all values - "+jedis.hvals("h2"));
		
		// hincrby 整数递增
		System.out.println("incrby - "+jedis.hincrBy("h2", "age", 1));
		
		// hincrbyfloat 小数递增
		System.out.println("incrbyfloat - "+jedis.hincrByFloat("h2", "age", 1.2));
		
		// hsetnx 不存在添加 存在舍弃
		jedis.hsetnx("h2", "email", "abc@us.com");
		System.out.println("all key - "+jedis.hkeys("h2"));
	}
	
	
	// zset
	private static void testZSet(Jedis jedis) {
		// zadd 添加元素到  zadd(key,score,value)
		for(int i=0;i<100;i++) {
			jedis.zadd("k1", (int)(Math.random()*99),i+"a");
		}
		
		// zrange 遍历
		Set<String> zrange = jedis.zrange("k1", 0, -1);
		System.out.println("zrange - "+zrange);
		
		// zrangebyscore 根据score遍历set
		Set<String> byScore = jedis.zrangeByScore("k1", 10, 50);
		System.out.println("zrangebyscore - "+byScore);

		// zrem 根据value删除
		Long zrem = jedis.zrem("k1", "1a","2a");// remove 2
		System.out.println("zrem - "+zrem);// return remove result total
		
		// zcard 获取元素个数
		System.out.println("count - "+jedis.zcard("k1")); // remaining 98
		
		// zcount 根据score统计元素个数
		System.out.println("count byscore - "+jedis.zcount("k1", 10, 15));
		
		// zrevrank 获取元素下标
		System.out.println("zrevrank index - "+jedis.zrevrank("k1", "27a"));
		
		// zscore 根据value获取score
		System.out.println("zscore - "+jedis.zscore("k1", "21a"));
		
		// zrevrange 倒序遍历
		System.out.println("zrevrange - "+jedis.zrevrange("k1", 10, 20));
		
		// zrevrangebyscore 根据score倒序遍历
		System.out.println("zrevrangebyscore - "+jedis.zrevrangeByScore("k1", 20, 10));
		// withScore 带有score的集合数据
		Set<Tuple> zrangeWithScores = jedis.zrangeWithScores("k1", 10, 20);
		System.out.println("zrangeWithScores - "+zrangeWithScores);
	}
	
	// set
	private static void testSet(Jedis jedis) {
		// sadd 添加元素
		Long res = jedis.sadd("s1", "a","b","c","1","2","3");
		System.out.println("sadd - "+res);
		// smembers 遍历元素
		Set<String> set = jedis.smembers("s1");
		System.out.println("smambers - "+set);
		
		// sismember 判断是否存在某个元素
		Boolean f = jedis.sismember("s1", "10");
		System.out.println("sismember - "+f);
		
		// scard 获取元素个数
		System.out.println("scard - "+jedis.scard("s1"));
		
		// srem 删除指定的元素
		Long rem = jedis.srem("s1", "a");
		System.out.println("srem - "+rem);
		System.out.println("smembers "+jedis.smembers("s1"));
		
		// srandmember 随机抽取元素
		String srandmember = jedis.srandmember("s1");
		System.out.println("single srandmember - "+srandmember);
		List<String> list = jedis.srandmember("s1", 3);
		System.out.println("many srandmember - "+list);
		
		for(int i=0;i<10;i++) {
			jedis.sadd("s1", i+"");
		}
		// spop 随机弹出元素
		String spop = jedis.spop("s1");
		System.out.println("single spop - "+spop);
		Set<String> spop2 = jedis.spop("s1",5);
		System.out.println("many spop - "+spop2);
		
		// smove 移动元素到另个set集合
		Long smove = jedis.smove("s1", "s2", "4");
		System.out.println("smove - "+smove);
		System.out.println("smembers - "+jedis.smembers("s2"));// random
		
		jedis.sadd("k1", "1","2","3","4","5");//A
		jedis.sadd("k2", "6","7","8","1","2");
		// sdiff 差集 - A中的数据在B中没有的部分
		Set<String> sdiff = jedis.sdiff("k1","k2");
		System.out.println("sdiff - "+sdiff);
		
		// sinter 交集 - A和B共有部分
		Set<String> sinter = jedis.sinter("k1","k2");
		System.out.println("inner - "+sinter);
		
		// sunion 全集 - A+B的数据 会去重
		Set<String> sunion = jedis.sunion("k1","k2");
		System.out.println("union - "+sunion);
	}
	
	// list类型
	private static void testList(Jedis jedis) {
		// lpush 
		jedis.lpush("lst1", "1","2","3","4","5");
		// lrange 遍历list 0到-1表示全部获取
		List<String> lst1 = jedis.lrange("lst1", 0, 2);// 5 4 3
		System.out.println(lst1);
		
		// rpush
		jedis.rpush("lst2", "1","2","3","4","5");
		List<String> lst2 = jedis.lrange("lst2", 0, 2);// 1 2 3
		System.out.println(lst2);
		
		// lpop 弹出左侧第一个位置的元素
		String lpop = jedis.lpop("lst2");
		System.out.println("lpop弹出的元素 "+lpop);
		
		// rpop 弹出右侧第一个位置的元素
		String rpop = jedis.rpop("lst2");
		System.out.println("rpop弹出的元素 "+rpop);
		
		// lindex 获取下标对应的元素
		String lindex = jedis.lindex("lst2", 1);
		System.out.println(lindex);
		
		// llen 获取key中元素的个数
		System.out.println("llen - "+jedis.llen("lst2"));
		
		// lrem 指定删除多少个元素
		Long lrem = jedis.lrem("lst2", 2, "1");// 删除两个1
		System.out.println(lrem);
		
		// ltrim 截取一部分覆盖到当前list中
		String newList = jedis.ltrim("lst2", 0, 2);
		System.out.println(newList);
		
		System.out.println("lst2 - "+jedis.lrange("lst2", 0, -1));
		// rpoplpush 移动一个元素到另个list
		String rpoplpush = jedis.rpoplpush("lst2", "lst1");
		System.out.println("rpoplpush "+rpoplpush);
		System.out.println("lst1 - "+jedis.lrange("lst1", 0, -1));
		
		// lset 修改指定下标的值
		System.out.println("lst2 - "+jedis.lrange("lst2", 0, -1));
		String lset = jedis.lset("lst2", 0, "a");
		System.out.println(lset);
		System.out.println("lst2 - "+jedis.lrange("lst2", 0, -1));
		
		// linsert 插入值
		System.out.println("lst2 - "+jedis.lrange("lst2", 0, -1));
		Long linsert = jedis.linsert("lst2", ListPosition.BEFORE, "a", "java");
		System.out.println(linsert);
		System.out.println("lst2 - "+jedis.lrange("lst2", 0, -1));
	}
	
	// string类型
	private static void testString(Jedis jedis) {
		// set get
		jedis.set("key1", "val1");
		jedis.set("key2", "val2");
		System.out.println(jedis.get("key1"));
		
		// mset mget
		jedis.mset("s1","a","s2","b","s3","c");
		List<String> list = jedis.mget("s1","s2","s3");
		System.out.println(list.size());
		
		// append
		jedis.append("s3", "a");
		System.out.println(jedis.mget("s1","s2","s3"));
		
		// del
		jedis.del("s3");
		System.out.println(jedis.get("s3"));
		
		// strlen 
		Long len = jedis.strlen("key1");
		System.out.println(len);
		
		// incr  decr
		jedis.set("n","1");
		Long incr = jedis.incr("n");
		System.out.println(incr);
		Long decr = jedis.decr("n");
		System.out.println(decr);
		
		// incrby decrby
		Long incrBy = jedis.incrBy("n", 20);
		System.out.println(incrBy);
		Long decrBy = jedis.decrBy("n",10);
		System.out.println(decrBy);
		
		// getrange 
		String getrange = jedis.getrange("key1", 0, -1);
		System.out.println(getrange);
		
		// setrange 添加元素到指定下标的位置
		jedis.setrange("key1", 2, "DDD");
		System.out.println(jedis.get("key1"));
		
		// setex 指定过期时间 以秒为单位
		jedis.setex("k5", 1, "v5");
		System.out.println(jedis.get("k5"));
		try {
			Thread.sleep(2000);
			System.out.println(jedis.get("k5"));
		} catch (Exception e) {
		}
		
		// setnx 不存在就添加
		jedis.setnx("k5", "v5");
		jedis.setnx("k5", "v6");
		System.out.println(jedis.get("k5"));
		
		// msetnx 
		jedis.msetnx("m1","v1","m2","v2");
		System.out.println(jedis.get("m2"));
		System.out.println(jedis.mget("m1","m2"));
	}
}
