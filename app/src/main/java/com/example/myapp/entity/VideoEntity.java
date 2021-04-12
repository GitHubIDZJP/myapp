package com.example.myapp.entity;

import java.io.Serializable;

public class VideoEntity implements Serializable {
    /**
     *vid : 1
     *vtitle : 青龙战甲搭配机动兵，p城上空肆意1v4
     * author : 狙击手麦克
     * coverurl:https://sdsds
     * headurl:https://sdad
     * commentNum:210
     * likeNum:23
     * collectNum:100
     */
    private int vid;
    private String vtitle;
    private String author;
    private String coverurl;
    private String headurl;
    private int commentNum;
    private int likeNum;
    private int collectNum;
    private  String playurl;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getVtitle() {
        return vtitle;
    }

    public void setVtitle(String vtitle) {
        this.vtitle = vtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(int collectNum) {
        this.collectNum = collectNum;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    //    private  int id;
//    private  String title;
//    private  String name;
//    private  int dzCount;
//    private  int collectCount;
//    private  int commentCount;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
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
//    public int getDzCount() {
//        return dzCount;
//    }
//
//    public void setDzCount(int dzCount) {
//        this.dzCount = dzCount;
//    }
//
//    public int getCollectCount() {
//        return collectCount;
//    }
//
//    public void setCollectCount(int collectCount) {
//        this.collectCount = collectCount;
//    }
//
//    public int getCommentCount() {
//        return commentCount;
//    }
//
//    public void setCommentCount(int commentCount) {
//        this.commentCount = commentCount;
//    }
}
