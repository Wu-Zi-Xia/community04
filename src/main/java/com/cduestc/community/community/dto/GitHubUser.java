package com.cduestc.community.community.dto;

import lombok.Data;

@Data
public class GitHubUser {
    private String id;
    private String name;
    private String bio;
    private String avatarUrl;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getBio() {
//        return bio;
//    }
//
//    public void setBio(String bio) {
//        this.bio = bio;
//    }
//
//    @Override
//    public String toString() {
//        return "GitHubUser{" +
//                "id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                ", bio='" + bio + '\'' +
//                '}';
//    }
}
