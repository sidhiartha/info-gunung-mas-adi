/*
*
*
*/

async function parseSearchResponse(keyword, successComponents, errorComponents) {
  successComponents.root.classList.add('d-none')
  errorComponents.root.style.display = 'none'

  const response = await searchKeyWord(keyword)

  if (response.error_code >= 0) {
    errorComponents.root.style.display = 'block'
    errorComponents.root.innerHTML = response.output
  } else {
    successComponents.root.classList.remove('d-none')
    successComponents.message.innerHTML = response.output
    await successComponents.map.initMap()

    const kmls = [...new Set(response.maps.myArrayList)]
    const pmIds = [...new Set(response.pm_id.myArrayList)]

    const fullKmls = kmls.map(kml => kmlFullUrl(kml))
    successComponents.map.loadKml(fullKmls, pmIds)
  }
}
