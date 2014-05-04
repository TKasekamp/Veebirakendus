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

$(function(){
	returnToPage();
});