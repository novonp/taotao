package com.taotao.service.jedis;

public interface JedisClient {
    //设置值 String字符串类型
    String set(String key, String value);
    //获取值 String字符串类型
    String get(String key);
    //判断 key是否存在
    Boolean exists(String key);
    //设置过期时间 (Ttl key 过期时间：Expire key second)
    Long expire(String key, int seconds);
    //设置时间 (设置key的有效期)
    Long ttl(String key);
    //自增
    Long incr(String key);
    //设置散列数据
    Long hset(String key, String field, String value);
    //获取 散列数据
    String hget(String key, String field);
    //删除 散列中的数据
    Long hdel(String key, String... field);
}
