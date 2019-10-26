package com.cduestc.community.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDto {
    private String search;
    private int page;
    private int size;
}
