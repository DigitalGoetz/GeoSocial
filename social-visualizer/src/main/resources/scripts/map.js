/**
 * 
 */

var map;
var raster;
var borderLayer = null;

$(function() {
	map = L.map('map').setView([ 51.505, -0.09 ], 6);
	raster = L.tileLayer.wms("http://localhost:7080/geoserver/semanticgis/wms",
			{
				layers : 'semanticgis:NE2_50M_SR_W',
				format : 'image/png',
				transparent : true,
				version : '1.1.0',
				attribution : "natural earth"
			});
	raster.addTo(map);

	map
			.on(
					'moveend',
					function() {
						var bounds = map.getBounds();
						console.log(bounds);
						var url = "http://localhost:7080/geoserver/semanticgis/ows?";
						url += "service=WFS&version=1.0.0&request=GetFeature&typeName=semanticgis:admin0&maxFeatures=250&outputFormat=application%2Fjson";
						url += "&srsName=urn:ogc:def:crs:EPSG::4326";
						url += "&bbox=";

						var neLat = bounds._northEast.lat;
						var neLng = bounds._northEast.lng;
						var swLat = bounds._southWest.lat;
						var swLng = bounds._southWest.lng;

						var minLat = swLat < neLat ? swLat : neLat;
						var maxLat = swLat <= neLat ? neLat : swLat;
						var minLng = swLng < neLng ? swLng : neLng;
						var maxLng = swLng <= neLng ? neLng : swLng;

						var coords = minLng + "," + minLat + "," + maxLng + ","
								+ maxLat;
						url += coords;

						console.log(url);
						$.ajax({
							url : url,
							method : "get",
							success : function(data) {
								console.log("Features obtained");
								loadFeatures(data);
							},
							error : function() {
								alert('gar');
							}
						});
					})

});

function loadFeatures(geoJson) {
	if (borderLayer != null) {
		map.removeLayer(borderLayer);
	}
	borderLayer = L.geoJson(geoJson);
	borderLayer.addTo(map);
}

function parseBounds(bounds) {
	var ne = bounds._northEast;
	var sw = bounds._southWest;

	var obj = {};
	obj.neLat = ne.lat;
	obj.neLng = ne.lng;
	obj.swLat = sw.lat;
	obj.swLng = sw.lng;

	return obj;
}