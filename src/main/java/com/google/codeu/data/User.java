package com.google.codeu.data;
import java.util.ArrayList;

public class User {

  private String email;
  private String name;
  private ArrayList<String> topics;
  private int age;
  private String aboutme;

  
  public User(String email, String name, int age, String aboutme) {
    this.email = email;
    this.topics = new ArrayList<String>();
    this.name = name;
    this.age=age;
    this.aboutme = aboutme;
  }

  public void addTopic(String link){
    this.topics.add(link);
  }
  
  public ArrayList<String> getTopics(){
    return this.topics;
  }

  public String getEmail(){
    return email;
  }

  public int getAge(){
    return age;
  }

  public void setAge(int other_age){
    age = other_age;
  }

  public String getName(){
    return name;
  }

  public String getAboutMe(){
    return aboutme;
  }

  public void setAboutMe(String other_aboutme){
    aboutme = other_aboutme;
  }

  public void setName(String other_name){
    name = other_name;
  }

}