// src/CanadaMap.js
import React from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvent } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';
import './CanadaMap.css';

// Default blue icon
const DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41]
});
L.Marker.prototype.options.icon = DefaultIcon;

// **red marker icon**
const RedIcon = new L.Icon({
  iconUrl: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png', // 红色标记
  iconSize: [32, 32],
  iconAnchor: [16, 32],
  popupAnchor: [0, -32]
});

// Listen to map movement events and update the map range
function MapEventHandler({ setMapBounds }) {
  useMapEvent('moveend', (event) => {
    const bounds = event.target.getBounds();
    if (setMapBounds) {
      setMapBounds(bounds);
    }
  });
  return null;
}

// EclipseMap
function EclipseMap({ userLocation, observations, setMapBounds }) {
  const defaultCenter = [56.130366, -106.346771]; // 默认中心点（加拿大）
  const center = userLocation ? userLocation : defaultCenter;
  const zoom = userLocation ? 9 : 4;

  return (
    <div className="ytmap-wrapper">
      <MapContainer
        center={center}
        zoom={zoom}
        className="w-full h-full"
        style={{ height: '100%', width: '100%' }}
        maxBounds={[[40, -150], [85, -50]]}
        minZoom={3}
        maxZoom={10}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        />

        {/* Monitor map range changes */}
        {setMapBounds && <MapEventHandler setMapBounds={setMapBounds} />}

        {/* User's current location (blue mark) */}
        {userLocation && (
          <Marker position={userLocation}>
            <Popup>Your Location</Popup>
          </Marker>
        )}

        {/* Recommended places (marked in red) */}
        {observations && observations.map((obs, index) => (
          <Marker key={index} position={[obs.lat, obs.lng]} icon={RedIcon}>
            <Popup>{obs.name || 'Observation Point'}</Popup>
          </Marker>
        ))}

        {/* Major cities (marked blue by default) */}
        {/* <Marker position={[43.651070, -79.347015]}>
          <Popup>Toronto</Popup>
        </Marker>
        <Marker position={[49.282730, -123.120735]}>
          <Popup>Vancouver</Popup>
        </Marker>
        <Marker position={[45.501690, -73.567253]}>
          <Popup>Montreal</Popup>
        </Marker> */}
        <Marker position={[43.4643, -80.5204]}>
          <Popup>Montreal</Popup>
        </Marker>
      </MapContainer>
    </div>
  );
}

export default EclipseMap;
