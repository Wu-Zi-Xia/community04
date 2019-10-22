package com.cduestc.community.community.dto;

import com.cduestc.community.community.model.User;
import lombok.Data;
import org.springframework.test.context.junit4.SpringRunner;

@Data
public class NotificationDto {
    private int id;
    private Long gmtCreate;
    private int status;
    private int notifier;
    private String notifierName;
    private String outerTitle;
    private int outerId;
    private String typeName;
    private int type;
}
