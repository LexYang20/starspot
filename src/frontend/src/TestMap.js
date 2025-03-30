// src/TestMap.js
import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';     // 一定要导入 CSS
import L from 'leaflet';
import iconUrl from 'leaflet/dist/images/marker-icon.png';
import iconShadowUrl from 'leaflet/dist/images/marker-shadow.png';

// 配置默认图标，避免默认图标丢失
let DefaultIcon = L.icon({
  iconUrl: iconUrl,
  shadowUrl: iconShadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41]
});
L.Marker.prototype.options.icon = DefaultIcon;

function TestMap() {
  // 加拿大中心点
  const defaultCenter = [56.130366, -106.346771];

  return (
    <div style={{ border: '2px solid red', width: '100%', height: '600px' }}>
      {/* 注意这里给 MapContainer 明确一个宽高或让父容器有宽高 */}
      <MapContainer 
        center={defaultCenter} 
        zoom={4}
        style={{ width: '100%', height: '100%' }}
      >
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution='&copy; OpenStreetMap contributors'
        />

        {/* 随便在多伦多放一个 Marker 用来测试 */}
        <Marker position={[43.651070, -79.347015]}>
          <Popup>Toronto</Popup>
        </Marker>
      </MapContainer>
    </div>
  );
}

export default TestMap;
