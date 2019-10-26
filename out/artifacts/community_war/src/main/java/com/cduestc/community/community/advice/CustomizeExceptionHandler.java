package com.cduestc.community.community.advice;

import com.alibaba.fastjson.JSON;
import com.cduestc.community.community.dto.ResultDto;
import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable ex, Model model, HttpServletResponse response) throws IOException {
        if("application/json".equals(request.getContentType()))
        {
         //返回json
            ResultDto resultDto=null;

            if(ex instanceof CustomizeException)//表示是我自己定义的异常，是已知的
            {
                resultDto=ResultDto.errorOf((CustomizeException)ex);
            }else//未知的就说服务器冒烟了，程序员后续会进行处理
            {
                System.out.println("============================ex"+ex);
                resultDto= ResultDto.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            response.setCharacterEncoding("utf-8");
            response.setStatus(200);
            response.setContentType("application/json");
            PrintWriter writer=response.getWriter();
            writer.write(JSON.toJSONString(resultDto));
            writer.close();
        }else
            {
                //返回错误页面
                if(ex instanceof CustomizeException)//表示是我自己定义的异常，是已知的
                {
                    model.addAttribute("message",ex.getMessage());
                }else//未知的就说服务器冒烟了，程序员后续会进行处理
                {
                    model.addAttribute("message",CustomizeErrorCode.SYSTEM_ERROR.getMessage());
                }
                return new ModelAndView("error");

            }
        return null;
        // HttpStatus status=getStatus(request);

    }




    private HttpStatus getStatus(HttpServletRequest request)
    {
        Integer statusCode= (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode==null)
        {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
