package com.example.notes.notetaking.Manager;

public class User {
    private String id;
    private String password;
    private String name;
    private String headPhoto;
    public User(String id,String password,String name,String headPhoto){
        this.id=id;
        this.password=password;
        this.name=name;
        this.headPhoto=headPhoto;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getId(){
        return id;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return password;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setHeadPhoto(String headPhoto){
        this.headPhoto=headPhoto;
    }
    public String getHeadPhoto(){
        return headPhoto;
    }
}
