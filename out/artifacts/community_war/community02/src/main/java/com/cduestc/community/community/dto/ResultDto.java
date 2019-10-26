package com.cduestc.community.community.dto;

import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import lombok.Data;

import java.util.List;

@Data
public class ResultDto<T> {
    private Integer code;
    private String message;
    private T data;


    public static ResultDto errorOf(Integer code,String message){
        ResultDto resultDto=new ResultDto();
                resultDto.setCode(code);
                resultDto.setMessage(message);
          return resultDto;
    }

    public static ResultDto errorOf(CustomizeErrorCode noLogin) {
        System.out.println("code"+noLogin.getCode());
        return errorOf(noLogin.getCode(),noLogin.getMessage());
    }
    public static<T> ResultDto oxOf(T t)
    {
        ResultDto resultDto=new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        resultDto.setData(t);
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeException ex) {

    return errorOf(ex.getCode(), ex.getMessage());
    }
    public static ResultDto oxOf()
    {
        ResultDto resultDto=new ResultDto();
        resultDto.setCode(200);
        resultDto.setMessage("请求成功");
        return resultDto;
    }
}
