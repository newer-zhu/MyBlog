package com.zhuhodor.myblog.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

//@Component
//public class RedisCache<k,v> implements Cache<k,v> {
//    private String cacheName;
//    public RedisCache(){}
//
//    public RedisCache(String cacheName){
//        this.cacheName = cacheName;
//    }
//
//    @Autowired
//    RedisTemplate redisTemplate ;
//
//    @Override
//    public v get(k k) throws CacheException {
//        setSerializer();
//        return (v)redisTemplate.opsForHash().get(this.cacheName,k.toString());
//    }
//
//    @Override
//    public v put(k k, v v) throws CacheException {
//        setSerializer();
//        redisTemplate.opsForHash().put(this.cacheName,k.toString(),v);
//        return null;
//    }
//
//    @Override
//    public v remove(k k) throws CacheException {
//        return (v) redisTemplate.opsForHash().delete(this.cacheName,k.toString());
//    }
//
//    @Override
//    public void clear() throws CacheException {
//        redisTemplate.execute(new RedisCallback() {
//            @Override
//            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                redisConnection.flushDb();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public int size() {
//        return redisTemplate.opsForHash().size(this.cacheName).intValue();
//    }
//
//    @Override
//    public Set<k> keys() {
//        return redisTemplate.opsForHash().keys(this.cacheName);
//    }
//
//    @Override
//    public Collection<v> values() {
//        return redisTemplate.opsForHash().values(this.cacheName);
//    }
//
//    private void setSerializer(){
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//    }
//}
