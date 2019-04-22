/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
 import com.google.appengine.api.datastore.FetchOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */

  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("recipient", message.getRecipient());
    messageEntity.setProperty("sentimentScore",message.getSentimentScore());
    messageEntity.setProperty("imageUrl", message.getImageUrl());
    
    datastore.put(messageEntity);
  }
  /** Stores the User in Datastore. */
 public void storeUser(User user) {
  Entity userEntity = new Entity("User", user.getEmail());
  userEntity.setProperty("email", user.getEmail());
  userEntity.setProperty("aboutme", user.getAboutMe());
  userEntity.setProperty("age", user.getAge());
  userEntity.setProperty("name", user.getName());
  userEntity.setProperty("topics", user.getTopics());
  datastore.put(userEntity);
 }
 
 /**
  * Returns the User owned by the email address, or
  * null if no matching User was found.
  */
 public User getUser(String email) {
  Query query = new Query("User")
    .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
  PreparedQuery results = datastore.prepare(query);
  Entity userEntity = results.asSingleEntity();
  if(userEntity == null) {
   return null;
  }
  
  String aboutme = (String) userEntity.getProperty("aboutme");
  long age = (long) userEntity.getProperty("age"); 
  int age2 = (int) age;
  String name = (String) userEntity.getProperty("name");
  User user = new User(email, name, age2, aboutme);

  ArrayList<String> topic = (ArrayList<String>) userEntity.getProperty("topics");
  if (topic != null){
    for(int i = 0; i < topic.size(); i++){
        user.addTopic(topic.get(i));
    }
  }
  
  
  return user;
 }

  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount(){
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has never posted a
   *     message. List is sorted by time descending.
   */
public List<Message> getMessages(String recipient) {
  List<Message> messages = new ArrayList<>();

  Query query =
      new Query("Message")
          .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
          .addSort("timestamp", SortDirection.DESCENDING);
  PreparedQuery results = datastore.prepare(query);

  for (Entity entity : results.asIterable()) {
    try {
      String idString = entity.getKey().getName();
      UUID id = UUID.fromString(idString);
      String user = (String) entity.getProperty("user");

      String text = (String) entity.getProperty("text");
      long timestamp = (long) entity.getProperty("timestamp");
      //double sentimentScore = (double) entity.getProperty("sentimentScore");
      //System.out.println(sentimentScore);
      //String recipient = (String) entity.getProperty("recipient");
      String imageUrl = (String) entity.getProperty("imageUrl");
      // double sentimentScore;
      //  if (imageUrl == ""){
      //    sentimentScore = (double) entity.getProperty("sentimentScore");
      // } else{ 
      //    sentimentScore = 0;
      // }

      Message message = new Message(id, user, text, timestamp, recipient, 0);
      

      if (imageUrl != null){
        message.setImageUrl(imageUrl);
      }

      messages.add(message);
    } catch (Exception e) {
      System.err.println("Error reading message.");
      System.err.println(entity.toString());
      e.printStackTrace();
    }
  }

  return messages;
  }

/*
returns a list of users that are similar to that of the input email.
*/
public List<User> getUsers(String inputEmail) {
  List<User> users = new ArrayList<>();

  Query query = new Query("User");
  PreparedQuery results = datastore.prepare(query);
  //every user in the program
  for (Entity entity : results.asIterable()) {
    try {
      String email = (String) entity.getProperty("email");
      //just to have it as a parameter in user
      String aboutme = (String) entity.getProperty("aboutme");

      long age2 = (long) entity.getProperty("age"); 
      int age = (int) age2;
      String name = (String) entity.getProperty("name");
  

      if (email.contains(inputEmail) && inputEmail != ""){
        User user = new User(email, name, age, aboutme );
        users.add(user);
      }
    } catch (Exception e) {
      System.err.println("Error reading user.");
      System.err.println(entity.toString());
      e.printStackTrace();
    }
  }
  return users;
  }
}
