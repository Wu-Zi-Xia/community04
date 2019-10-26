package com.cduestc.community.community.cache;

import com.cduestc.community.community.dto.TagDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public List<TagDto> getTagCache(){
        List<String> tags1=new ArrayList<String>();
        tags1.add("java");
        tags1.add("c语言");
        tags1.add("Python");
        TagDto program1=setTagDtos("开发语言",tags1);

        List<String> tags2=new ArrayList<String>();
        tags2.add("Spring");
        tags2.add("Spring boot");
        tags2.add("Spring mvc");
        TagDto program2=setTagDtos("平台架构",tags2);

        List<TagDto> tagDtos=new ArrayList<>();
        tagDtos.add(program1);
        tagDtos.add(program2);
        return tagDtos;
    }
    private TagDto setTagDtos(String navTag,List<String> tags){
        TagDto program=new TagDto();
        program.setCategoryName(navTag);
        List<String> tags1=new ArrayList<>();
        for(String a:tags)
        {
            tags1.add(a);
        }
        program.setTags(tags1);
        return program;
    }
    public String filterInvalid(String tags){
        String[] strings= StringUtils.split(tags,"，");
        List<TagDto> tagDtoList=getTagCache();
        List<String> tagList=tagDtoList.stream().
               flatMap(tagDto -> tagDto.getTags().stream()).
               collect(Collectors.toList());//查出集合里面的子集合的所有元素
        String invalid=Arrays.stream(strings).filter(t->!tagList.contains(t)).collect(Collectors.joining("，"));//判断我传过来的字符串数组里面是否都包含在我定义的所有标签
        return invalid;
    }
}
