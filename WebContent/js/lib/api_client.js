const baseUrl = `${window.location.origin}${window.location.pathname}`


async function searchKeyWord(keyword) {
  console.log(`base url = ${baseUrl}`)
  const response = await fetch(`${baseUrl}token?keyword=${keyword}`)
  return response.json()
}

const kmlFullUrl = (kml) => {
  return `${baseUrl}/${kml}`
}
