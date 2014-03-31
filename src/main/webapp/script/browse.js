// Websocket function. SANDER MUST MAEK BETTER. NAO!
function createWebsocket() {
	var socketAddr = window.location.origin.replace("https", "ws") + "/feed";
    var websocket = new WebSocket(socketAddr);
    websocket.onopen = function() { console.log("socket up!"); };
    websocket.onclose = function() { console.log("socket closed!"); };
   	websocket.onmessage = function(event) {
        console.log("ws received " + event.data);
        var items = JSON.parse(event.data);
		var tag = $("#recent");
		tag.empty();
		for(var i = 0; i < items.length; i++){
			tag.append("<p><a href=\"source.html?id=" + items[i].codeID + "\">" + items[i].codeName + " - " + items[i].createDate + "</a></p>");
		}
        };
    };

$(function() {
	createWebsocket();
});