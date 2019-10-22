package com.cduestc.community.community.controller;

import com.cduestc.community.community.dto.NotificationDto;
import com.cduestc.community.community.enums.NotificationTypeEnum;
import com.cduestc.community.community.model.Comment;
import com.cduestc.community.community.model.User;
import com.cduestc.community.community.service.CommentService;
import com.cduestc.community.community.service.NotificationService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private CommentService commentService;
    @RequestMapping("/notification")
    public String notification(@RequestParam("id")int id, HttpServletRequest request){
        //在拦截器里面已经把userModel放入到session中了
        List<User> userModel= (List<User>) request.getSession().getAttribute("userModel");
        if(userModel.size()==0)
        {
            return "redirect:/";
        }
        //已读功能，将status设置为已读
       NotificationDto notificationDto =  notificationService.selectById(id,userModel.get(0));
           //跳转到相应的页面，去数据库查出当前的outer_id
        if(notificationDto.getType()== NotificationTypeEnum.REPLAY_QUESTION.getType())
        {
            return "redirect:/question?id="+notificationDto.getOuterId();
        }
        else if(notificationDto.getType()==NotificationTypeEnum.REPLAY_COMMENT.getType()){
            //获取当前通知对应的评论
            Comment comment=commentService.selectCommentById(notificationDto.getOuterId());
            return "redirect:/question?id="+comment.getParentId();
        }
        return "redirect:/";
    }
}
