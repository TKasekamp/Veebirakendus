function cacheCode(){
	var title = $('#codename').val();
	var text = $('#codearea').val();
	localStorage.setItem('title', title);
	localStorage.setItem('text', text);
	return null;
}

function checkCodeCache(){
	if (location.pathname == "/index.html"){
		window.onbeforeunload = cacheCode;
		if(localStorage.getItem('text') || localStorage.getItem('title')) {
			$('#codename').val(localStorage.getItem('title'));
			$('#codearea').val(localStorage.getItem('text'));
		}
	}
}

$(function() {
	checkCodeCache();
	
	var bodyHtml = document.getElementsByTagName("body")[0].innerHTML;
	var path = location.pathname;
	localStorage.setItem(path, bodyHtml);
	console.log(path+" saved to cache.");
});