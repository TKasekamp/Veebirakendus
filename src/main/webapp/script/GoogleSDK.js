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
				if (text=="no user"){
					passwordPrompt();
				} else {
					console.log("Failed to connect to server");
				}
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
									delete resp.result;
									resp.password = "";
									userinfo = resp;
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

// password prompt

$(function() {
	var password1 = $("#password1"), password2 = $("#password2"), allFields = $(
			[]).add(password1).add(password2), message = $(".message");

	function updateMessage(mes) {
		message.text(mes).addClass("ui-state-highlight");
		setTimeout(function() {
			message.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(pass, min, max) {
		if (pass.val().length > max || pass.val().length < min) {
			pass.addClass("ui-state-error");
			updateMessage("Length of password must be between " + min + " and "
					+ max + ".");
			return false;
		} else {
			return true;
		}
	}

	function checkEquivalence(p1, p2) {
		if (p1.val() == p2.val()) {
			return true;
		} else {
			p2.addClass("ui-state-error");
			updateMessage("Confirmed password is wrong.");
			return false;
		}
	}

	$("#passwordPrompt").dialog(
			{
				autoOpen : false,
				height : 300,
				width : 350,
				modal : true,
				buttons : {
					"Create an account" : function() {
						var passValid = true;
						allFields.removeClass("ui-state-error");
						passValid = passValid && checkLength(password1, 5, 20);
						passValid = passValid
								&& checkEquivalence(password1, password2);
						if (passValid) {
							userinfo.password = password1.val();
							gLogin();
							$(this).dialog("close");
						}
					},
					Cancel : function() {
						$(this).dialog("close");
					}
				},
				close : function() {
					updateMessage("Please type a password for your account.");
					allFields.val("").removeClass("ui-state-error");
				}
			});
});

function passwordPrompt() {
	$("#passwordPrompt").dialog("open");
}