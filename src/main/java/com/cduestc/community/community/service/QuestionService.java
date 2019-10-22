package com.cduestc.community.community.service;

import com.cduestc.community.community.dto.PaginationDTO;
import com.cduestc.community.community.dto.QuestionDto;
import com.cduestc.community.community.dto.QuestionQueryDto;
import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import com.cduestc.community.community.mapper.QuestionExMapper;
import com.cduestc.community.community.mapper.QuestionMapper;
import com.cduestc.community.community.mapper.UserMapper;
import com.cduestc.community.community.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * spring会自动管理，service可以起到一个组装的作用，当前台需要返回的数据，对于数据库来说，可能会多几个字段，那么，
 * 使用传输层的对象去返回给前端，但是在service中以数据库的基本表去查询，组装成我们需要的传输层的对象
 */
@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionExMapper questionExMapper;
    //列表展示所有的question数据
    public PaginationDTO list(String search,Integer page, Integer size) {
        if(StringUtils.isNotBlank(search))
        {
            String[] tags= StringUtils.split(search," ");
            search=Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        //容错校验
//        QuestionExample questionExample=new QuestionExample();
//        int totalCount=(int)questionMapper.countByExample(questionExample);//获取问题总数
        QuestionQueryDto questionQueryDto=new QuestionQueryDto();
        questionQueryDto.setSearch(search);
        int totalCount=questionExMapper.countBySearch(questionQueryDto);
        if(totalCount==0)
        {
            return null;
        }
        Integer totalPage=0;
        if(totalCount%size==0)
        {
            totalPage=totalCount/size;
        }
        if(totalCount%size!=0)
        {
            totalPage=totalCount/size+1;
        }
        if(page<1)
        {
            page=1;
        }
        if(page>totalPage)
        {
            page=totalPage;
        }
        int offset=size*(page-1);//计算分页查询应该从哪个位置开始查询
//        List<Question> questionModelList=questionExMapper.list(offset,size);
        questionQueryDto.setPage(offset);
        questionQueryDto.setSize(size);
        List<Question> questionModelList=questionExMapper.selectBySearch(questionQueryDto);
        List<QuestionDto> questionDtoList=new ArrayList<>();
        PaginationDTO<QuestionDto> paginationDTO=new PaginationDTO<>();
        for(Question questionModel:questionModelList)
        {
            UserExample userExample=new UserExample();
            userExample.createCriteria().andIdEqualTo(questionModel.getCreator());
            List<User> user=userMapper.selectByExample(userExample);//找到发起问题的人
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(questionModel,questionDto);//内置的工具类，可以将对象按照属性名去进行属性的赋值
            questionDto.setUserModel(user.get(0));
            questionDtoList.add(questionDto);
        }
        paginationDTO.setPage(page);
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setData(questionDtoList);

        paginationDTO.setPagination(totalCount,page,size);
        return paginationDTO;
    }

    public PaginationDTO<QuestionDto> list1(Integer page, Integer size, int accountId) {
        //容错校验
        System.out.println(accountId);
        Integer totalCount= questionExMapper.countByToken(accountId);
        if(totalCount==0)
        {
            return null;
        }
        Integer totalPage=0;
        if(totalCount%size==0)
        {
            totalPage=totalCount/size;
        }
        if(totalCount%size!=0)
        {
            totalPage=totalCount/size+1;
        }
        if(page<1)
        {
            page=1;
        }
        if(page>totalPage)
        {
            page=totalPage;
        }
        int offset=size*(page-1);//计算分页查询应该从哪个位置开始查询
        List<Question> questionModelList=questionExMapper.list1(accountId,offset,size);
        List<QuestionDto> questionDtoList=new ArrayList<>();
        PaginationDTO<QuestionDto> paginationDTO=new PaginationDTO();
        for(Question questionModel:questionModelList)
        {
            UserExample userExample=new UserExample();
            userExample.createCriteria().andIdEqualTo(questionModel.getCreator());
            List<User> user=userMapper.selectByExample(userExample);//找到发起问题的人
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(questionModel,questionDto);//内置的工具类，可以将对象按照属性名去进行属性的赋值
            questionDto.setUserModel(user.get(0));
            questionDtoList.add(questionDto);
        }
        paginationDTO.setPage(page);
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setData(questionDtoList);
        paginationDTO.setPagination(totalCount,page,size);
        return paginationDTO;
    }

    public QuestionDto getClassById(Integer id) {
        QuestionDto questionDto=new QuestionDto();
        Question questionModel=questionMapper.selectByPrimaryKey(id);
        if(questionModel==null)
        {
           throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);//抛出一个throwable类型的对象，并且里面传入的的自己定义的枚举对象
        }
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdEqualTo(questionModel.getCreator());
        List<User> user=userMapper.selectByExample(userExample);//找到发起问题的人
        BeanUtils.copyProperties(questionModel,questionDto);//内置的工具类，可以将对象按照属性名去进行属性的赋值
        questionDto.setUserModel(user.get(0));
        return questionDto;
    }

    public QuestionDto selectOneById(Integer id) {
        Question questionModel= questionMapper.selectByPrimaryKey(id);
        if(questionModel==null)
        {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);//抛出一个throwable类型的对象，并且里面传入的的自己定义的枚举对象
        }
        QuestionDto questionDto=new QuestionDto();
        BeanUtils.copyProperties(questionModel,questionDto);
        return questionDto;
    }

    public void createOrUpdate(Question questionModel) {
        if(questionModel.getId()!=null)
        {
            QuestionExample questionExample=new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(questionModel.getId());
           int updated=questionMapper.updateByExampleSelective(questionModel,questionExample);
            if(updated!=1)
            {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);//抛出一个RuntimeException类型的对象，并且里面传入的的自己定义的枚举对象（也就是可能出现的异常）
            }
        }
        else
            {
                questionMapper.insert(questionModel);
            }
    }

    public void initViewCount(Integer id) {
        Question updateQuestion=new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        //处理了并发访问的问题
        questionExMapper.initViewCount(updateQuestion);
    }

    public List<QuestionDto> getRelatedQuestion(QuestionDto relatedQuestionDto) {
        if(StringUtils.isEmpty(relatedQuestionDto.getTag()))
        {
            return new ArrayList<>();
        }
       String[] tags= StringUtils.split(relatedQuestionDto.getTag(),"，");
       String regexpTag=Arrays.stream(tags).collect(Collectors.joining("|"));
       System.out.println("========================"+regexpTag);
       Question question=new Question();
       question.setId(relatedQuestionDto.getId());
       question.setTag(regexpTag);
       List<Question> questionList=questionExMapper.selectRelated(question);//查找相关的问题
        List<QuestionDto> questionDtoList=questionList.stream().map(q->{
            QuestionDto questionDto=new QuestionDto();
            BeanUtils.copyProperties(q,questionDto);
            return questionDto;
        }).collect(Collectors.toList());
        return  questionDtoList;
    }
}
