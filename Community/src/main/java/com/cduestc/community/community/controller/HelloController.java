package com.cduestc.community.community.controller;

import com.cduestc.community.community.dto.PaginationDTO;
import com.cduestc.community.community.mapper.UserMapper;
import com.cduestc.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HelloController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    String error=null;
    @RequestMapping("/")
    public String hello(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "5") Integer size,
                        @RequestParam(value = "search",required = false) String search) {

        PaginationDTO paginationDTO=questionService.list(search,page,size);
        if(paginationDTO==null)
        {
            error="快点击右上角，发布你的第一条问题吧";
            model.addAttribute("error",error);//model将数据传到前端
        }
        else {
            model.addAttribute("paginationDTO", paginationDTO);//model将数据传到前端
            if(search!=null)
            {
                model.addAttribute("search",search);
            }

        }
            return "index";
    }
}
