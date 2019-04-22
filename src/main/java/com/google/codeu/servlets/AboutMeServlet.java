package com.google.codeu.servlets;

import java.io.IOException;
import org.jsoup.*;
import org.jsoup.safety.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
/**
 * Handles fetching and saving user data.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with the "about me" section for a particular user.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();

    String user = userService.getCurrentUser().getEmail();


    if(user == null || user.equals("")) {
      // Request is invalid, return empty response
      response.getWriter().println("invalid user");
      return;
    }

    User userData = datastore.getUser(user);

    // if(userData == null || userData.getAboutMe() == null) {
    //   response.getWriter().println("no user found");
    //   return;
    // }

    //System.out.println("The users name is " + userData.getName());
    Gson gson = new Gson();
    String json = gson.toJson(userData);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String userEmail = userService.getCurrentUser().getEmail();
    String link = request.getParameter("link");
    String name = request.getParameter("name");
    String age2 = request.getParameter("age");
    int age  = Integer.parseInt(age2);
    String aboutme = request.getParameter("aboutme");
    String redirect = request.getParameter("redirect");
    User returnUser = datastore.getUser(userEmail);
    if (returnUser == null){
      returnUser = new User(userEmail, name, age, aboutme); 
    } else{
      returnUser.setAge(age);
      returnUser.setName(name);
      returnUser.setAboutMe(aboutme);
    }

    if (link != null && !returnUser.getTopics().contains(link)){
      returnUser.addTopic(link);
    }
    datastore.storeUser(returnUser);

    response.sendRedirect(redirect);
  }
}