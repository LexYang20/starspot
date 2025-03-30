export async function getEclipseObservations(mapBounds) {
    // Mock data for eclipse observation points
    return [
      { lat: mapBounds.north - 0.1, lng: mapBounds.east - 0.1 },
      { lat: mapBounds.south + 0.1, lng: mapBounds.west + 0.1 }
    ];
  }
  