import React from 'react';
import { CircleMarker, Tooltip } from 'react-leaflet';
import { getCloudColor, getLightPollutionColor } from '../../utils/WeatherPollutionColor';

/**
 * CityPoint Component
 *
 * Renders markers and data show box for selected cities
 * Displays different layers (clouds, light pollution) based on configuration
 *
 * @param {string} city - City name
 * @param {Array} pos - lat, long
 * @param {boolean} selected - Whether this city is currently selected
 * @param {Object} data - Weather data for this city
 * @param {boolean} showCloudLayer - toggle
 * @param {boolean} showLightPollution - toggle
 * @param {Function} handleCitySelect - Callback when a city marker is clicked
 * @returns {JSX.Element|null}
 */
const CityPoint = ({ city, pos, selected, data, showCloudLayer, showLightPollution, handleCitySelect }) => {
    // Only render when city is selected
    if (!selected) {
        return null;
    }

    return (
        <React.Fragment>
            {/* Conditional cloud layer marker */}
            {showCloudLayer && (
                <CircleMarker
                    center={pos}
                    radius={15}
                    pathOptions={{
                        fillColor: getCloudColor(data?.cloudCover || 0),
                        fillOpacity: 0.6,
                        color: getCloudColor(data?.cloudCover || 0),
                        weight: 1
                    }}
                />
            )}

            {/* Conditional light pollution marker */}
            {showLightPollution && (
                <CircleMarker
                    center={pos}
                    radius={15}
                    pathOptions={{
                        fillColor: getLightPollutionColor(data?.lightPollution || 0),
                        fillOpacity: 0.6,
                        color: getLightPollutionColor(data?.lightPollution || 0),
                        weight: 1
                    }}
                />
            )}

            {/* City marker and tooltip */}
            <CircleMarker
                center={pos}
                radius={5}
                pathOptions={{
                    fillColor: '#fff',
                    fillOpacity: 0.8,
                    color: '#fff',
                    weight: 2
                }}
                eventHandlers={{
                    click: () => handleCitySelect(city)
                }}
            >
                <Tooltip
                    direction="top"
                    offset={[0, -20]}
                    opacity={0.95}
                    permanent={true}
                >
                    <div className="canada-weather-tooltip-content">
                        <div className="canada-weather-city-name">{city}</div>
                        <div className="canada-weather-weather-data">
                            <div className="canada-weather-data-row">
                                <span>Temperature:</span>
                                <span>{data?.temperature}°C</span>
                            </div>
                            <div className="canada-weather-data-row">
                                <span>Cloud Coverage:</span>
                                <span>{data?.cloudCover}%</span>
                            </div>
                            <div className="canada-weather-data-row">
                                <span>High Cloud Coverage:</span>
                                <span>{data?.cloudCoverHigh}%</span>
                            </div>
                            <div className="canada-weather-data-row">
                                <span>Mid Cloud Coverage:</span>
                                <span>{data?.cloudCoverMid}%</span>
                            </div>
                            <div className="canada-weather-data-row">
                                <span>Low Cloud Coverage:</span>
                                <span>{data?.cloudCoverLow}%</span>
                            </div>
                            <div className="canada-weather-data-row">
                                <span>Light Pollution:</span>
                                <span>{data?.lightPollution}</span>
                            </div>
                        </div>
                    </div>
                </Tooltip>
            </CircleMarker>
        </React.Fragment>
    );
};

export default CityPoint;