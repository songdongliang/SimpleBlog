package com.lvwang.osf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;
import java.util.Set;

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
    public String hset(final String key, final String value){
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
    public String hget(final String key){
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
    public String hset(final String key, final String value, final Integer seconds) {
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

    public Long hset(final String hKey, final String field, final String value) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.hset(hKey, field,value);
            }
        });
    }

    public String hget(final String hKey, final String field) {
        return this.execute(new Function<String, ShardedJedis>() {
            @Override
            public String callBack(ShardedJedis shardedJedis) {
                return shardedJedis.hget(hKey, field);
            }
        });
    }

    public Long sadd(final String key, final String...members) {
        return this.execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.sadd(key, members);
            }
        });
    }

    public Set<String> smembers(final String key) {
        return this.execute(new Function<Set<String>, ShardedJedis>() {
            @Override
            public Set<String> callBack(ShardedJedis shardedJedis) {
                return shardedJedis.smembers(key);
            }
        });
    }

    public Boolean sismember(final String key, final String member) {
        return this.execute(new Function<Boolean, ShardedJedis>() {
            @Override
            public Boolean callBack(ShardedJedis shardedJedis) {
                return shardedJedis.sismember(key, member);
            }
        });
    }

    public Long srem(final String key, final String...members) {
        return execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.srem(key,members);
            }
        });
    }

    public Long strlen(final String key) {
        return execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.strlen(key);
            }
        });
    }

    public Set<String> hkeys(final String key) {
        return execute(new Function<Set<String>, ShardedJedis>() {
            @Override
            public Set<String> callBack(ShardedJedis shardedJedis) {
                return shardedJedis.hkeys(key);
            }
        });
    }

    public Long lpush(final String key, final String...values) {
        return execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.lpush(key, values);
            }
        });
    }

    public List<String> lrange(final String key, final int start, final int end) {
        return execute(new Function<List<String>, ShardedJedis>() {
            @Override
            public List<String> callBack(ShardedJedis shardedJedis) {
                return shardedJedis.lrange(key,start,end);
            }
        });
    }

    public Long llen(final String key) {
        return execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.llen(key);
            }
        });
    }

    public Long lrem(final String key, final int count, final String value) {
        return execute(new Function<Long, ShardedJedis>() {
            @Override
            public Long callBack(ShardedJedis shardedJedis) {
                return shardedJedis.lrem(key,count,value);
            }
        });
    }
}