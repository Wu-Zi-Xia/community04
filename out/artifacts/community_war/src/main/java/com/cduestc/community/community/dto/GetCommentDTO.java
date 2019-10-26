package com.cduestc.community.community.dto;

import com.cduestc.community.community.model.User;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class GetCommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String description;
    private User user;
}
