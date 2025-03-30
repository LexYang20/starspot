import React, { useState, useEffect, useRef } from 'react';
import { MapContainer, TileLayer, CircleMarker, Tooltip, useMap, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import './CanadaMap.css';
import L from 'leaflet';

/**
 * Map configuration constants
 * Contains all the static configuration parameters for the map display
 * - center: Geographical center of Canada for initial view
 * - zoom: Initial zoom level for the map
 * - maxBounds: Restricts map panning to these coordinates to keep focus on Canada
 * - minZoom/maxZoom: Limits zoom levels to maintain appropriate detail
 * - maxBoundsViscosity: Controls how "hard" the map boundaries are (1.0 = cannot pan outside boundaries)
 */
const MAP_CONFIG = {
    center: [56.130366, -106.346771], // Center of Canada
    zoom: 4,
    maxBounds: [
        [40, -150], // Southwest coordinates
        [85, -50]   // Northeast coordinates
    ],
    minZoom: 3,
    maxZoom: 6,
    maxBoundsViscosity: 1.0
};

/**
 * MapController Component
 *
 * Handles map view changes when a city is selected
 * Automatically zooms to city location when selectedCity changes
 *
 * @param {string} selectedCity - The name of the currently selected city
 * @param {Object} cities - Object containing city name to coordinates mapping
 * @returns {null} This component doesn't render any visible elements
 */
const MapController = ({ selectedCity, cities }) => {
    const map = useMap();

    useEffect(() => {
        // When a city is selected, center the map on its coordinates with appropriate zoom
        if (selectedCity && cities[selectedCity]) {
            map.setView(cities[selectedCity], 8);
        }
    }, [selectedCity, cities, map]);

    return null;
};

/**
 * MapClickHandler Component
 *
 * Detects and processes clicks on empty areas of the map
 * Used to deselect the current city when user clicks on empty map space
 *
 * @param {Function} onMapClick - Callback function to execute when map is clicked
 * @returns {null} This component doesn't render any visible elements
 */
const MapClickHandler = ({ onMapClick }) => {
    useMapEvents({
        click: (e) => {
            const target = e.originalEvent.target;
            // Only trigger deselection if clicking on non-interactive elements
            // and not on tooltips
            if (!target.classList?.contains('leaflet-interactive') &&
                !target.closest('.leaflet-tooltip')) {
                onMapClick();
            }
        },
    });
    return null;
};

/**
 * SearchBox Component
 *
 * Provides city search functionality with auto-complete dropdown
 *
 * @param {string} searchTerm - Current search input value
 * @param {Function} setSearchTerm - Function to update search term state
 * @param {Array} filteredCities - List of cities matching the current search term
 * @param {Function} handleCitySelect - Callback for when a city is selected from dropdown
 * @returns {JSX.Element} Search input with dropdown results
 */
const SearchBox = ({ searchTerm, setSearchTerm, filteredCities, handleCitySelect }) => (
    <div className="canada-weather-search-container">
        <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search cities"
            className="canada-weather-search-input"
        />
        {filteredCities.length > 0 && (
            <div className="canada-weather-search-dropdown">
                {filteredCities.map(city => (
                    <div
                        key={city}
                        onClick={() => handleCitySelect(city)}
                        className="canada-weather-search-item"
                    >
                        {city}
                    </div>
                ))}
            </div>
        )}
    </div>
);

/**
 * LightPollutionLayer Component
 *
 * Manages the light pollution overlay on the map
 * Dynamically adds/removes the layer based on toggle state
 *
 * @param {boolean} showLightPollution - Flag to control layer visibility
 * @returns {null} This component doesn't render any visible elements
 */
const LightPollutionLayer = ({ showLightPollution }) => {
    const map = useMap();
    const lightPollutionLayerRef = useRef(null);

    useEffect(() => {
        if (showLightPollution) {
            // Create and add light pollution layer if it doesn't exist yet
            if (!lightPollutionLayerRef.current) {
                lightPollutionLayerRef.current = L.tileLayer('https://djlorenz.github.io/astronomy/lp2006/overlay/tiles/tile_{z}_{x}_{y}.png', {
                    opacity: 0.6,
                    maxNativeZoom: 9,
                    maxZoom: 18,
                    bounds: L.latLngBounds(
                        L.latLng(-60.0, -180.0),
                        L.latLng(75.0, 180.0)
                    )
                }).addTo(map);
            }
        } else {
            // Remove layer if it exists and showLightPollution is false
            if (lightPollutionLayerRef.current) {
                map.removeLayer(lightPollutionLayerRef.current);
                lightPollutionLayerRef.current = null;
            }
        }

        // Cleanup function to remove layers when component unmounts
        return () => {
            if (lightPollutionLayerRef.current) {
                map.removeLayer(lightPollutionLayerRef.current);
            }
        };
    }, [map, showLightPollution]);

    return null;
};

/**
 * CityPoint Component
 *
 * Renders markers and data tooltips for selected cities
 * Shows different layers based on configuration (cloud layer, light pollution)
 *
 * @param {string} city - City name
 * @param {Array} pos - Geographic coordinates [lat, lng]
 * @param {boolean} selected - Whether this city is currently selected
 * @param {Object} data - Weather and environmental data for this city
 * @param {boolean} showCloudLayer - Whether to show cloud coverage visualization
 * @param {boolean} showLightPollution - Whether to show light pollution visualization
 * @param {Function} handleCitySelect - Callback for when city marker is clicked
 * @returns {JSX.Element|null} City marker with tooltip or null if not selected
 */
const CityPoint = ({ city, pos, selected, data, showCloudLayer, showLightPollution, handleCitySelect }) => {
    // Only render when the city is selected
    if (!selected) {
        return null;
    }

    /**
     * Determines appropriate color to represent cloud coverage level
     * Darker blues indicate higher cloud coverage
     *
     * @param {number} coverage - Cloud coverage percentage (0-100)
     * @returns {string} Hex color code
     */
    const getCloudColor = (coverage) => {
        if (coverage >= 80) return '#0d47a1'; // Very high coverage - dark blue
        if (coverage >= 60) return '#1976d2'; // High coverage
        if (coverage >= 40) return '#42a5f5'; // Medium coverage
        if (coverage >= 20) return '#90caf9'; // Low coverage
        return '#bbdefb';                     // Very low coverage - light blue
    };

    /**
     * Returns color for light pollution visualization
     *
     * @returns {string} Hex color code for light pollution
     */
    const getLightPollutionColor = () => {
        return '#FF0000'; // Deep red color for light pollution
    };

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
                        fillColor: getLightPollutionColor(),
                        fillOpacity: 0.6,
                        color: getLightPollutionColor(),
                        weight: 1
                    }}
                />
            )}

            {/* City marker with tooltip */}
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
                                <span>Cloud Cover High:</span>
                                <span>{data?.cloudCoverHigh}%</span>
                            </div>
                            <div className="canada-weather-data-row">
                                <span>Cloud Cover Mid:</span>
                                <span>{data?.cloudCoverMid}%</span>
                            </div>
                            <div className="canada-weather-data-row">
                                <span>Cloud Cover Low:</span>
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

/**
 * ToggleControl Component
 *
 * Reusable component for toggle switches
 *
 * @param {boolean} checked - Current toggle state
 * @param {Function} onChange - Function to handle toggle state changes
 * @param {string} label - Text label for the toggle
 * @returns {JSX.Element} Toggle switch with label
 */
const ToggleControl = ({ checked, onChange, label }) => (
    <label className="canada-weather-toggle-label">
        <input
            type="checkbox"
            checked={checked}
            onChange={onChange}
            className="canada-weather-toggle-input"
        />
        <span className="canada-weather-toggle-slider"></span>
        <span className="canada-weather-toggle-text">{label}</span>
    </label>
);

/**
 * Main CanadaMap Component
 *
 * Parent component that manages the entire weather map application
 * Handles state management, data fetching, and renders child components
 *
 * @returns {JSX.Element} Complete weather map application
 */
const CanadaMap = () => {
    // State declarations with detailed comments
    // Weather data for all cities
    const [weatherData, setWeatherData] = useState({});

    // Map of city names to their coordinates
    const [cities, setCities] = useState({});

    // Error state for API fetch failures
    const [error, setError] = useState(null);

    // Current search input text
    const [searchTerm, setSearchTerm] = useState('');

    // List of cities filtered by current search term
    const [filteredCities, setFilteredCities] = useState([]);

    // Currently selected city (null if none)
    const [selectedCity, setSelectedCity] = useState(null);

    // Toggle for cloud coverage visualization layer
    const [showCloudLayer, setShowCloudLayer] = useState(false);

    // Toggle for light pollution visualization layer
    const [showLightPollution, setShowLightPollution] = useState(false);

    // Loading state for initial data fetch
    const [isLoading, setIsLoading] = useState(true);

    /**
     * Fetch initial data from API endpoints
     * Gets city coordinates and weather data
     */
    useEffect(() => {
        const abortController = new AbortController();
        let isSubscribed = true;

        const fetchData = async () => {
            try {
                setIsLoading(true);

                // API endpoints
                const citiesApiPath = '/api/weather/cities';
                const weatherApiPath = '/api/weather/all';

                // Fetch cities data with coordinates
                const citiesResponse = await fetch(citiesApiPath, {
                    signal: abortController.signal
                });

                if (!citiesResponse.ok) {
                    throw new Error('Failed to fetch cities');
                }

                const citiesData = await citiesResponse.json();
                if (isSubscribed) {
                    setCities(citiesData);
                }

                // Fetch weather data for all cities
                const weatherResponse = await fetch(weatherApiPath, {
                    signal: abortController.signal
                });

                if (!weatherResponse.ok) {
                    throw new Error('Failed to fetch weather data');
                }

                const weatherList = await weatherResponse.json();

                if (isSubscribed) {
                    // Convert array to object with city names as keys for faster lookups
                    const weatherMap = {};
                    weatherList.forEach(weather => {
                        weatherMap[weather.cityName] = weather;
                    });
                    setWeatherData(weatherMap);
                }

                setIsLoading(false);
            } catch (error) {
                // Ignore abort errors (caused by component unmounting)
                if (error.name === 'AbortError') {
                    return;
                }

                console.error('Error fetching data:', error);
                setError(error.message);
                setIsLoading(false);
            }
        };

        fetchData();

        // Cleanup function to abort fetch requests if component unmounts
        return () => {
            isSubscribed = false;
            abortController.abort();
        };
    }, []);

    /**
     * Filter cities based on search term
     * Updates filtered city list whenever search term changes
     */
    useEffect(() => {
        // Clear filtered list if search term is empty
        if (searchTerm.trim() === '') {
            setFilteredCities([]);
            return;
        }

        // Find cities that include the search term (case insensitive)
        const filtered = Object.keys(cities).filter(city =>
            city.toLowerCase().includes(searchTerm.toLowerCase())
        );

        setFilteredCities(filtered);
    }, [searchTerm, cities]);

    /**
     * Handle city selection from search or map
     * Updates selected city and clears search inputs
     *
     * @param {string} cityName - Name of city to select
     */
    const handleCitySelect = (cityName) => {
        setSelectedCity(cityName);
        setSearchTerm('');
        setFilteredCities([]);
    };

    /**
     * Clear city selection when clicking on empty map area
     */
    const handleMapClick = () => {
        setSelectedCity(null);
    };

    return (
        <div className="canada-weather-map-module">
            {/* Background and overlay elements */}
            <div className="canada-weather-bg"></div>
            <div className="canada-weather-overlay"></div>

            {/* Error banner - only shows if there's an error */}
            {error && (
                <div className="canada-weather-error-banner">
                    Error: {error}. Please check your API configuration.
                </div>
            )}

            {/* Top controls - search and toggles */}
            <div className="canada-weather-controls">
                <SearchBox
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    filteredCities={filteredCities}
                    handleCitySelect={handleCitySelect}
                />

                <div className="canada-weather-toggle-container">
                    <ToggleControl
                        checked={showCloudLayer}
                        onChange={(e) => setShowCloudLayer(e.target.checked)}
                        label="Show Cloud Layer"
                    />
                    <ToggleControl
                        checked={showLightPollution}
                        onChange={(e) => setShowLightPollution(e.target.checked)}
                        label="Show Light Pollution Layer"
                    />
                </div>
            </div>

            {/* Map container */}
            <div className="canada-weather-map-wrapper">
                <div className="canada-weather-map-container">
                    {!isLoading && Object.keys(cities).length > 0 ? (
                        <MapContainer {...MAP_CONFIG}>
                            <MapController selectedCity={selectedCity} cities={cities} />
                            <MapClickHandler onMapClick={handleMapClick} />

                            {/* Base map layer - dark style */}
                            <TileLayer
                                url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
                                attribution='&copy; <a href="https://carto.com/">CartoDB</a> contributors'
                                noWrap={true}
                                bounds={MAP_CONFIG.maxBounds}
                            />

                            {/* Optional OpenWeatherMap cloud coverage layer */}
                            {showCloudLayer && (
                                <TileLayer
                                    url="https://tile.openweathermap.org/map/clouds_new/{z}/{x}/{y}.png?appid=9c19f2e271b07792f731a738e41f1891"
                                    opacity={0.5}
                                    attribution='&copy; OpenWeatherMap'
                                />
                            )}

                            {/* Optional light pollution layer */}
                            <LightPollutionLayer showLightPollution={showLightPollution} />

                            {/* City markers - only renders for selected city */}
                            {Object.entries(cities).map(([city, pos]) => (
                                <CityPoint
                                    key={city}
                                    city={city}
                                    pos={pos}
                                    selected={city === selectedCity}
                                    data={weatherData[city]}
                                    showCloudLayer={showCloudLayer}
                                    showLightPollution={showLightPollution}
                                    handleCitySelect={handleCitySelect}
                                />
                            ))}
                        </MapContainer>
                    ) : (
                        <div className="canada-weather-loading-indicator">
                            {isLoading ? "Loading map data..." : "No data available. Please check API connection."}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default CanadaMap;