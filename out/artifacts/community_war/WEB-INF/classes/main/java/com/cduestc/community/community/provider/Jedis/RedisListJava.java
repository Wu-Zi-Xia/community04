package com.cduestc.community.community.provider.Jedis;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
public class RedisListJava {
    Jedis jedis=new Jedis("localhost");

    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost");
        jedis.lpush("lcgx","redis");
        jedis.lpush("lcgx","mongodb");
        jedis.lpush("lcgx","Google");
        List<String > lcgx=jedis.lrange("lcgx",0,2);
        for (String temp:lcgx)
        {
            System.out.println(temp);
        }
    }


}
