package com.cduestc.community.community.controller;

import com.cduestc.community.community.dto.CommentDTO;
import com.cduestc.community.community.dto.GetCommentDTO;
import com.cduestc.community.community.dto.ResultDto;
import com.cduestc.community.community.enums.CommentTypeEnum;
import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import com.cduestc.community.community.model.Comment;
import com.cduestc.community.community.model.User;
import com.cduestc.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController
{
    @Autowired
   private CommentService commentService;
    @ResponseBody//此接口返回的数据是json格式的
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
//    在这个地方我困扰了很久，在使用postman进行接口的访问的时候,我总是不能够从session中获取到我已经在拦截器中写入的用户，最后发现我的请求中没有往cookie中写入我用token去标识的用户的那个值
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request)
    {
        List<User> userModel= (List<User>) request.getSession().getAttribute("userModel");
       // System.out.println("-==========================================================="+userModel+"========================="+request.getSession().getAttribute("userModel"));
        if(userModel==null)
        {
           // ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentDTO==null|| StringUtils.isBlank(commentDTO.getDescription()))
        {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment=new Comment();
        BeanUtils.copyProperties(commentDTO,comment);
        comment.setCommentator(userModel.get(0).getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0l);
        comment.setCommentCount(0);
        commentService.insert(comment,userModel.get(0));
        return ResultDto.oxOf();
    }

    @ResponseBody//此接口返回的数据是json格式的
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDto<List<GetCommentDTO>> comments(@PathVariable(name = "id") Integer id){
       List<GetCommentDTO> getCommentDTOList=commentService.getByParentId(id,CommentTypeEnum.COMMENT.getType());
        return ResultDto.oxOf(getCommentDTOList);
    }


}
