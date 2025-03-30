import React from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import { useEffect } from "react";
import L from "leaflet";
import "leaflet.heat";

// 示例数据格式：[纬度, 经度, 权重]，权重可以是极光出现概率（0-1 之间）
const auroraData = [
    [60.0, -113.0, 0.8],
    [61.0, -114.0, 0.6],
    [62.0, -115.0, 0.9],
    [63.0, -116.0, 0.7],
    [64.0, -117.0, 0.6],
    [65.0, -120.0, 0.9],
    [66.0, -125.0, 0.8], 
    [67.0, -130.0, 0.7], 
  ];

const Heatmap = () => {
  useEffect(() => {
    const map = L.map("heatmap").setView([60, -113], 4); // 设置地图中心和缩放级别

    // 添加地图底图
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "© OpenStreetMap contributors",
    }).addTo(map);

    // 创建热力图层
    const heat = L.heatLayer(auroraData, {
      radius: 20, // 热力点的半径
      blur: 15,   // 模糊程度
      maxZoom: 10,
      max: 1,     // 归一化的最大值
    }).addTo(map);

    return () => map.remove(); // 清理地图实例
  }, []);

  return <div id="heatmap" style={{ height: "600px", width: "100%" }} />;
};

export default Heatmap;
