package com.cduestc.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND("你的问题不在了，要不然换个试试呗",2001),
    NOTIFICATION_NOT_FOUND("你的通知不在了，要不然换个试试呗",2010),
    TARGET_PARENT_NOT_FOUND("未选中任何问题或者评论回复",2002),
    NO_LOGIN("当前账户未登录，请登录重试",2003),
     TYPE_PARAM_WRONG ("您未选中任何元素，请选择一个吧",2004),
    PARENT_COMMENT_NOT_FOUND("你操作的评论不存在了",2005),
    PARENT_PARENT_NOT_FOUND("你操作的问题不存在了",2006 ),
    SYSTEM_ERROR("服务器冒烟了，请稍后重试",2007),
    COMMENT_IS_EMPTY("评论内容不能为空",2008 ),
    READ_NOTIFICATION_FAIL("兄弟你这是读别人的信息呢？",2009 ),
    FILE_UPLOAD_FAIL("图片上传失败",2011);
    private String message;
    private Integer code;
    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(String s, Integer c){
        this.message=s;
        this.code=c;
    }
}
