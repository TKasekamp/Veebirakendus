function returnToPage(){
	var returnTo = getCookie('redirectFrom');
	if (returnTo != ''){
		window.location.href = returnTo;
		document.cookie = 'redirectFrom=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';
	} else if (window.location.href.indexOf("localhost") > -1)
		window.location.href = "http://localhost:8080/";
	else
		window.location.href = "http://codepump2.herokuapp.com/";
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

$(function(){
	returnToPage();
});