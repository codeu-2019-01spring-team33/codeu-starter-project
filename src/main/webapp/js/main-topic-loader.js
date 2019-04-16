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
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');
if (!parameterUsername) {
  window.location.replace('/');
}

/*
This is going to redirect the user to whatever chatroom topic they wanted
*/

function createPage(){
  var topic = document.getElementById("topic-area").value;
  window.location.replace('topic-page.html?user=' + topic);
}

/**
 * Creates an li element.
 * @param {Element} childElement
 * @return {Element} li element
 */
function createListItem(childElement) {
  const listItemElement = document.createElement('li');
  listItemElement.appendChild(childElement);
  return listItemElement;
}

/**
 * Creates an anchor element.
 * @param {string} url
 * @param {string} text
 * @return {Element} Anchor element
 */
function createLink(url, text) {
  const linkElement = document.createElement('a');
  linkElement.appendChild(document.createTextNode(text));
  linkElement.href = url;
  return linkElement;
}

/*
This will parse the link into returning the actual words that the topic is supposed
to be.
*/
function parseLink(link){
	pageName = link.split("user=")[1];
	topic = pageName.split("Page")[0];
	topic = topic.charAt(0).toUpperCase() + topic.slice(1);
	return topic + " Page";
}

/*
This is going to fetch the about me servlet and return all the topics the user
wants on their home page. It is going to iterate over it, and create li elements with
links to the pages.
*/
function getLinks(){
	url = '/about?user=' + parameterUsername;
	fetch(url)
	    .then((response) => {
	      return response.json();
	    })
	    .then((user) => {
	    	list = document.getElementById("links");
	    	//list.innerHTML = "";
	    	user.topics.forEach((topic) =>{
	    		list.appendChild(createListItem(createLink(topic, parseLink(topic))));
	    	});
	    });
}

/*
what gets called on the onload of the body. 
*/
function buildUI(){
	getLinks();
}