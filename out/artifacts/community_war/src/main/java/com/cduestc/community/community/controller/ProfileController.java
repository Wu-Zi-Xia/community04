package com.cduestc.community.community.controller;

import com.cduestc.community.community.dto.NotificationDto;
import com.cduestc.community.community.dto.PaginationDTO;
import com.cduestc.community.community.dto.QuestionDto;
import com.cduestc.community.community.model.Notification;
import com.cduestc.community.community.model.User;
import com.cduestc.community.community.service.NotificationService;
import com.cduestc.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;
    @GetMapping("/profile/{action}")//动态进行路径的跳转
    public String profile(@RequestParam(value = "page",defaultValue = "1") Integer page,
                          @RequestParam(value = "size",defaultValue = "5") Integer size,
                          @PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request)
    {
        //在拦截器里面已经把userModel放入到session中了
        List<User> userModel= (List<User>) request.getSession().getAttribute("userModel");
        if(userModel.size()==0)
        {
            return "redirect:/";
        }
        if("question".equals(action))
        {
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO<QuestionDto> paginationDTO= questionService.list1(page,size,userModel.get(0).getId());
            if(paginationDTO==null)
            {
                model.addAttribute("error","你还没有问题哦");
            }
            else
                {
                model.addAttribute("paginationDTO",paginationDTO);

                }
        }
        if("replies".equals(action))
        {
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            PaginationDTO<NotificationDto> paginationDTO= notificationService.list1(page,size,userModel.get(0).getId());
            if(paginationDTO==null)
            {
                model.addAttribute("error","你还没有回复哦");
            }
            else
                {
                    model.addAttribute("paginationDTO",paginationDTO);
            }
        }
        return "profile";
    }
}
