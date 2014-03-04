$(document).ready(function () {
    $("a#login").click(function () {
        var temp = $("div.login");
        if (temp.css("visibility") == "hidden") {
            temp.css("visibility", "visible");
        }
        else {
            temp.css("visibility", "hidden");
        }
    });
});

