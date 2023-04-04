package com.vuhoangtrucvy20110415.week9_insertdatafirebase;

public class Member {
    private String Name;
    private Integer Age;
    private Long ph;
    private Float ht;

    public Member() {

    }

    public void setName(String name) {
        Name = name;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public void setPh(Long ph) {
        this.ph = ph;
    }

    public void setHt(Float ht) {
        this.ht = ht;
    }

    public String getName() {
        return Name;
    }

    public Integer getAge() {
        return Age;
    }

    public Long getPh() {
        return ph;
    }

    public Float getHt() {
        return ht;
    }
}