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

package com.google.codeu.servlets;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List; // import just the List interface
import java.util.ArrayList; // import just the ArrayList class

/**
 * Redirects the user to the Google login page or their page if they're already logged in.
 */
@WebServlet("/messagechart")
public class ChartServlet extends HttpServlet {
    private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    // The following line should match however you manipulated getMessages() in Step 1
    List<Message> msgList = datastore.getMessages(null); 
    Gson gson = new Gson();
    String json = gson.toJson(msgList);
    response.getWriter().println(json);
  }


}
