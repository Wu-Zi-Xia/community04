package com.cduestc.community.community.enums;

public enum  NotificationTypeEnum {
    REPLAY_QUESTION(1,"回复了你的问题"),
    REPLAY_COMMENT(2,"回复了你的评论"),
    LIKE_QUESTION(3,"点赞了了你的问题"),
    LIKE_COMMENT(4,"点赞了了你的评论");
    private int type;
    private String name;

    private NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

    public static String getNameByValue(int type) {
        for(NotificationTypeEnum notificationTypeEnum:NotificationTypeEnum.values())
        {
           if(notificationTypeEnum.getType()==type)
           {
               return notificationTypeEnum.getName();
           }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
