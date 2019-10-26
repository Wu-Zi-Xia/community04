package com.cduestc.community.community.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer parentId;
    private String description;
    private Integer type;
}
