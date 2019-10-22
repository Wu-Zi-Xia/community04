package com.cduestc.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    //private List<NotificationDto> notificationDtoList;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private int page;
    private int totalPage;
    private List<Integer> pages=new ArrayList<>();

    //设置分页下面的那些出现，那些不出现
    public void setPagination(int totalCount, int page, int size) {
//        totalPage=0;
//        if (totalCount % size == 0) {
//            totalPage = totalCount / size;
//        } else {
//            totalPage = totalCount / size + 1;
//        }


               pages.add(page);
              for(int i=1;page-i>0&i<=3;i++)
              {
                  pages.add(0,page-i);
                  System.out.println("向前加");
              }
              for(int i=1;page+i<=totalPage&i<=3;i++)
              {
                  pages.add(pages.size(),page+i);
                  System.out.println("向后加");
              }
//          for(int i=0;i<pages.size();i++)
//          {
//          System.out.println("Page::::::::::::::::::"+pages.get(i));
//          }


        //是否展示上一页
        if(page==1)
        {
            showPrevious=false;
        }
        else
            {
                showPrevious=true;
            }
        if(page==totalPage)
        {
            showNext=false;
        }
        else
            {
                showNext=true;
            }
        //是否展示第一页
        if(pages.contains(1))
        {
            showFirstPage=false;
        }
        else
            {
                showFirstPage=true;
            }
        //是否展示最后一页
        if(pages.contains(totalPage))
        {
            showEndPage=false;
        }
        else
        {
            showEndPage=true;
        }
    }
}
