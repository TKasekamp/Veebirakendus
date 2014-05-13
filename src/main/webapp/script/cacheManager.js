var appCache = window.applicationCache;

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

function checkForManifestUpdate(){
	var manifestExpireTime = localStorage.getItem('manifestExpireTime');
	var currentDate = new Date();
	var currentTime = currentDate.getTime();
	if (manifestExpireTime==null || manifestExpireTime<currentTime){
		window.location.href = "/updateappcache.html";
	}
}

function updateCache(){
	if (appCache.status == appCache.IDLE || appCache.status == appCache.UPDATEREADY){
		if(appCache.status == appCache.UPDATEREADY){
			appCache.swapCache();
		}
		var currentDate = new Date();
		var currentTime = currentDate.getTime();
		var manifestExpireTime = currentTime+(1*24*60*60*1000); // 1 day
		localStorage.setItem('manifestExpireTime', manifestExpireTime);
		console.log("App cache updated.");
		window.location.href = "/";
	} else {
		setInterval(function(){updateCache()},1000);
	}
}

$(function CacheManager() {
	if (location.pathname == "/updateappcache.html"){
		updateCache();
	} else {
		checkCodeCache();
		checkForManifestUpdate();
	
		var bodyHtml = document.body.innerHTML;
		var path = location.pathname;
		localStorage.setItem(path, bodyHtml);
		console.log(path+" saved to cache.");
	}
});