package com.collegeapp.bernhardtinfo.faculty;

public class TeacherData {
    private String tname,temail,tpost,image,key;

    public TeacherData() {
    }

    public TeacherData(String tname, String temail, String tpost, String image, String key) {
        this.tname = tname;
        this.temail = temail;
        this.tpost = tpost;
        this.image = image;
        this.key = key;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTemail() {
        return temail;
    }

    public void setTemail(String temail) {
        this.temail = temail;
    }

    public String getTpost() {
        return tpost;
    }

    public void setTpost(String tpost) {
        this.tpost = tpost;
    }

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
