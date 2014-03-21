window.fbAsyncInit = function() {
	FB.init({
		appId : '221920274674013',
		channelUrl : 'https://codepump2.herokuapp.com/',
		status : true, // check login status
		cookie : true,
		xfbml : true,
		oauth : true
	});

	FB.Event.subscribe('auth.authResponseChange', function(response) {
		if (response.status === 'connected') {
			testAPI();
		} else if (response.status === 'not_authorized') {
			FB.login();
		} else {
			FB.login();
		}
	});
};

function testAPI() {
	console.log('Welcome!  Fetching your information.... ');
	FB.api('/me', function(response) {
		console.log('Good to see you, ' + response.name + '.');
	});
};

(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id))
		return;
	js = d.createElement(s);
	js.id = id;
	js.src = "https://connect.facebook.net/en_US/all.js#xfbml=1&appId=221920274674013";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function fbLogout() {
	FB.logout(function() {
		document.location.reload();
	});
}

function fbLoginStatus() {
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			console.log('User is logged in.');
			var uid = response.authResponse.userID;
			var accessToken = response.authResponse.accessToken;
		} else if (response.status === 'not_authorized') {
			console.log("User is logged in, but hasn't authorized the app.");
		} else {
			console.log('User is not logged in.');
		}
	});
}