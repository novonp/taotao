package com.taotao.content.jedis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

public class JedisClientCluster implements JedisClient{
    @Autowired
    private JedisCluster cluster;
    @Override
    public String set(String key, String value) {
        return cluster.set(key, value);
    }

    @Override
    public String get(String key) {
        return cluster.get(key);
    }

    @Override
    public Boolean exists(String key) {
        return cluster.exists(key);
    }

    @Override
    public Long expire(String key, int seconds) {
        return cluster.expire(key,seconds);
    }

    @Override
    public Long ttl(String key) {
        return cluster.ttl(key);
    }

    @Override
    public Long incr(String key) {
        return cluster.incr(key);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return cluster.hset(key, field, value);
    }

    @Override
    public String hget(String key, String field) {
        return cluster.hget(key, field);
    }

    @Override
    public Long hdel(String key, String... field) {
        return cluster.hdel(key, field);
    }
}
