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

// Get ?user=XYZ parameter value

function buildUI() {
	showAboutMeFormIfLoggedIn();
	setFormAction();
}

function showAboutMeFormIfLoggedIn() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn) {
          checkIfExists();
          document.getElementById('user-prof').style.visibility="visible";
        } else {
        	document.getElementById('user-prof').style.visibility="hidden";
        }
      });
}

function checkIfExists(){
	url = '/about?';
	fetch(url)
	    .then((response) => {
	      return response.json();
	    })
	    .then((user) => {
	    	if (user == null){
	    		const params = new URLSearchParams();
	    		params.append('name', '');
	    		params.append('age', 0);
	    		params.append('aboutme', '');
	    		params.append('redirect', window.location.href);
	    		fetch('/about', {
	    			method: 'POST',
	    			body: params
	    		});
	    	}
	    });
}


function setFormAction(){
	document.getElementById("user-prof").action="/about?redirect=" + window.location.href;
}