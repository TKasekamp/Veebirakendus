// http://www.prideparrot.com/blog/archive/2011/9/how_to_display_dates_and_times_in_clients_timezone
// https://bitbucket.org/pellepim/jstimezonedetect/overview
function setTimezoneCookie(){
 
  	var timezone_cookie = "codepump_timezone";
 
    // if the timezone cookie not exists create one.
    if (!getCookie(timezone_cookie)) { 
		   var tz = jstz.determine(); // Determines the time zone of the browser client
  			var c = tz.name();          
            // create a new cookie 
			 document.cookie = timezone_cookie + "=" + c;
            // re-load the page
            location.reload(); 
        
    }
    // if the current timezone and the one stored in cookie are different
    // then store the new timezone in the cookie and refresh the page.
    else {         
 
        var storedOffset = getCookie(timezone_cookie);
        var currentOffset = jstz.determine().name();
 
        // user may have changed the timezone
        if (storedOffset !== currentOffset) { 
            document.cookie = timezone_cookie + "=" + currentOffset;
            location.reload();
        }
    }
}

function getCookie(name) {
  var value = "; " + document.cookie;
  var parts = value.split("; " + name + "=");
  if (parts.length == 2) return parts.pop().split(";").shift();
}

$(document).ready(
function(){
    setTimezoneCookie();
});