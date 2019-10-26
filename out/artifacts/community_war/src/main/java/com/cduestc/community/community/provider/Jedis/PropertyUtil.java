package com.cduestc.community.community.provider.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    //此方法是加载配置文件
    public static Properties loadProperties(String propertyFile) throws IOException {
        Properties properties=new Properties();
        InputStream is =
                PropertyUtil.class.getClassLoader().
                        getResourceAsStream(propertyFile);
        if(is==null)
        {
            is =
                    PropertyUtil.class.getClassLoader().
                            getResourceAsStream("properties/" + propertyFile);
        }
        properties.load(is);
        return properties;
    }
//此方法是获取到配置文件中的对应的key值的value值
    public static String getValue(String propertyFile, String key) throws IOException {
        Properties properties = loadProperties(propertyFile);
        return properties.getProperty(key);
    }


}

