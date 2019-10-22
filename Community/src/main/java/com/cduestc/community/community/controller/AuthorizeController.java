package com.cduestc.community.community.controller;

import com.cduestc.community.community.dto.AccessTokenDto;
import com.cduestc.community.community.dto.GitHubUser;
import com.cduestc.community.community.model.User;
import com.cduestc.community.community.provider.GithubProvider;
import com.cduestc.community.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {
    @Autowired
    GithubProvider githubProvider;
    @Autowired
    UserService userService;
    @Value("${github.client.id}")//配置文件里面的key的值将会被注入到这个变量里面
    private String client_id;
    @Value("${github.client.secrect}")
    private String client_secret;
    @Value("${github.redirect.uri}")
    private String client_redirect_uri;
    @GetMapping("/callback")
    public String callBack(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        AccessTokenDto accessTokenDto=new AccessTokenDto();
        accessTokenDto.setClient_id(client_id);
        accessTokenDto.setClient_secret(client_secret);
        accessTokenDto.setCode(code);
        accessTokenDto.setState(state);
        accessTokenDto.setRedirect_url(client_redirect_uri);
       String accessToken= githubProvider.getAccessTokenDato(accessTokenDto);//获取到accessToken
        GitHubUser gitHubUser=githubProvider.getUser(accessToken);
        //System.out.println(gitHubUser);
        if(gitHubUser!=null)//如果对象不等于null
        {
            List<User> userModel1=userService.getUserByAccountId(new Integer(gitHubUser.getId()));
            if(userModel1.size()==0) {
                User userModel = new User();
                userModel.setToken(UUID.randomUUID().toString());
                userModel.setName(gitHubUser.getName());
                userModel.setAccountId(new Integer(gitHubUser.getId()));
                userModel.setGmtCreate(System.currentTimeMillis());
                userModel.setGmtModified(userModel.getGmtCreate());
                userModel.setAvatarUrl(gitHubUser.getAvatarUrl());
                System.out.println(userModel);
                userService.insert(userModel);//插入数据库
                response.addCookie(new Cookie("token", userModel.getToken()));//将toke放入到cookie中；
                request.getSession().setAttribute("userModel", userModel);//将用户信息放入到session中
            }
            else
                {
                    User userModel = new User();
                    userModel.setToken(UUID.randomUUID().toString());
                    userModel.setName(gitHubUser.getName());
                    userModel.setAccountId(new Integer(gitHubUser.getId()));
//                    userModel.setGmtCreate(String.valueOf(System.currentTimeMillis()));
                  // userModel.setGmtModified(String.valueOf(System.currentTimeMillis()));
                    userModel.setAvatarUrl(gitHubUser.getAvatarUrl());
                    System.out.println(userModel);
                    userService.update(userModel);//更新数据库
                    response.addCookie(new Cookie("token", userModel.getToken()));//将toke放入到cookie中；
                    request.getSession().setAttribute("userModel", userModel);//将用户信息放入到session中
                }
            return "redirect:/";
        }
        else
            {
           log.error("callback get githubUser error,{}",gitHubUser);
        return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response)
    {
        request.getSession().removeAttribute("userModel");

        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
