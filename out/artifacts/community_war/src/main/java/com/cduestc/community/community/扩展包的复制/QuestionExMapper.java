package com.cduestc.community.community.扩展包的复制;

import com.cduestc.community.community.model.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionExMapper {
    int initViewCount(@Param("record") Question record);

    int initCommentCount(@Param("record") Question record);
    List<Question> list(@Param("offset") int offset, @Param("size") Integer size);

    Integer countByToken(String accountId);

    List<Question> list1(@Param("accountId") String accountId, @Param("offset") int offset, @Param("size") Integer size);


}
