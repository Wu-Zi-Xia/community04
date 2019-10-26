package com.cduestc.community.community.controller;

import com.cduestc.community.community.dto.GetCommentDTO;
import com.cduestc.community.community.dto.QuestionDto;
import com.cduestc.community.community.enums.CommentTypeEnum;
import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import com.cduestc.community.community.service.CommentService;
import com.cduestc.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller

public class QuestionsController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @GetMapping("/question")
    public String question(@RequestParam(name ="id") Integer id,
                           Model model,
                           HttpServletRequest request)
    {
        if(request.getSession().getAttribute("userModel")==null)
        {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
       //查询这个问题
       QuestionDto questionDto =questionService.getClassById(id);
        //查询问题所有的一级评论
        List<GetCommentDTO> commentDTOList=new ArrayList<GetCommentDTO>();
        commentDTOList=commentService.getByParentId(id, CommentTypeEnum.QUESTION.getType());
        questionDto.setCommentDTOList(commentDTOList);//查询出当前问题的所有评论的数据
        //查询与当前问题相关的所有的问题
        List<QuestionDto> relatedQuestions=questionService.getRelatedQuestion(questionDto);
       //增加阅读数
       questionService.initViewCount(id);
       model.addAttribute("questions",questionDto);
       model.addAttribute("relatedQuestions",relatedQuestions);
        return "questions";
    }
}
