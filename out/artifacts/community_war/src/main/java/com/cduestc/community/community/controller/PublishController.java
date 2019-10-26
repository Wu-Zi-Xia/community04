package com.cduestc.community.community.controller;

import com.cduestc.community.community.cache.TagCache;
import com.cduestc.community.community.dto.QuestionDto;
import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import com.cduestc.community.community.mapper.QuestionMapper;
import com.cduestc.community.community.model.Question;
import com.cduestc.community.community.model.User;
import com.cduestc.community.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.HTML;
import java.util.List;

@Controller
public class PublishController {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionService questionService;
    @GetMapping("/publishUpdate" )
    public String edit(@RequestParam("id") Integer id,
                       Model model)
    {


//        QuestionModel questionModel=new QuestionModel();

        QuestionDto questionDto=questionService.selectOneById(id);
        if(questionDto==null)
        {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
//        questionModel.setTitle(questionDto.getTitle());
//        questionModel.setDescription(questionDto.getDescription());
//        questionModel.setTag(questionDto.getTag());
        model.addAttribute("title",questionDto.getTitle());
        model.addAttribute("description",questionDto.getDescription());

        model.addAttribute("tag",questionDto.getTag());//放入到model中，用作回显
        model.addAttribute("id",questionDto.getId());
        return "publish";
    }



    //访问发布页面
    @GetMapping("/publish")
    public String publish(Model model)
    {
        model.addAttribute("tags",new TagCache().getTagCache());
        return "publish";
    }
//    提交发布的问题的路由
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam("id") Integer id,
                            HttpServletRequest request,
                            Model model)
    {
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);//放入到model中，用作回显
        model.addAttribute("id",id);

        //判空验证没有做，不难
        if(StringUtils.isEmpty(title))
        {
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(StringUtils.isEmpty(description))
        {
            model.addAttribute("error","问题详情不能为空");
            return "publish";
        }
        if(StringUtils.isEmpty(tag))
        {
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        //判断标签是否合法
        TagCache tagCache=new TagCache();
        String invalid=tagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invalid))
        {
            model.addAttribute("error","输入非法字符"+invalid);
            return "publish";
        }

        Question questionModel=new Question();
        questionModel.setTitle(title);
        questionModel.setDescription(description);
        questionModel.setTag(tag);
        questionModel.setId(id);
        questionModel.setCommentCount(0);
        questionModel.setViewCount(0);
        questionModel.setLikeCount(0);
        //在拦截器里面已经把userModel放入到session中了
       List<User> userModel= (List<User>) request.getSession().getAttribute("userModel");
        if(userModel.size()==0)
        {
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        questionModel.setCreator(userModel.get(0).getId());
        questionModel.setGmtCreate(System.currentTimeMillis());
        questionModel.setGmtModified(questionModel.getGmtCreate());
        questionService.createOrUpdate(questionModel);
       return "redirect:/";
    }

}
