var login = {
	objectify : function(t1,t2){
		return{
			username : t1,
			password : t2
		};
	}
};

function getCookie(){
	var lst = document.cookie.split("=");
	return lst[1];
};

function objectify(t1){
	return {
		SID : t1
	};
};

$(document).ready(function () {
	if(getCookie().length < 32){
		var button = $("a#login");
		button.html("Log Out");
		button.click(function() {
			$.ajax('/logout', {
				type: 'POST',
				data: JSON.stringify(objectify(getCookie())), // pack the bid object into json string
				success: function(objekt) {
					document.cookie = 'SID=';
					location.reload();
				},
				error: function(req, text) {
					alert("Failed to log out, try again");
					console.log("Failed to connect to server");
				}
			});
			
		});
		
	}
	else{
		$("a#login").click(function () {
			var temp = $("div.login");
			if (temp.css("visibility") == "hidden") {
				temp.css("visibility", "visible");
			}
			else {
				temp.css("visibility", "hidden");
			}
		});
		
		$("#loginarea").click(function (){
			var user = $("#username").val();
			var pass = $("#password").val();
			var objekt = login.objectify(user,pass);
			$.ajax('/login', {
				type: 'POST',
				data: JSON.stringify(objekt), // pack the bid object into json string
				success: function(objekt) {
					if(objekt.response == 0 || objekt.response == 1 || objekt.response == 2){
						alert("Wrong username / password");
					}
					else if(objekt.response == 3){
						document.cookie = 'SID=' + objekt.SID;
						location.reload();
					}
				},
				error: function(req, text) {
					console.log("Failed to connect to server");
				}
			});
		});
	}
});

