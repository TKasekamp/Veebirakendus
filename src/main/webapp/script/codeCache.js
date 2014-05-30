window.onbeforeunload = cacheCode;

function cacheCode(){
	var title = $('#codename').val();
	var text = $('#codearea').val();
	localStorage.setItem('title', title);
	localStorage.setItem('text', text);
	return null;
}

function checkCodeCache(){
	if(localStorage.getItem('text') || localStorage.getItem('title')) {
		$('#codename').val(localStorage.getItem('title'));
		$('#codearea').val(localStorage.getItem('text'));
	}
}

$(document).ready(
		function() {
			checkCodeCache();
		}
);