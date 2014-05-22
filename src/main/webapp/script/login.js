var login = {
	objectify : function(t1, t2) {
		return {
			email : t1,
			password : t2
		};
	}
};

function getCookie(cname){
	var name = cname + '=';
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++){
		var c = ca[i].trim();
		if (c.indexOf(name)==0) 
			return c.substring(name.length,c.length);
	}
	return '';
}

$(document).ready(
		function() {
			if (!getCookie('SID')) {
				$("a#login").click(function() {
					loginButtonClicked = true;
					var temp = $("div.login");
					if (temp.css("display") == "none") {
						temp.css("display", "inline");
					} else {
						temp.css("display", "none");
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
				
				$("#gLoginButton").click(
						function() {
							document.cookie = 'redirectFrom='+window.location.href+'; path=/';
							window.location.href = "/GoogleLogin.jsp";
						}
					);
				$(".sup").click(
						function() {
							window.location.href = "/surprise.html";
						}
					);
			}
		});