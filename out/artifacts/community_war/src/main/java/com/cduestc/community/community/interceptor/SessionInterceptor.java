package com.cduestc.community.community.interceptor;

import com.cduestc.community.community.mapper.UserMapper;
import com.cduestc.community.community.model.User;
import com.cduestc.community.community.model.UserExample;
import com.cduestc.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//alt+insert键可以快速完成方法的覆写
@Service
public class SessionInterceptor implements HandlerInterceptor
{
    @Autowired
     UserMapper userMapper;
    @Autowired
    NotificationService notificationService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        Cookie[] cookies=request.getCookies();//主要是为了保存登录态，在服务器关闭后，也能保证用户处于登录状态
        if(cookies!=null&&cookies.length!=0)//判断cookie不为空
        {
            for (Cookie cokie:cookies)
            {
                if(cokie.getName().equals("token"))
                {

                    UserExample example = new UserExample();
                    example.createCriteria().andTokenEqualTo(cokie.getValue());
                    List<User> users =userMapper.selectByExample(example);
                    if(users.size()!=0)
                    {
                        request.getSession().setAttribute("userModel",users);//将用户信息放入到session中
                        System.out.println("SessionInterceptor=============================="+request.getSession().getAttribute("userModel"));
                       //在拦截器里面去做未读数这个就是为了在刷新页面的时候可以让未读数始终保持最新数据
                        int unreadCount=notificationService.countByStatus();
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
