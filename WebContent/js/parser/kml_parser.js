let map
let infoWindow
let geoXmlDoc
let pmIds
let categories

let highlightLineOptions = {
  fillColor: '#FFFF00',
  strokeColor: '#FF0000',
  fillOpacity: 0.9,
  strokeWidth: 10
}

async function initMapHelper() {
  const {Map} = await google.maps.importLibrary('maps')

  let mapCanvas = document.getElementById('map_canvas')

  if (!map) {
    map = new Map(
      mapCanvas,
      {
        center: {lat: -3.9896019053741445, lng: 118.24355803686623},
        zoom: 4,
        zoomControl: true,
        scrollwheel: false,
        mapId: 'de23be82e426640f',
      },
    )

    mapCanvas.classList.add('col-12')
    mapCanvas.style.aspectRatio = '1.8'
  } else {
    cleanMap()
  }
}

function cleanMap() {
  if (!geoXmlDoc) {
    return
  }

  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      if (geoXmlDoc[j].placemarks[i].polyline)
        geoXmlDoc[j].placemarks[i].polyline.setMap(null)
      else
        geoXmlDoc[j].placemarks[i].marker.setMap(null)
    }
  }
  infoWindow.close()
}

async function loadKmlHelper(kmls, _pmIds) {
  pmIds = _pmIds
  const {InfoWindow} = await google.maps.importLibrary('maps')

  infoWindow = new InfoWindow({})

  let geoXml3 = new geoXML3.parser({
    map: map,
    infoWindow: infoWindow,
    singleInfoWindow: false,
    afterParse: afterParse
  })

  geoXml3.parse(kmls)
}

function afterParse(doc) {
  geoXmlDoc = doc

  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      // Mouseover Highlight Styling for Polyline (Jalur Pendakian)
      if (geoXmlDoc[j].placemarks[i].polyline) {
        geoXmlDoc[j].placemarks[i].polyline.normalStyle = {
          strokeColor: geoXmlDoc[j].placemarks[i].polyline
            .get('strokeColor'),
          strokeWeight: geoXmlDoc[j].placemarks[i].polyline
            .get('strokeWeight'),
          strokeOpacity: geoXmlDoc[j].placemarks[i].polyline
            .get('strokeOpacity')
        }
        highlightPoly(geoXmlDoc[j].placemarks[i].polyline, j, i)
      }
    }
  }

  buildMapSidebar()
  google.maps.event.addListener(map, 'bounds_changed', buildMapSidebar)
  google.maps.event.addListener(map, 'center_changed', buildMapSidebar)
  google.maps.event.addListener(map, 'zoom_changed', buildMapSidebar)
  cekPm(pmIds)
}

function highlightPoly(poly, kml, placemark) {
  google.maps.event.addListener(poly, 'mouseover', function () {
    let rowElem = document.getElementById('row' + kml + placemark)
    if (rowElem) {
      rowElem.style.backgroundColor = '#FFFA5E'
    }
    poly.setOptions(highlightLineOptions)
  })
  google.maps.event.addListener(poly, 'mouseout', function () {
    let rowElem = document.getElementById('row' + kml + placemark)
    if (rowElem) {
      rowElem.style.backgroundColor = '#FFFFFF'
    }
    poly.setOptions(poly.normalStyle)
  })
}

function buildMapSidebar() {
  if (geoXmlDoc) {
    categories = []
    buildSideMark()
    buildLegend()
  }
}

function buildSideMark() {
  let currentBounds = map.getBounds()
  if (!currentBounds) {
    currentBounds = new google.maps.LatLngBounds()
  }
  let sideMark = '<table id="mark_table">'
  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      if (!categories[geoXmlDoc[j].placemarks[i].styleUrl]) {
        let styleName = geoXmlDoc[j].placemarks[i].styleUrl
        styleName.replace(' ', '_')
        categories[geoXmlDoc[j].placemarks[i].styleUrl] = {
          name: styleName,
          style: geoXmlDoc[j].placemarks[i].style,
          styleUrl: geoXmlDoc[j].placemarks[i].styleUrl
        }
      }
      if (geoXmlDoc[j].placemarks[i].polyline) {
        if (currentBounds.intersects(geoXmlDoc[j].placemarks[i].polyline.bounds)) {
          sideMark += '<tr id="row' + j + i
            + '" onmouseover="kmlHighlightPoly('
            + j + ',' + i
            + ')" onmouseout="kmlUnHighlightPoly('
            + j + ',' + i + ')">'
          sideMark += '<td><img src=' + geoXmlDoc[j].placemarks[i].style.href + ' height="15" width="15" alt="icon" /></td>'
          sideMark += '<td><a href="javascript:kmlClick('
            + j + ',' + i + ')">'
            + geoXmlDoc[j].placemarks[i].name
            + '</a></td>'
          sideMark += '<td><a href="javascript:kmlShowPlacemark('
            + j + ',' + i + ')">(+)</a></td>'
          sideMark += '</tr>'
        }
      } else {
        if (currentBounds.contains(geoXmlDoc[j].placemarks[i].marker.getPosition())) {
          sideMark += '<tr id="row' + j + i + '" style="border-bottom: 1px dashed #969696">'
          sideMark += '<td><img src=' + geoXmlDoc[j].placemarks[i].style.href + ' height="15" width="15" alt="icon" /></td>'
          sideMark += '<td><a href="javascript:kmlClick('
            + j + ',' + i + ')">'
            + geoXmlDoc[j].placemarks[i].name
            + '</a></td>'
          sideMark += '<td></td>'
          sideMark += '</tr>'
        }
      }
    }
  }
  sideMark += '</table>'
  document.getElementById('side-mark').innerHTML = sideMark
}

function kmlHighlightPoly(kml, placemark) {
  geoXmlDoc[kml].placemarks[placemark].polyline
    .setOptions(highlightLineOptions)
}

function kmlUnHighlightPoly(kml, placemark) {
  geoXmlDoc[kml].placemarks[placemark].polyline
    .setOptions(geoXmlDoc[kml].placemarks[placemark].polyline.normalStyle)
}

function kmlClick(kml, placemark) {
  if (geoXmlDoc[kml].placemarks[placemark].polyline) {
    let center = geoXmlDoc[kml].placemarks[placemark].polyline
      .getPath().getArray()
    let n = Math.round((center.length / 2))
    infoWindow.setContent('<div><strong>'
      + geoXmlDoc[kml].placemarks[placemark].name
      + '</strong><br><br><strong>' + 'Estimasi Jarak : '
      + '</strong>'
      + Math.round(getPanjangJalur(kml, placemark))
      + ' meter' + '<br><strong><br>' + 'Keterangan : '
      + '</strong>'
      + geoXmlDoc[kml].placemarks[placemark].description
      + '</div>')
    infoWindow.setPosition(center[n])
    infoWindow.open(map)
  } else {
    geoXmlDoc[kml].placemarks[placemark].marker.setAnimation(google.maps.Animation.DROP)
    map.setCenter(geoXmlDoc[kml].placemarks[placemark].marker
      .getPosition())
    infoWindow
      .setContent('<div><strong>'
        + geoXmlDoc[kml].placemarks[placemark].name
        + '</strong><br><br><strong>'
        + 'Koordinat (latitude, longitude) : '
        + '</strong>'
        + geoXmlDoc[kml].placemarks[placemark].latlng
        + '<br><strong><br>'
        + 'Ketinggian : '
        + '</strong>'
        + geoXmlDoc[kml].placemarks[placemark].Point.coordinates[0].alt
        + ' mdpl'
        + '<br><strong><br>'
        + 'Keterangan : '
        + '<br>'
        + '</strong>'
        + geoXmlDoc[kml].placemarks[placemark].description
        + '</div>')
    infoWindow.open(map,
      geoXmlDoc[kml].placemarks[placemark].marker)
  }
}

function getPanjangJalur(kml, jalur) {
  let pjalur = geoXmlDoc[kml].placemarks[jalur].polyline.getPath()
    .getArray()
  let jarakTotal = 0
  for (let i = 0; i < pjalur.length; i++) {
    let koordinat1 = getKoordinat(pjalur[i])
    let koordinat2 = getKoordinat(pjalur[i + 1])
    jarakTotal += getJarak(koordinat1, koordinat2)
    if (i === (pjalur.length - 2)) {
      break
    }
  }
  return jarakTotal
}

function getKoordinat(latlng) {
  let ll = latlng.toString()
  let k = ll.substring(1, ll.indexOf(')'))
  return k.split(', ')
}

function getJarak(koordinat1, koordinat2) {
  // Rumus Haversine
  let R = 6378137 // Earth mean radius in meter
  let dLat = rad(koordinat2[0] - koordinat1[0])
  let dLong = rad(koordinat2[1] - koordinat1[1])
  let a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
    + Math.cos(rad(koordinat1[0]))
    * Math.cos(rad(koordinat2[0])) * Math.sin(dLong / 2)
    * Math.sin(dLong / 2)
  let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  return R * c // returns the distance in meter
}

let rad = function (x) {
  return x * Math.PI / 180
}

function kmlShowPlacemark(kml, placemark) {
  if (geoXmlDoc[kml].placemarks[placemark].polyline) {
    map.fitBounds(geoXmlDoc[kml].placemarks[placemark].polyline.bounds)
  } else {
    map.setCenter(geoXmlDoc[kml].placemarks[placemark].marker.getPosition())
  }
  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      let placemark = geoXmlDoc.placemarks[i]
      if (kml === j && placemark === i) {
        if (geoXmlDoc[kml].placemarks[placemark].polyline)
          geoXmlDoc[kml].placemarks[placemark].polyline
            .setMap(map)
        if (geoXmlDoc[kml].placemarks[placemark].marker)
          geoXmlDoc[kml].placemarks[placemark].marker
            .setMap(map)
      } else {
        if (geoXmlDoc[kml].placemarks[placemark].polyline)
          geoXmlDoc[kml].placemarks[placemark].polyline
            .setMap(null)
        if (geoXmlDoc[kml].placemarks[placemark].marker)
          geoXmlDoc[kml].placemarks[placemark].marker
            .setMap(null)
      }
    }
  }
}

function kmlShowAll() {
  let currentBounds = new google.maps.LatLngBounds()
  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      if (geoXmlDoc[j].placemarks[i].polyline)
        geoXmlDoc[j].placemarks[i].polyline.setMap(map)
      else {
        geoXmlDoc[j].placemarks[i].marker.setMap(map)
        currentBounds.extend(geoXmlDoc[j].placemarks[i].marker
          .getPosition())
      }
    }
  }
  map.fitBounds(currentBounds)
  infoWindow.close()
}

function buildLegend() {
  let legend = '<table style="color:#808080 font-size:13px" id="legend_table" cellspacing="0" cellpadding="0">'
  for (let id in categories) {
    legend += '<tr style="border-bottom: 1px solid black">'
    legend += '<td><input type="checkbox" checked="checked" onclick="legendClick(this,&quot'
      + categories[id].name
      + '&quot)" id="'
      + id
      + '_box" /></td>'
    legend += '<td>'
    legend += '<img src=' + categories[id].style.href + ' height="15" width="15" alt="icon" /></td>'
    legend += '<td>' + id.substr(1) + '</td>'
    legend += '</tr>'
  }
  legend += '</table>'
  document.getElementById('side-legend').innerHTML = legend
}

function legendClick(box, category) {
  if (box.checked)
    show(category)
  else
    hide(category)
}

function show(category) {
  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      if (geoXmlDoc[j].placemarks[i].styleUrl === category) {
        if (geoXmlDoc[j].placemarks[i].polyline)
          geoXmlDoc[j].placemarks[i].polyline.setMap(map)
        else
          geoXmlDoc[j].placemarks[i].marker.setMap(map)
      }
    }
  }
}

function hide(category) {
  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      if (geoXmlDoc[j].placemarks[i].styleUrl === category) {
        if (geoXmlDoc[j].placemarks[i].polyline)
          geoXmlDoc[j].placemarks[i].polyline.setMap(null)
        else
          geoXmlDoc[j].placemarks[i].marker.setMap(null)
      }
    }
  }
  infoWindow.close()
}

function cekPm(pmId) {
  console.log(`pmId ${pmId.length}`)
  if (pmId.length === 1) {
    singleExt(pmId)
  } else if (pmId.length > 1) {
    multiExt(pmId)
  }
}

/* fungsi single pm */
function singleExt(pm) {
  console.log(`geoXmlDoc ${geoXmlDoc.length}`)
  for (let j = 0; j < geoXmlDoc.length; j++) {
    for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
      console.log(`pm ${pm}`)
      console.log(`geoXmlDoc pmId ${geoXmlDoc[j].placemarks[i].extdata['pmId']}`)
      if (pm === geoXmlDoc[j].placemarks[i].extdata['pmId']) {
        console.log(`marker or ${geoXmlDoc[j].placemarks[i].marker}`)
        //alert(geoXmlDoc[j].placemarks[i].name)
        if (geoXmlDoc[j].placemarks[i].marker) {
          //alert(geoXmlDoc[j].placemarks[i].name)
          map.setCenter(geoXmlDoc[j].placemarks[i].marker
            .getPosition())
          infoWindow
            .setContent('<div><strong>'
              + geoXmlDoc[j].placemarks[i].name
              + '</strong><br><br><strong>'
              + 'Koordinat (latitude, longitude) : '
              + '</strong>'
              + geoXmlDoc[j].placemarks[i].latlng
              + '<br><strong><br>'
              + 'Ketinggian : '
              + '</strong>'
              + geoXmlDoc[j].placemarks[i].Point.coordinates[0].alt
              + ' mdpl'
              + '<br><strong><br>'
              + 'Keterangan : '
              + '<br>'
              + '</strong>'
              + geoXmlDoc[j].placemarks[i].description
              + '</div>')
          infoWindow.open(map,
            geoXmlDoc[j].placemarks[i].marker)
          geoXmlDoc[j].placemarks[i].marker.setAnimation(google.maps.Animation.BOUNCE)
          console.log('bouncing 1')
        } else if (geoXmlDoc[j].placemarks[i].polyline) {
          //alert(geoXmlDoc[j].placemarks[i].name)
          let center = geoXmlDoc[j].placemarks[i].polyline
            .getPath().getArray()
          let n = Math.round((center.length / 2))
          infoWindow.setContent('<div><strong>'
            + geoXmlDoc[j].placemarks[i].name
            + '</strong><br><br><strong>' + 'Estimasi panjang jalur : '
            + '</strong>'
            + Math.round(getPanjangJalur(j, i))
            + ' meter' + '<br><strong><br>'
            + 'Keterangan : ' + '</strong>'
            + geoXmlDoc[j].placemarks[i].description
            + '</div>')
          infoWindow.setPosition(center[n])
          infoWindow.open(map)
        }
      }
    }
  }
}

/* fungsi multiple pm */
function multiExt(pm) {
  for (let k = 0; k < pm.length; k++) {
    let tp = pm[k]
    for (let j = 0; j < geoXmlDoc.length; j++) {
      for (let i = 0; i < geoXmlDoc[j].placemarks.length; i++) {
        if (tp === geoXmlDoc[j].placemarks[i].extdata['pmId']) {
          if (geoXmlDoc[j].placemarks[i].marker) {
            //map.setCenter(geoXmlDoc[j].placemarks[i].marker.getPosition())
            if (pm.length < 3) {
              infoWindow = new google.maps.InfoWindow({})
              infoWindow.setContent('<div><strong>'
                + geoXmlDoc[j].placemarks[i].name
                + '</strong> </div>')
              infoWindow.open(map,
                geoXmlDoc[j].placemarks[i].marker)
            }
            geoXmlDoc[j].placemarks[i].marker.setAnimation(google.maps.Animation.BOUNCE)
            console.log('bouncing 2')
          } else if (geoXmlDoc[j].placemarks[i].polyline) {
            console.log('not bouncing 2')
          }
        }
      }
    }
  }
}
