import React, { useEffect, useState } from "react";
// import Map from "../components/Map";
import "./AuroraPage.css";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";
import LocationSelector from "../components/LocationSelector";
import CanadaMap from '../components/CanadaMap';
// import HeapMap from '../components/HeapMap';
function AuroraPage() {
    const [kpIndex, setKpIndex] = useState("Loading...");
    const [forecastData, setForecastData] = useState([]);
    const [selectedCity, setSelectedCity] = useState("");

    const handleCitySelect = (cityName, kpValue, forecast) => {
        setSelectedCity(cityName);
        setKpIndex(kpValue);

        const formattedData = Object.entries(forecast)
            .map(([time, probability]) => ({
                time,
                probability: parseInt(probability.replace("%", ""), 10),
            }))
            .sort((a, b) => {
                const timeToNumber = (t) => {
                    const [hour, period] = t.split(" ");
                    let hourNum = parseInt(hour, 10);
                    if (period === "PM" && hourNum !== 12) hourNum += 12;
                    if (period === "AM" && hourNum === 12) hourNum = 0;
                    return hourNum;
                };
                return timeToNumber(a.time) - timeToNumber(b.time);
            });
        setForecastData(formattedData);
    };

    return (
        <div className="map-page">
            <div className="top-section">
                <div className="map-selector-container">
                    <div className="map-container">
                        <div className="location-title">Location</div>
                        <CanadaMap selectedCity={selectedCity} />
                    </div>
                    <LocationSelector onCitySelect={handleCitySelect} />
                </div>

                <div className="info-container">
                    <div className="title">Information</div>
                    <div className="info-box weather-box">weather</div>
                    <div className="info-box kp-box">KP Index: {kpIndex}</div>
                    <div className="info-box cloud-cover-box">Cloud Cover</div>
                    <div className="info-box solar-wind-box">Solar Wind</div>
                    <div className="info-box visibility-box">Visibility</div>
                    <div className="info-box time-box">Time of a Day</div>
                </div>
            </div>

            <div className="prediction-container">
                <h2>Probability of Detecting Aurora</h2>
                <ResponsiveContainer width="80%" height={300}>
                    <LineChart data={forecastData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="time" />
                        <YAxis label={{ value: "Probability %", angle: -90, position: "insideLeft" }} />
                        <Tooltip />
                        <Line type="monotone" dataKey="probability" stroke="#8884d8" dot={{ r: 3 }} />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
}

export default AuroraPage;
