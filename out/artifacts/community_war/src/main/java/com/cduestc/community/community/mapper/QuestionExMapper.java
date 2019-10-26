package com.cduestc.community.community.mapper;

import com.cduestc.community.community.dto.QuestionQueryDto;
import com.cduestc.community.community.model.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionExMapper {
    int initViewCount(@Param("record")Question record);

    int initCommentCount(@Param("record")Question record);
    List<Question> list(@Param("offset") int offset, @Param("size") Integer size);

    Integer countByToken(int accountId);

    List<Question> list1(@Param("accountId") int accountId, @Param("offset")int offset, @Param("size")Integer size);

    List<Question> selectRelated(@Param("record")Question record);

    int countBySearch(QuestionQueryDto questionQueryDto);

    List<Question> selectBySearch(QuestionQueryDto questionQueryDto);
}
