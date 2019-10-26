package com.cduestc.community.community.provider.Jedis;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
public class RedisKeyJava {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost");
        Set<String> keys= jedis.keys("*");
        Iterator<String>keysSet= keys.iterator();
        while (keysSet.hasNext())
        {
            System.out.println(keysSet.next());
        }
    }
}
