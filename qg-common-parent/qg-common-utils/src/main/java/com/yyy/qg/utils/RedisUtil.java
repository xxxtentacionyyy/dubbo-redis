package com.yyy.qg.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Resource(name = "redisTemplate")
    ValueOperations<Object, Object> valOpsObj;

    /**
     * 根据指定key获取String
     * @param key
     * @return
     */
    public String getStr(String key){
        return valOpsStr.get(key);
    }

    /**
     * 设置Str缓存
     * @param key
     * @param val
     */
    public void setStr(String key, String val){
        valOpsStr.set(key,val);
    }

    /**
     * 设置限时缓存
     * @param key
     * @param val
     * @param timeOut
     */
    public void setStr(String key, String val , Long timeOut){
        valOpsStr.set(key,val,timeOut,TimeUnit.MINUTES);
    }

    /**
     * 删除指定key
     * @param key
     */
    public void del(String key){
        stringRedisTemplate.delete(key);
    }

    public List<String> getKeys(String patternKey){
        ScanOptions options = ScanOptions.scanOptions().count(10000).match(patternKey+"*").build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>)redisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()){
            result.add(cursor.next().toString());
        }
        try {
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据指定o获取Object
     * @param o
     * @return
     */
    public Object getObj(Object o){
        return valOpsObj.get(o);
    }

    /**
     * 设置obj缓存
     * @param o1
     * @param o2
     */
    public void setObj(Object o1, Object o2){
        valOpsObj.set(o1, o2);
    }


    /**
     * 删除Obj缓存
     * @param o
     */
    public void delObj(Object o){
        redisTemplate.delete(o);
    }

    /***
     * 加锁的方法
     * @return
     */
    public boolean lock(String key,Long expire){
        RedisConnection redisConnection=redisTemplate.getConnectionFactory().getConnection();
//设置序列化方法
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        if(redisConnection.setNX(key.getBytes(),new byte[]{1})){
            redisTemplate.expire(key,expire, TimeUnit.SECONDS);
            redisConnection.close();
            return true;
        }else{
            redisConnection.close();
            return false;
        }
    }
    /***
     * 解锁的方法
     * @param key
     */
    public void unLock(String key){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.delete(key);
    }

}
