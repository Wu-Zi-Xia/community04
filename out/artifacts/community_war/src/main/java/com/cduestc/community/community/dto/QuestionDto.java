package com.cduestc.community.community.dto;

import com.cduestc.community.community.model.User;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private String creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User userModel;
    private List<GetCommentDTO> commentDTOList;
}
