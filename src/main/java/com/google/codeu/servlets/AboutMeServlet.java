package com.google.codeu.servlets;

import java.io.IOException;
import org.jsoup.*;
import org.jsoup.safety.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.*;
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

    String user = request.getParameter("user");

    if(user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    User userData = datastore.getUser(user);

    if(userData == null || userData.getAboutMe() == null) {
      return;
    }

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
    System.out.println(request.getParameter("link"));
    String link = request.getParameter("link");
    System.out.println(request.getParameter("name"));
    System.out.println(request.getParameter("age"));
    System.out.println(request.getParameter("aboutme"));
    //User returnUser = datastore.getUser(userEmail);
    String name = request.getParameter("name");
    int age = Integer.parseInt(request.getParameter("age"));
    String aboutme = request.getParameter("aboutme");
    User returnUser = datastore.getUser(userEmail);
    if (returnUser == null){
      returnUser = new User(userEmail, name, age, aboutme); 
    } else{
      returnUser.setAge(age);
      returnUser.setName(name);
      returnUser.setAboutMe(aboutme);
    }

    // if (!returnUser.getTopics().contains(link)){
    //   returnUser.addTopic(link);
    // }
    datastore.storeUser(returnUser);

    response.sendRedirect("/");
  }
}