function recent(){
	$.ajax('/recent',{
		dataType: 'json',
		success: function(items){
			var tag = $("#recent");
			for(var i = 0; i < items.length; i++){
				tag.append("<p><a href=\"source.html?id=" + items[i].codeID + "\">" + items[i].codeName + " - " + items[i].createDate + "</a></p>");
			}
		},
		error: function(req,text){
			console.error("failed to load items: " + text);
		}
	});
};

function loadCode(){
	$.ajax('/data', {
		dataType: 'json',
		success: function (itemsJson) {
			var tag = $('.down');
			for (var i = 0; i < itemsJson.length; i++) {
				tag.append("<li><a href=\"source.html?id=" + itemsJson[i].id + "\">" + itemsJson[i].name + "</a></li>");
			}
		},
		error: function (req, text) {
			console.error('failed to load items: ' + text);
		}
	});
	//recent(); not activated here
};

	// Websocket function. SANDER MUST MAEK BETTER. NAO!
    function createWebsocket() {
        var socketAddr = window.location.origin.replace("http", "ws") + "/feed";
        var websocket = new WebSocket(socketAddr);
        websocket.onopen = function() { console.log("socket up!"); };
        websocket.onclose = function() { console.log("socket closed!"); };
        websocket.onmessage = function(event) {
            console.log("ws received " + event.data);
            var items = JSON.parse(event.data);
			var tag = $("#recent");
			for(var i = 0; i < items.length; i++){
				tag.append("<p><a href=\"source.html?id=" + items[i].codeID + "\">" + items[i].codeName + " - " + items[i].createDate + "</a></p>");
			}

  
        };
    };

$(function() {
	loadCode();
	createWebsocket();
});
