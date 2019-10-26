package com.cduestc.community.community.provider;

import com.alibaba.fastjson.JSON;
import com.cduestc.community.community.dto.AccessTokenDto;
import com.cduestc.community.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessTokenDato(AccessTokenDto accessTokenDto) throws IOException {
         MediaType mediaType = MediaType.get("application/json; charset=utf-8");
         OkHttpClient client = new OkHttpClient();
         RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
         Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")//okhttp的post请求
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String s= response.body().string();

            String[] Strings=s.split("&");//返回两个String拆分之后的前后两个String
            String token=Strings[0].split("=")[1];
            System.out.println(token);
            return token;
        }
    }
    public GitHubUser getUser(String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();//okhttp的get请求
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
            Response response = client.newCall(request).execute();
            String rep= response.body().string();//获取到user的json字符串
        System.out.println(rep);
       GitHubUser gitHubUser= JSON.parseObject(rep,GitHubUser.class);//将json数据转成对象
        return gitHubUser;
    }
}
