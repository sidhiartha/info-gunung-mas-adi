var mapCanvas = '';
var elevationCanvas = '';
var KML = '';
var afterParseFunc = null;
var map = null;
var geoXML = null;
var infoWindow = null;
var categories = [];

async function parseKML(map_canvas, kml, afterParseFunc) {
  // Request libraries when needed, not in the script tag.
  const {Map} = await google.maps.importLibrary('maps');

  let mapCanvas = document.getElementById('map_canvas')

  let map = new Map(
    mapCanvas,
    {
      center: {lat: -3.9896019053741445, lng: 118.24355803686623},
      zoom: 4,
      zoomControl: true,
      scrollwheel: false
    }
  );

  mapCanvas.classList.add('col-12')
  mapCanvas.style.aspectRatio = '1.8'
  //map.setTilt(45);

  infowindow = new google.maps.InfoWindow({});

  if (kml != '') {
    geoXML = new geoXML3.parser({
      map: map,
      infoWindow: infowindow,
      singleInfoWindow: false,
      afterParse: afterParseFunc
    });
    geoXML.parse(kml);
  }
}

function loadKML(map, elevation, KMLFile, afterParse) {

  mapCanvas = map;
  //elevationCanvas = elevation;
  KML = KMLFile;
  afterParseFunc = afterParse;

  parseKML(map, KMLFile, afterParse);

  //google.load("visualization", "1", {packages: ["columnchart"]});
  //google.setOnLoadCallback(drawElevation);

}

function parse(pmIdAll, kmlFiles) {
  console.log(`pmId ${pmIdAll}`)
  console.log(`kml ${kmlFiles}`)

  var geoXMLDoc = null;
  var kml = remArrayKml(kmlFiles);
  var pmId = remArraypmId(pmIdAll);

  var highlightLineOptions = {
    fillColor: '#FFFF00',
    strokeColor: '#FF0000',
    fillOpacity: 0.9,
    strokeWidth: 10
  };

  /*hapus kml yang sama*/
  function remArrayKml(kmlFiles) {
    temp_kmlFiles = {};
    for (var i = 0; i < kmlFiles.length; i++) {
      temp_kmlFiles[kmlFiles[i]] = kmlFiles[i];
    }
    new_kmlFiles = [];
    for (var key in temp_kmlFiles) {
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
    for (var key in temp_pmIdAll) {
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
            strokeColor: geoXMLDoc[j].placemarks[i].polyline
              .get('strokeColor'),
            strokeWeight: geoXMLDoc[j].placemarks[i].polyline
              .get('strokeWeight'),
            strokeOpacity: geoXMLDoc[j].placemarks[i].polyline
              .get('strokeOpacity')
          };
          geoXMLDoc[j].placemarks[i].polyline.normalStyle = normalStyle;
          highlightPoly(geoXMLDoc[j].placemarks[i].polyline, j, i);
        }
      }
    }
    buildSideMark();
    map.addListener('bounds_changed', buildSideMark);
    map.addListener('center_changed', buildSideMark);
    map.addListener('zoom_changed', buildSideMark);
    cekPm(pmId);
  }

  function highlightPoly(poly, kml, placemark) {
    poly.addListener('mouseover', function () {
      var rowElem = document.getElementById('row' + kml + placemark);
      if (rowElem)
        rowElem.style.backgroundColor = '#FFFA5E';
      poly.setOptions(highlightLineOptions);
    });
    poly.addListener('mouseout', function () {
      var rowElem = document.getElementById('row' + kml + placemark);
      if (rowElem)
        rowElem.style.backgroundColor = '#FFFFFF';
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

  var rad = function (x) {
    return x * Math.PI / 180;
  };

  function getKoordinat(latlng) {
    var ll = latlng.toString();
    var k = ll.substring(1, ll.indexOf(')'));
    var koordinat = k.split(', ');
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
        + '</strong><br><br><strong>' + 'Estimasi Jarak : '
        + '</strong>'
        + Math.round(getPanjangJalur(kml, placemark))
        + ' meter' + '<br><strong><br>' + 'Keterangan : '
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
          + 'Koordinat (latitude, longitude) : '
          + '</strong>'
          + geoXMLDoc[kml].placemarks[placemark].latlng
          + '<br><strong><br>'
          + 'Ketinggian : '
          + '</strong>'
          + geoXMLDoc[kml].placemarks[placemark].Point.coordinates[0].alt
          + ' mdpl'
          + '<br><strong><br>'
          + 'Keterangan : '
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
    if (!currentBounds) {
      currentBounds = new google.maps.LatLngBounds();
    }

    if (geoXMLDoc) {
      var sideMark = '<table id="mark_table">';
      for (var j = 0; j < geoXMLDoc.length; j++) {
        for (var i = 0; i < geoXMLDoc[j].placemarks.length; i++) {
          if (!categories[geoXMLDoc[j].placemarks[i].styleUrl]) {
            var styleName = geoXMLDoc[j].placemarks[i].styleUrl;
            styleName.replace(' ', '_');
            categories[geoXMLDoc[j].placemarks[i].styleUrl] = {
              name: styleName,
              style: geoXMLDoc[j].placemarks[i].style,
              styleUrl: geoXMLDoc[j].placemarks[i].styleUrl
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
              sideMark += '<td><img src=' + geoXMLDoc[j].placemarks[i].style.href + ' height="15" width="15" alt="icon" /></td>';
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
              sideMark += '<tr id="row' + j + i + '" style="border-bottom: 1px dashed #969696;">';
              sideMark += '<td><img src=' + geoXMLDoc[j].placemarks[i].style.href + ' height="15" width="15" alt="icon" /></td>';
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
        legend += '<img src=' + categories[id].style.href + ' height="15" width="15" alt="icon" /></td>'
        legend += '<td>' + id.substr(1) + '</td>';
        legend += '</tr>';
      }
      legend += '</table>';
      document.getElementById('side-legend').innerHTML = legend;
      document.getElementById('side-mark').innerHTML = sideMark;
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
        if (pm == geoXMLDoc[j].placemarks[i].extdata['pmId']) {
          //alert(geoXMLDoc[j].placemarks[i].name);
          if (geoXMLDoc[j].placemarks[i].marker) {
            //alert(geoXMLDoc[j].placemarks[i].name);
            map.setCenter(geoXMLDoc[j].placemarks[i].marker
              .getPosition());
            infowindow
              .setContent('<div><strong>'
                + geoXMLDoc[j].placemarks[i].name
                + '</strong><br><br><strong>'
                + 'Koordinat (latitude, longitude) : '
                + '</strong>'
                + geoXMLDoc[j].placemarks[i].latlng
                + '<br><strong><br>'
                + 'Ketinggian : '
                + '</strong>'
                + geoXMLDoc[j].placemarks[i].Point.coordinates[0].alt
                + ' mdpl'
                + '<br><strong><br>'
                + 'Keterangan : '
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
              + '</strong><br><br><strong>' + 'Estimasi panjang jalur : '
              + '</strong>'
              + Math.round(getPanjangJalur(j, i))
              + ' meter' + '<br><strong><br>'
              + 'Keterangan : ' + '</strong>'
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
          if (tp == geoXMLDoc[j].placemarks[i].extdata['pmId']) {
            if (geoXMLDoc[j].placemarks[i].marker) {
              //map.setCenter(geoXMLDoc[j].placemarks[i].marker.getPosition());
              if (pm.length < 3) {
                infowindow = new google.maps.InfoWindow({});
                //google.maps.event.addListener(geoXMLDoc[j].placemarks[i].marker, 'center_changed', function() {
                infowindow.setContent('<div><strong>'
                  + geoXMLDoc[j].placemarks[i].name
                  + '</strong> </div>');
                infowindow.open(map,
                  geoXMLDoc[j].placemarks[i].marker);
              }
              geoXMLDoc[j].placemarks[i].marker.setAnimation(google.maps.Animation.BOUNCE);
            } else if (geoXMLDoc[j].placemarks[i].polyline) {
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
}








