package com.bean;

/**
 * Created by Rajalakshmi on 30-11-2015.
 */
public class NotifyListBean{

    private String from=null;
    private String to=null;
    private String owner=null;
    private String type =null;
    private String content =null;
    private String media =null;
    private String sortdate=null;
    private String notifttype=null;
    private int viewed=0;
    private String fileid =null;
    private String username="";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSortdate() {
        return sortdate;
    }

    public void setSortdate(String sortdate) {
        this.sortdate = sortdate;
    }

    public String getNotifttype() {
        return notifttype;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public void setNotifttype(String notifttype) {
        this.notifttype = notifttype;

    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString(){
        return "==" + from+
                "==" + to+
                "==" + owner+
                "==" + type +
                "==" + content +
                "==" + media +
                "==" + sortdate+
                "==" + notifttype+
                "==" + viewed+
                "==" + fileid;
    }
}