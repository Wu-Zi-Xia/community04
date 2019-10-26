package com.cduestc.community.community.service;

import com.cduestc.community.community.dto.GetCommentDTO;
import com.cduestc.community.community.enums.CommentTypeEnum;
import com.cduestc.community.community.enums.NotificationStatusEnum;
import com.cduestc.community.community.enums.NotificationTypeEnum;
import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import com.cduestc.community.community.mapper.*;
import com.cduestc.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionExMapper questionExMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentExMapper commentExMapper;
    @Autowired
    NotificationMapper notificationMapper;
    @Transactional//将整个方法体作为一个事务进行操作
    public void insert(Comment comment,User user) {//将评论人传过来是想在查询通知的时候，少查询一次user
       if(comment.getParentId()==null|| comment.getParentId()==0)//异常处理代码,判断是否有被评论的元素传过来
       {
           throw new CustomizeException(CustomizeErrorCode.TARGET_PARENT_NOT_FOUND);
       }
       if(comment.getType()==0||!CommentTypeEnum.isExist(comment.getType()))//判断发起的评论是否是正确的，在这个项目中只有给问题做评论或者给评论做评论这两种
       {
           throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
       }

       if(comment.getType()==CommentTypeEnum.COMMENT.getType())//判断是在给问题做评论还是给评论做评论
       {
           //回复评论
           Comment parentComment=commentMapper.selectByPrimaryKey(comment.getParentId());
           if(parentComment==null)//判断数据库是否有数据
           {
               throw new CustomizeException(CustomizeErrorCode.PARENT_COMMENT_NOT_FOUND);
           }
           else
               {
                   commentMapper.insert(comment);
                   parentComment.setCommentCount(1);
                   commentExMapper.initCommentCount(parentComment);
                   //创建一个通知 ctrl+alt+m 选中代码段抽出一个方法
                   Notification notification=new Notification();
                   notification.setGmtCreate(System.currentTimeMillis());
                   notification.setOuterId(comment.getParentId());
                   notification.setNotifier(comment.getCommentator());
                   notification.setReceiver(parentComment.getCommentator());
                   notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
                   notification.setType(NotificationTypeEnum.REPLAY_COMMENT.getType());
                   notification.setNotifierName(user.getName());
                   notification.setOuterTitle(parentComment.getDescription());
                   notificationMapper.insert(notification);
               }
       }
       else
           {
               //回复问题
             Question parentQuestion= questionMapper.selectByPrimaryKey(comment.getParentId());
             if(parentQuestion==null)//判断数据库是否有数据
             {
                 throw new CustomizeException(CustomizeErrorCode.PARENT_PARENT_NOT_FOUND);
             }
             else
                 {
                     //这里这两个插入操作必须要保持原子性，所以要使用事务
                     commentMapper.insert(comment);
                     parentQuestion.setCommentCount(1);
                     questionExMapper.initCommentCount(parentQuestion);
                     //创建一个通知
                     Notification notification=new Notification();
                     notification.setGmtCreate(System.currentTimeMillis());
                     notification.setOuterId(comment.getParentId());
                     notification.setNotifier(comment.getCommentator());
                     notification.setReceiver(parentQuestion.getCreator());
                     notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
                     notification.setType(NotificationTypeEnum.REPLAY_QUESTION.getType());
                     notification.setNotifierName(user.getName());
                     notification.setOuterTitle(parentQuestion.getTitle());
                     notificationMapper.insert(notification);
                 }
           }

    }
//ctrl+alt+p:抽离出一个变量出来
    public List<GetCommentDTO> getByParentId(Integer id, Integer type) {
        List<GetCommentDTO> getCommentDTOList=new ArrayList<>();
        CommentExample example=new CommentExample();
        example.setOrderByClause("gmt_create desc");
        example.createCriteria().andParentIdEqualTo(id);
        example.createCriteria().andTypeEqualTo(type);
        List<Comment> comments=commentMapper.selectByExample(example);//获取到所有的评论
//        for (Comment comment:list)
//        {
//            GetCommentDTO getCommentDTO=new GetCommentDTO();
//            BeanUtils.copyProperties(comment, getCommentDTO);
//            getCommentDTO.setUser(userMapper.selectByPrimaryKey(comment.getCommentator()));//通过评论的创建者去获取到用户信息
//            getCommentDTOList.add(getCommentDTO);
//        }
        if(comments.size()==0)
        {
            return null;
        }
        //使用namenda表达式找出评论中所有非重复的评论者
        Set<Integer> commentators=comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds=new ArrayList<>();
        userIds.addAll(commentators);
       // System.out.println(getCommentDTOList.size());

        //获取评论人并且转换成Map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userIds);
       List<User> users=userMapper.selectByExample(userExample);
        Map<Integer, User> userMap=users.stream().collect(Collectors.toMap(user -> user.getId(),user -> user));

        //转换comment为getCommentDTO
        getCommentDTOList=comments.stream().map(comment -> {
            GetCommentDTO getCommentDTO=new GetCommentDTO();
            BeanUtils.copyProperties(comment, getCommentDTO);
            getCommentDTO.setUser(userMap.get(comment.getCommentator()));
            return getCommentDTO;
        }).collect(Collectors.toList());

        return getCommentDTOList;
    }

    public Comment selectCommentById(int outerId) {
       Comment comment= commentMapper.selectByPrimaryKey(outerId);
       if(comment==null)
       {
           throw new CustomizeException(CustomizeErrorCode.PARENT_COMMENT_NOT_FOUND);
       }
       return comment;
    }
}
