var map;
var raster;
var borderLayer = null;

var tweetList = new Object();

$(function() {
	map = new L.map('map').setView([ 0.0, 0.0 ], 6);
	var nationalMapUrl = "../map/{z}/{y}/{x}";
	var nationalMapAttribution = "<a href='http:usgs.gov'>USGS</a> National Map Data";
	var nationalMap = new L.TileLayer(nationalMapUrl, {
		maxZoom : 18,
		attribution : nationalMapAttribution
	});
	nationalMap.addTo(map);

	setInterval(getTweets, 3000);

});

function getTweets() {
	$.ajax({
		method : 'get',
		url : '../web3rest/api/tweets',
		success : function(data) {
			console.log(data);
			updateTweetList(data);
		},
		error : function(errorData) {
			console.log("blarg " + errorData);
		}

	});
}


function updateTweetList(newTweets){
	
	for( id in tweetList){
		console.log("removing ")
		map.removeLayer(tweetList[id]);
	}
	
	for(var i = 0; i < newTweets.length; i++){
		var tweet = newTweets[i];
		var marker = L.marker([tweet.latitude,tweet.longitude]);
		map.addLayer(marker);
		tweetList[tweet.id] = marker;
	}
	


}
