package com.cduestc.community.community.扩展包的复制;

import com.cduestc.community.community.model.Comment;
import org.apache.ibatis.annotations.Param;

public interface CommentExMapper {
    int initCommentCount(@Param("record") Comment record);
}
