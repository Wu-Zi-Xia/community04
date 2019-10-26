package com.cduestc.community.community.provider.Jedis;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
public class RedisJava {
    public static void main(String[] args) {
        //连接本地的redis服务
        Jedis jedis=new Jedis("localhost");
        System.out.println("服务正在运行:"+jedis.ping());
    }
}
