var loginButtonClicked = false;
var userinfo;

(function() {
	var po = document.createElement('script');
	po.type = 'text/javascript';
	po.async = true;
	po.src = 'https://apis.google.com/js/client:plusone.js';
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(po, s);
})();

function gLogin() {
	$(function() {
		$.ajax('/login/google', {
			type : 'POST',
			data : JSON.stringify(userinfo),
			success : function(userinfo) {
				console.log("Log in successful.");
				location.reload();
			},
			error : function(req, text) {
				console.log("Failed to connect to server");
			}
		});
	});
}

function signinCallback(authResult) {
	if (authResult['status']['signed_in']) {
		if (!getCookie() && loginButtonClicked) {

			gapi.client.load('plus', 'v1', function() {
				var request = gapi.client.plus.people.get({
					'userId' : 'me'
				});
				request.execute(function(resp) {
					gapi.client.load('oauth2', 'v2', function() {
						gapi.client.oauth2.userinfo.get().execute(
								function(resp) {
									userinfo = resp;
									userinfo.password = "";
									userinfo.username = userinfo.name;
									delete userinfo.name;
									delete userinfo.result;
									gLogin();
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