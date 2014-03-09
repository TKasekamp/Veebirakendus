
/* Functions */
var vldemo2 = {
    loadAuctionItems: function(itemList) {
        $.ajax('/data', {
            dataType: 'json',
            success: function (itemsJson) {
                console.info('ajax success');
                for (var i = 0; i < itemsJson.length; i++) {
                    itemList.addItem(itemsJson[i]);
    				console.log(itemsJson[i]);
                }
            },
            error: function (req, text) {
                console.error('failed to load auction items: ' + text);
            }
        });
    },

    makeBid: function(view, bidBuilder) {
        var item = view.selectedItem;
        var bid = bidBuilder.buildBid(item);
        $.ajax('/data', {
            type: 'POST',
            data: JSON.stringify(bid), // pack the bid object into json string
            success: function(savedBid) {
                // server returns the bid with its new generated id
                // syncing js&dom is a pain. angularjs may help
                bidBuilder.clear();
                item.bids.push(savedBid);
                view.addBid(savedBid);
            },
            error: function(req, text) {
                console.error('failed to post bid: ' + text);
            }
        });
    }
};

$(function() {

    console.log("running demoapp.js");

    var bidBuilder = new auctions.BidBuilder($('#stuff'));
    var viewingList = new auctions.ViewingList($('#viewing'));
    var itemList = new auctions.BrowsingList($('#items'), viewingList);

    $('#submit1').click(function() {
        vldemo2.makeBid(viewingList, bidBuilder);
    });

    vldemo2.loadAuctionItems(itemList);

});



