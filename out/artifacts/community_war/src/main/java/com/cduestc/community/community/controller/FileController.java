package com.cduestc.community.community.controller;

import com.cduestc.community.community.dto.FileDto;
import com.cduestc.community.community.provider.CosProvider;
import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class FileController {
    @Autowired
    CosProvider cosProvider;
    @ResponseBody//此接口返回的数据是json格式的
    @RequestMapping("/file/upload")
    public FileDto upload(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest) request;
        MultipartFile file=multipartHttpServletRequest.getFile("editormd-image-file");
        COSClient cosClient=cosProvider.getCosClient();
        FileInputStream fileInputStream=null;
        //文件上传
        String url=cosProvider.upload(file, fileInputStream,cosClient);
        //返回url
        FileDto fileDto=new FileDto();
        fileDto.setSuccess(1);
        fileDto.setMessage("图片描述");
        fileDto.setUrl(url);
        return fileDto;
    }
}
