package com.cduestc.community.community.service;

import com.cduestc.community.community.dto.NotificationDto;
import com.cduestc.community.community.dto.PaginationDTO;
import com.cduestc.community.community.dto.QuestionDto;
import com.cduestc.community.community.enums.NotificationStatusEnum;
import com.cduestc.community.community.enums.NotificationTypeEnum;
import com.cduestc.community.community.exception.CustomizeErrorCode;
import com.cduestc.community.community.exception.CustomizeException;
import com.cduestc.community.community.mapper.NotificationExMapper;
import com.cduestc.community.community.mapper.NotificationMapper;
import com.cduestc.community.community.mapper.UserMapper;
import com.cduestc.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private NotificationExMapper notificationExMapper;
    public PaginationDTO<NotificationDto> list1(int page, int size, int accountId) {
        PaginationDTO<NotificationDto> paginationDTO=new PaginationDTO<>();
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(accountId);
        int totalCount=(int)notificationMapper.countByExample(notificationExample);
        int totalPage =0;
        if(totalCount%size==0)
        {
            totalPage=totalCount/size;
        }
        if(totalCount%size!=0)
        {
            totalPage=totalCount/size+1;
        }
        if(page<1)
        {
            page=1;
        }
        if(page>totalPage)
        {
            page=totalPage;
        }
        long offset=size*(page-1);//计算分页查询应该从哪个位置开始查询
        List<Notification> notificationList=notificationExMapper.list1(accountId,offset,size);
        List<NotificationDto> notificationDtoList=new ArrayList<>();
        if(notificationList.size()==0){
            return paginationDTO;
        }
        for(Notification notification:notificationList)
        {
            NotificationDto notificationDto=new NotificationDto();
            BeanUtils.copyProperties(notification,notificationDto);//内置的工具类，可以将对象按照属性名去进行属性的赋值
            notificationDto.setTypeName(NotificationTypeEnum.getNameByValue(notification.getType()));
            notificationDtoList.add(notificationDto);
        }
        paginationDTO.setPage(page);
        paginationDTO.setTotalPage(totalPage);
        paginationDTO.setData(notificationDtoList);
        paginationDTO.setPagination(totalCount,page,size);
        return paginationDTO;
    }

    public int countByStatus() {
        NotificationExample notification=new NotificationExample();
        notification.createCriteria().andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return  (int)notificationMapper.countByExample(notification);
    }

    public NotificationDto selectById(int id,User user) {
        NotificationDto notificationDto=new NotificationDto();
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus()).andIdEqualTo(id);
        Notification notification=new Notification();
        NotificationDto notificationDto1=new NotificationDto();
        notification=notificationMapper.selectByPrimaryKey(id);
        if(notification==null)
        {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(notification.getReceiver()!=user.getId())
        {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        //修改已读的状态
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        BeanUtils.copyProperties(notification,notificationDto1);//内置的工具类，可以将对象按照属性名去进行属性的赋值
        notificationDto1.setTypeName(NotificationTypeEnum.getNameByValue(notification.getType()));
        return notificationDto1;
    }
}
