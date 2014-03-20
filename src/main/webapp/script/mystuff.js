var mystuff = {
	objectify : function(){
		return{
			SID : getCookie()
		};
	}
};

function recent(){
	$.ajax('/recent',{
		dataType: 'json',
		data: mystuff.objectify(),
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
	$.ajax('/mystuff', {
		dataType: 'json',
		data: mystuff.objectify(),
		success: function (itemsJson) {
			var tag = $('.down');
			for (var i = 0; i < itemsJson.length; i++) {
				tag.append("<li><a href=\"source.html?id=" + itemsJson[i].codeId + "\">" + itemsJson[i].codeName + "</a></li>");
			}
		},
		error: function (req, text) {
			console.error('failed to load items: ' + text);
		}
	});
	$.ajax('/statistics', {
		dataType: 'json',
		data: mystuff.objectify(),
		success: function (statistics) {
			var tag = $('#statistics');
			var tag2 = $('#statistics2');
			tag.empty();
			tag.append(statistics.name + "s stuff");
			tag2.append("You have: " + statistics.count + " pastes");
		},
		error: function (req, text) {
			console.error('failed to load items: ' + text);
		}
	});
	recent();
};

$(function() {
	if(getCookie()){
		loadCode();
	}
	else{
		var tag = $(".content");
		tag.empty();
		tag.append("<h4>This page is for logged in users only. Please log in to view this page</h4>");
		recent();
	}
});
