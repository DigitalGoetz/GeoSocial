var map;
var raster;
var borderLayer = null;

var tweetList = new Object();

$(function() {
	map = new L.map('map').setView([ 0.0, 0.0 ], 2);
	var nationalMapUrl = "../map/{z}/{y}/{x}";
	var nationalMapAttribution = "<a href='http:usgs.gov'>USGS</a> National Map Data";
	var nationalMap = new L.TileLayer(nationalMapUrl, {
		maxZoom : 18,
		attribution : nationalMapAttribution
	});
	nationalMap.addTo(map);

	setInterval(getTweets, 10000);

});

function getTweets() {
	$.ajax({
		method : 'get',
		url : '../web3rest/api/tweets',
		success : function(data) {
			updateTweetList(data);
		}
	});
}

function idInList(key, list) {
	for (var i = 0; i < list.length; i++) {
		var id = list[i].id;
		if (id == key) {
			return true;
		}
	}
	return false;
}

function idInObject(key, obj) {
	for (id in obj) {
		if (id == key) {
			return true;
		}
	}
	return false;
}

function updateTweetList(data) {
	// Remove stale Tweets
	for (id in tweetList) {
		if (!idInList(id, data.tweets)) {
			map.removeLayer(tweetList[id]);
		}
	}

	// Include new Tweets
	for (var i = 0; i < data.tweets.length; i++) {

		var tweet = data.tweets[i];

		if (idInObject(tweet.id, tweetList)) {
			continue;
		}

		var lat = tweet.meta.latitude;
		var lon = tweet.meta.longitude;
		var marker = L.marker([ lat, lon ]);
		
		var content = tweet.meta.text + "<br />";
		if (tweet.meta.images != undefined){
			var split = tweet.meta.images.split(',');
			for(var j = 0; j < split.length; j++){
				var line =  "<a href='" + split[j] + "'>" + split[j] +"</a><br />";
				content += line;
			}
		}
		
		marker.bindPopup(content);

		map.addLayer(marker);
		tweetList[tweet.id] = marker;
	}

}
