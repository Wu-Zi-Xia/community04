package com.cduestc.community.community.mapper;

import com.cduestc.community.community.model.Notification;
import com.cduestc.community.community.model.NotificationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotificationExMapper {
    //List<Notification> selectByExample(NotificationExample example);
    List<Notification> list1(@Param("accountId") int accountId, @Param("offset")long offset, @Param("size")long size);
}
