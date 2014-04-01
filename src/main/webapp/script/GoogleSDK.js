var gName;
var gId;
var gEmail;
var loginButtonClicked = false;

(function() {
	var po = document.createElement('script');
	po.type = 'text/javascript';
	po.async = true;
	po.src = 'https://apis.google.com/js/client:plusone.js';
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(po, s);
})();

function signinCallback(authResult) {
	if (authResult['status']['signed_in']) {
		if (!getCookie() && loginButtonClicked) {
			function newUser() {
				$(function() {
					var objekt = (function() {
						return {
							username : String(gName),
							email : String(gEmail),
							password : String(gId)
						};
					})();
					$
							.ajax(
									'/signup',
									{
										type : 'POST',
										data : JSON.stringify(objekt),
										success : function(objekt) {
										},
										error : function(req, text) {
											objekt.name = "Uploading failed. Failed to connect to server";
										}
									});
				});
			}

			function login() {
				$(function() {
					var objekt = (function() {
						return {
							email : String(gEmail),
							password : String(gId)
						};
					})();
					$.ajax('/login', {
						type : 'POST',
						data : JSON.stringify(objekt),
						success : function(objekt) {
							if (objekt.response == 0) {
								newUser();
								login();
							} else if (objekt.response == 1
									|| objekt.response == 2) {
								alert("Wrong username / password");
							} else if (objekt.response == 3) {
								console.log("Log in successful.");
								document.cookie = 'SID=' + objekt.SID;
								location.reload();
							}
						},
						error : function(req, text) {
							console.log("Failed to connect to server");
						}
					});
				});
			}

			gapi.client.load('plus', 'v1', function() {
				var request = gapi.client.plus.people.get({
					'userId' : 'me'
				});
				request.execute(function(resp) {
					gName = resp.displayName;
					gId = resp.id;
					console.log('Welcome, ' + gName);
					console.log('User ID: ' + gId);
					gapi.client.load('oauth2', 'v2', function() {
						gapi.client.oauth2.userinfo.get().execute(
								function(resp) {
									gEmail = resp.email;
									console.log(gEmail);
									login();
								})
					});
				});
			});
		}
	} else {
		console.log('Sign-in state: ' + authResult['error']);
	}
};

function gLogout() {
	gapi.auth.signOut();
	gName = null;
	gEmail = null;
	gId = null;
}

function gLoggedIn() {
	gapi.client.load('plus', 'v1', function() {
		var request = gapi.client.plus.people.get({
			'userId' : 'me'
		});
		request.execute(function(resp) {
			if (resp.id != 'undefined') {
				return true;
			}
		});
	});
}
