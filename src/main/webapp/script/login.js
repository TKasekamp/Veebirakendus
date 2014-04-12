var login = {
	objectify : function(t1, t2) {
		return {
			email : t1,
			password : t2
		};
	}
};

function getCookie() {
	var lst = document.cookie.split(";");
	var cookie = "";
	for (var i = 0; i < lst.length; i++) {
		if (lst[i].indexOf("SID") != -1) {
			var temp = lst[i].split("=");
			cookie = temp[1];
		}
	}
	return cookie;
};

$(document).ready(
		function() {
			if (!getCookie()) {
					$("a#login").click(function() {
					loginButtonClicked = true;
					var temp = $("div.login");
					var gLoginButton = $("#g-signin-button");
					if (temp.css("display") == "none") {
						temp.css("display", "inline");
						gLoginButton.css("display", "inline");
					} else {
						temp.css("display", "none");
						gLoginButton.css("display", "none");
					}
				});

				$("#loginarea").click(
					function() {
						var email = $("#email").val();
						var pass = $("#password").val();
						var objekt = login.objectify(email, pass);
						$.ajax('/login/ajax', {
							type : 'POST',
							data : JSON.stringify(objekt),
							success : function(objekt) {
								if (objekt.response == 0
										|| objekt.response == 1
										|| objekt.response == 2) {
									alert("Wrong email / password");
								} else if (objekt.response == 3) {
									location.reload();
								}
							},
							error : function(req, text) {
								console.log("Failed to connect to server");
							}
						});
					}
				);
			}
		});
