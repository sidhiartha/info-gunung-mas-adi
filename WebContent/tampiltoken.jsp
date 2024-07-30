<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>

<head>
<%
	String input = (String) request.getAttribute("input");
	String output = (String) request.getAttribute("output");
	String cuaca = (String) request.getAttribute("cuaca");
	output += cuaca;
	Object maps = request.getAttribute("maps");
	Object pmId = request.getAttribute("pmId");
%>

<link rel="stylesheet" type="text/css" href="css/in.css">
<link rel="stylesheet" type="text/css" href="css/out.css">
<link rel="stylesheet" type="text/css" href="css/footer.css">
<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css">
<link rel="stylesheet" href="js/lib/jqueryui/themes/smoothness/jquery-ui.css">
<script type="text/javascript" src="js/lib/jquery/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="js/lib/jqueryui/jquery-ui.min-1.11.1.js"></script>
<script type="text/javascript" src="js/lib/jsapi.js"></script>
<script type="text/javascript" src="js/lib/jsapi_key.js"></script>
<script type="text/javascript" src="js/lib/geoxml3.js"></script>
<script type="text/javascript" src="js/lib/ProjectedOverlay.js"></script>
<script type="text/javascript" src="js/parser/parseKML.js"></script>
<script type="text/javascript" src="js/lib/gb.js"></script>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyB_SyYcKLUyka_tRY6IK2_UWsC7AIcQNu8&sensor=false"> </script>
<!-- <link rel="shortcut icon" href="images/gunungbeta.ico" /> -->

<title>Gunung Beta</title>
</head>

<body>
	<!-- start header -->
	<div id="header">
		<img id="logo" src="images/headericon.png" />
	</div>
	<!-- end header -->

	<!-- start page -->
	<div id="page">
		<div class="search-box">
			<form action="token" method="get" id="calculate" class="index">
				<fieldset class="search-field">
					<input name="i" id="search" maxlength="200" type="text"
						placeholder="Pencarian..." onkeyup="showState(this.value)"/> <input
						type="submit" title="proses" value=""
						style="display: none" /> <a
						href="bantuan.jsp"> <img src="images/help-512.png"
						style="width: 22px; height: 22px; border: 0px; float: right; margin-top: 5px;">
					</a>
				</fieldset>
			</form>
		</div>

		<!-- start content -->
		<div class="content-box" style="width: 100%;">

			<script type="text/javascript">
				$(function() {
					$( "#accordion" ).accordion({
						heightStyle: "content"
					});
					
					var kmlFiles = <%=maps%>;
					if(kmlFiles == '' || kmlFiles == null) {
						$('#content').html('');
						$('#sidebar').html('');
					}
				});
			</script>

			<!-- tampil informasi disini -->
			<div id="accordion" style="margin-bottom: 20px;">
				<h3>Input : <b>"<%=input%>" </b></h3>
				<div style="max-height: 150px; min-height: 0px">
					<p>
						<%=output%>
					</p>
				</div>
			</div>

			<br /> <br />
			<div id="content">
				<div class="post">
					<div id="map_canvas"></div>
				</div>
			</div>
			<!-- end content -->
			<!-- start sidebar -->
			<div id="sidebar">
				<ul>
					<li>
						<h2>Legenda</h2>
						<div id="side-legend"></div>
					</li>
					<li style="margin-top: -25px;">
						<h2>Detail</h2> <a href="javascript:kmlShowAll();" id="show_all">(show
							all)</a>
						<div id="side-mark" style="margin-bottom: -20px;"></div>
					</li>
				</ul>
			</div>
			<!-- end sidebar -->
		</div>
	</div>
	<!-- end page -->
	
	<!-- start footer -->
	<jsp:include page="footer.html"></jsp:include>
	<!-- end footer -->

	<script type="text/javascript">
		var geoXMLDoc = null;
		var pmIdAll = <%=pmId%>;
		var kmlFiles =<%=maps%>;
		var kml = remArrayKml(kmlFiles);
		var pmId = remArraypmId(pmIdAll);
 		//alert("file kml yang di load : " + kml);
 		//alert("pmId yang di load : " + pmId);
		
		var highlightLineOptions = {
			fillColor : "#FFFF00",
			strokeColor : "#FF0000",
			fillOpacity : 0.9,
			strokeWidth : 10
		};

		/*hapus kml yang sama*/
		function remArrayKml(kmlFiles) {
			temp_kmlFiles = {};
			for (var i = 0; i < kmlFiles.length; i++) {
				temp_kmlFiles[kmlFiles[i]] = kmlFiles[i];
			}
			new_kmlFiles = [];
			for ( var key in temp_kmlFiles) {
				new_kmlFiles.push(key);
			}
			return new_kmlFiles;
		}
		
		/*hapus pmid yang sama*/
		function remArraypmId(pmIdAll) {
			temp_pmIdAll = {};
			for (var i = 0; i < pmIdAll.length; i++) {
				temp_pmIdAll[pmIdAll[i]] = pmIdAll[i];
			}
			new_pmIdAll = [];
			for ( var key in temp_pmIdAll) {
				new_pmIdAll.push(key);
			}
			return new_pmIdAll;
		}

		loadKML('map_canvas', 'elevation_chart', kml, useTheData);

		function useTheData(doc) {
			geoXMLDoc = doc;
			for (var j = 0; j < geoXMLDoc.length; j++) {
				for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
					// Mouseover Highlight Styling for Polyline (Jalur Pendakian)
					if (geoXMLDoc[j].placemarks[i].polyline) {
						var normalStyle = {
							strokeColor : geoXMLDoc[j].placemarks[i].polyline
									.get('strokeColor'),
							strokeWeight : geoXMLDoc[j].placemarks[i].polyline
									.get('strokeWeight'),
							strokeOpacity : geoXMLDoc[j].placemarks[i].polyline
									.get('strokeOpacity')
						};
						geoXMLDoc[j].placemarks[i].polyline.normalStyle = normalStyle;
						highlightPoly(geoXMLDoc[j].placemarks[i].polyline, j, i);
					}
				}
			}
			buildSideMark();
			google.maps.event.addListener(map, "bounds_changed", buildSideMark);
			google.maps.event.addListener(map, "center_changed", buildSideMark);
			google.maps.event.addListener(map, "zoom_changed", buildSideMark);
			cekPm(pmId);
		}

		function highlightPoly(poly, kml, placemark) {
			google.maps.event.addListener(poly, "mouseover", function() {
				var rowElem = document.getElementById('row' + kml + placemark);
				if (rowElem)
					rowElem.style.backgroundColor = "#FFFA5E";
				poly.setOptions(highlightLineOptions);
			});
			google.maps.event.addListener(poly, "mouseout", function() {
				var rowElem = document.getElementById('row' + kml + placemark);
				if (rowElem)
					rowElem.style.backgroundColor = "#FFFFFF";
				poly.setOptions(poly.normalStyle);
			});
		}

		function kmlHighlightPoly(kml, placemark) {
			geoXMLDoc[kml].placemarks[placemark].polyline
					.setOptions(highlightLineOptions);
		}

		function kmlUnHighlightPoly(kml, placemark) {
			geoXMLDoc[kml].placemarks[placemark].polyline
					.setOptions(geoXMLDoc[kml].placemarks[placemark].polyline.normalStyle);
		}

		var rad = function(x) {
			return x * Math.PI / 180;
		};

		function getKoordinat(latlng) {
			var ll = latlng.toString();
			var k = ll.substring(1, ll.indexOf(")"));
			var koordinat = k.split(", ");
			return koordinat;
		}

		function getJarak(koordinat1, koordinat2) {
			// Rumus Haversine
			var R = 6378137; // Earth mean radius in meter
			var dLat = rad(koordinat2[0] - koordinat1[0]);
			var dLong = rad(koordinat2[1] - koordinat1[1]);
			var a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
					+ Math.cos(rad(koordinat1[0]))
					* Math.cos(rad(koordinat2[0])) * Math.sin(dLong / 2)
					* Math.sin(dLong / 2);
			var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			var d = R * c;
			return d; // returns the distance in meter
		}

		function getPanjangJalur(kml, jalur) {
			var pjalur = geoXMLDoc[kml].placemarks[jalur].polyline.getPath()
					.getArray();
			var jarakTotal = 0;
			for (var i = 0; i < pjalur.length; i++) {
				koordinat1 = getKoordinat(pjalur[i]);
				koordinat2 = getKoordinat(pjalur[i + 1]);
				jarakTotal += getJarak(koordinat1, koordinat2);
				if (i == (pjalur.length - 2))
					break;
			}
			return jarakTotal;
		}

		function kmlClick(kml, placemark) {
			if (geoXMLDoc[kml].placemarks[placemark].polyline) {
				var center = geoXMLDoc[kml].placemarks[placemark].polyline
						.getPath().getArray();
				var n = Math.round((center.length / 2));
				infowindow.setContent('<div><strong>'
						+ geoXMLDoc[kml].placemarks[placemark].name
						+ '</strong><br><br><strong>' + "Estimasi Jarak : "
						+ '</strong>'
						+ Math.round(getPanjangJalur(kml, placemark))
						+ " meter" + '<br><strong><br>' + "Keterangan : "
						+ '</strong>'
						+ geoXMLDoc[kml].placemarks[placemark].description
						+ '</div>');
				infowindow.setPosition(center[n]);
				infowindow.open(map);
			} else {
				geoXMLDoc[kml].placemarks[placemark].marker.setAnimation(google.maps.Animation.DROP);
				map.setCenter(geoXMLDoc[kml].placemarks[placemark].marker
						.getPosition());
				infowindow
						.setContent('<div><strong>'
								+ geoXMLDoc[kml].placemarks[placemark].name
								+ '</strong><br><br><strong>'
								+ "Koordinat (latitude, longitude) : "
								+ '</strong>'
								+ geoXMLDoc[kml].placemarks[placemark].latlng
								+ '<br><strong><br>'
								+ "Ketinggian : "
								+ '</strong>'
								+ geoXMLDoc[kml].placemarks[placemark].Point.coordinates[0].alt
								+ " mdpl"
								+ '<br><strong><br>'
								+ "Keterangan : "
								+ '<br>'
								+ '</strong>'
								+ geoXMLDoc[kml].placemarks[placemark].description
								+ '</div>');
				infowindow.open(map,
						geoXMLDoc[kml].placemarks[placemark].marker);
			}
		}

		function kmlShowPlacemark(kml, placemark) {
			if (geoXMLDoc[kml].placemarks[placemark].polyline)
				map
						.fitBounds(geoXMLDoc[kml].placemarks[placemark].polyline.bounds);
			else
				map.setCenter(geoXMLDoc[kml].placemarks[placemark].marker
						.getPosition());
			for (var j = 0; j < geoXMLDoc.length; j++) {
				for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
					var placemark = geoXmlDoc.placemarks[i];
					if (kml == j && placemark == i) {
						if (geoXMLDoc[kml].placemarks[placemark].polyline)
							geoXMLDoc[kml].placemarks[placemark].polyline
									.setMap(map);
						if (geoXMLDoc[kml].placemarks[placemark].marker)
							geoXMLDoc[kml].placemarks[placemark].marker
									.setMap(map);
					} else {
						if (geoXMLDoc[kml].placemarks[placemark].polyline)
							geoXMLDoc[kml].placemarks[placemark].polyline
									.setMap(null);
						if (geoXMLDoc[kml].placemarks[placemark].marker)
							geoXMLDoc[kml].placemarks[placemark].marker
									.setMap(null);
					}
				}
			}
		}

		function kmlShowAll() {
			var currentBounds = new google.maps.LatLngBounds();
			for (var j = 0; j < geoXMLDoc.length; j++) {
				for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
					if (geoXMLDoc[j].placemarks[i].polyline)
						geoXMLDoc[j].placemarks[i].polyline.setMap(map);
					else {
						geoXMLDoc[j].placemarks[i].marker.setMap(map);
						currentBounds.extend(geoXMLDoc[j].placemarks[i].marker
								.getPosition());
					}
				}
			}
			map.fitBounds(currentBounds);
			infowindow.close();
		}

		function show(category) {
			for (var j = 0; j < geoXMLDoc.length; j++) {
				for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
					if (geoXMLDoc[j].placemarks[i].styleUrl == category) {
						if (geoXMLDoc[j].placemarks[i].polyline)
							geoXMLDoc[j].placemarks[i].polyline.setMap(map);
						else
							geoXMLDoc[j].placemarks[i].marker.setMap(map);
					}
				}
			}
		}

		function hide(category) {
			for (var j = 0; j < geoXMLDoc.length; j++) {
				for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
					if (geoXMLDoc[j].placemarks[i].styleUrl == category) {
						if (geoXMLDoc[j].placemarks[i].polyline)
							geoXMLDoc[j].placemarks[i].polyline.setMap(null);
						else
							geoXMLDoc[j].placemarks[i].marker.setMap(null);
					}
				}
			}
			infowindow.close();
		}

		function legendClick(box, category) {
			if (box.checked)
				show(category);
			else
				hide(category);
		}

		function buildSideMark() {
			var currentBounds = map.getBounds();
			if (!currentBounds)
				var currentBounds = new google.maps.LatLngBounds();
			if (geoXMLDoc) {
				var sideMark = '<table id="mark_table">';
				for (var j = 0; j < geoXMLDoc.length; j++) {
					for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
						if (!categories[geoXMLDoc[j].placemarks[i].styleUrl]) {
							var styleName = geoXMLDoc[j].placemarks[i].styleUrl;
							styleName.replace(" ", "_");
							categories[geoXMLDoc[j].placemarks[i].styleUrl] = {
								name : styleName,
								style : geoXMLDoc[j].placemarks[i].style,
								styleUrl : geoXMLDoc[j].placemarks[i].styleUrl
							};
						}
						if (geoXMLDoc[j].placemarks[i].polyline) {
							if (currentBounds
									.intersects(geoXMLDoc[j].placemarks[i].polyline.bounds)) {
								sideMark += '<tr id="row' + j + i
										+ '" onmouseover="kmlHighlightPoly('
										+ j + ',' + i
										+ ')" onmouseout="kmlUnHighlightPoly('
										+ j + ',' + i + ')">';
								sideMark += '<td><img src='+geoXMLDoc[j].placemarks[i].style.href+' height="15" width="15" alt="icon" /></td>';
								sideMark += '<td><a href="javascript:kmlClick('
										+ j + ',' + i + ');">'
										+ geoXMLDoc[j].placemarks[i].name
										+ '</a></td>';
								sideMark += '<td><a href="javascript:kmlShowPlacemark('
										+ j + ',' + i + ');">(+)</a></td>';
								sideMark += '</tr>';
							}
						} else {
							if (currentBounds
									.contains(geoXMLDoc[j].placemarks[i].marker
											.getPosition())) {
								sideMark += '<tr id="row'+j+i+'" style="border-bottom: 1px dashed #969696;">';
								sideMark += '<td><img src='+geoXMLDoc[j].placemarks[i].style.href+' height="15" width="15" alt="icon" /></td>';
								sideMark += '<td><a href="javascript:kmlClick('
										+ j + ',' + i + ');">'
										+ geoXMLDoc[j].placemarks[i].name
										+ '</a></td>';
								sideMark += '<td></td>';
								sideMark += '</tr>';
							}
						}
					}
				}
				sideMark += '</table>';

				legend = '<table style="color:#808080; font-size:13px;" id="legend_table" cellspacing="0" cellpadding="0">';
				for (id in categories) {
					legend += '<tr style="border-bottom: 1px solid black;">';
					legend += '<td><input type="checkbox" checked="checked" onclick="legendClick(this,&quot;'
							+ categories[id].name
							+ '&quot;)" id="'
							+ id
							+ '_box" /></td>';
					legend += '<td>';
					legend += '<img src='+categories[id].style.href+' height="15" width="15" alt="icon" /></td>'
					legend += '<td>' + id.substr(1) + '</td>';
					legend += '</tr>';
				}
				legend += '</table>';
				document.getElementById("side-legend").innerHTML = legend;
				document.getElementById("side-mark").innerHTML = sideMark;
			}
		}

		function cekPm(pmId) {
			if (pmId.length == 1) {
// 				alert("single pmId");
				singleExt(pmId);
			} else if (pmId.length > 1) {
// 				alert("multi pmId");
				multiExt(pmId);
			}
		}

		/* fungsi single pm */
		function singleExt(pm) {
			for (var j = 0; j < geoXMLDoc.length; j++) {
				for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
					if (pm == geoXMLDoc[j].placemarks[i].extdata["pmId"]) {
						//alert(geoXMLDoc[j].placemarks[i].name);
						if (geoXMLDoc[j].placemarks[i].marker) {
							//alert(geoXMLDoc[j].placemarks[i].name);
							map.setCenter(geoXMLDoc[j].placemarks[i].marker
									.getPosition());
							infowindow
									.setContent('<div><strong>'
											+ geoXMLDoc[j].placemarks[i].name
											+ '</strong><br><br><strong>'
											+ "Koordinat (latitude, longitude) : "
											+ '</strong>'
											+ geoXMLDoc[j].placemarks[i].latlng
											+ '<br><strong><br>'
											+ "Ketinggian : "
											+ '</strong>'
											+ geoXMLDoc[j].placemarks[i].Point.coordinates[0].alt
											+ " mdpl"
											+ '<br><strong><br>'
											+ "Keterangan : "
											+ '<br>'
											+ '</strong>'
											+ geoXMLDoc[j].placemarks[i].description
											+ '</div>');
							infowindow.open(map,
									geoXMLDoc[j].placemarks[i].marker);
							geoXMLDoc[j].placemarks[i].marker.setAnimation(google.maps.Animation.BOUNCE);
						} else if (geoXMLDoc[j].placemarks[i].polyline) {
							//alert(geoXMLDoc[j].placemarks[i].name);
							var center = geoXMLDoc[j].placemarks[i].polyline
									.getPath().getArray();
							var n = Math.round((center.length / 2));
							infowindow.setContent('<div><strong>'
									+ geoXMLDoc[j].placemarks[i].name
									+ '</strong><br><br><strong>' + "Estimasi panjang jalur : "
									+ '</strong>'
									+ Math.round(getPanjangJalur(j, i))
									+ " meter" + '<br><strong><br>'
									+ "Keterangan : " + '</strong>'
									+ geoXMLDoc[j].placemarks[i].description
									+ '</div>');
							infowindow.setPosition(center[n]);
							infowindow.open(map);
						}
					}
				}
			}
		}

		/* fungsi multiple pm */
		function multiExt(pm) {
			for (var k = 0; k < pm.length; k++) {
				var tp = pm[k];
				for (var j = 0; j < geoXMLDoc.length; j++) {
					for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
						if (tp == geoXMLDoc[j].placemarks[i].extdata["pmId"]) {
							if (geoXMLDoc[j].placemarks[i].marker) {
								//map.setCenter(geoXMLDoc[j].placemarks[i].marker.getPosition());
								if (pm.length < 3){
 								infowindow = new google.maps.InfoWindow({});
 								//google.maps.event.addListener(geoXMLDoc[j].placemarks[i].marker, 'center_changed', function() {
 								infowindow.setContent('<div><strong>'
 										+ geoXMLDoc[j].placemarks[i].name
 										+ '</strong> </div>');
 								infowindow.open(map,
 										geoXMLDoc[j].placemarks[i].marker);
 								}
								geoXMLDoc[j].placemarks[i].marker.setAnimation(google.maps.Animation.BOUNCE);
							} 
 							else if (geoXMLDoc[j].placemarks[i].polyline) {
								//alert(geoXMLDoc[j].placemarks[i].name);
								var center = geoXMLDoc[j].placemarks[i].polyline
										.getPath().getArray();
								var n = Math.round((center.length / 2));
// 								infowindow = new google.maps.InfoWindow({});
								//google.maps.event.addListener(geoXMLDoc[j].placemarks[i].marker, 'center_changed', function() {
// 								infowindow.setContent('<div><strong>'
// 										+ geoXMLDoc[j].placemarks[i].name
// 										+ '</strong></div>');
// 								infowindow.setPosition(center[n]);
								//infowindow.open(map,geoXMLDoc[j].placemarks[i].polyline);
// 								infowindow.open(map);
							}
						}
					}
				}
			}
		}
	</script>
</body>

</html>
