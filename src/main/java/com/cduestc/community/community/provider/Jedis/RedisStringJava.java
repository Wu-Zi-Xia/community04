package com.cduestc.community.community.provider.Jedis;

import redis.clients.jedis.Jedis;

public class RedisStringJava {
    public static void main(String[] args) {
        //连接本地的redis服务
        Jedis jedis=new Jedis("localhost");
        System.out.println("服务正在运行:"+jedis.ping());
        jedis.set("cgx","123456789");
        System.out.println(jedis.get("cgx"));



    }






}
