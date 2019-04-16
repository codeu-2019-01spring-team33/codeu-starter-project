package com.google.codeu.data;
import java.util.ArrayList;

public class User {

  private String email;
  private String aboutMe;
  private ArrayList<String> topics;
  
  public User(String email, String aboutMe) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.topics = new ArrayList<String>();
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

  public String getAboutMe() {
    return aboutMe;
  }
}