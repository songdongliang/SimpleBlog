package com.lvwang.osf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

    @Autowired(required = false)
    private ShardedJedisPool shardedJedisPool;

    private <T> T execute(Function<T,ShardedJedis> function) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return function.callBack(shardedJedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行SET操作
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value){
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callBack(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
            }
        });
    }

    /**
     * 执行GET操作
     *
     * @param key
     * @return
     */
    public String get(final String key){
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callBack(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public Long del(final String key){
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
            }
        });
    }

    /**
     * 设置生存时间，单位为：秒
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(final String key, final Integer seconds) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, seconds);
            }
        });
    }

    /**
     * 设置String类型的值，并且指定生存时间，单位为：秒
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callBack(ShardedJedis e) {
                String result = e.set(key, value);
                // 设置生存时间
                e.expire(key, seconds);
                return result;
            }
        });
    }

    /**
     * 是否包含某个键
     * @param key
     * @return
     */
    public boolean containsKey(final String key) {
        return this.execute(new Function<Boolean, ShardedJedis>() {
            @Override
            public Boolean callBack(ShardedJedis shardedJedis) {
                return shardedJedis.exists(key);
            }
        });
    }

    public Long set(final String hKey, final String field, final String value) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.hset(hKey, field,value);
            }
        });
    }

    public String get(final String hKey, final String field) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callBack(ShardedJedis shardedJedis) {
                return shardedJedis.hget(hKey, field);
            }
        });
    }
}