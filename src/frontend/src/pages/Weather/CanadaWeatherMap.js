import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import '../../styles/Weather/CanadaMap.css';
import { MAP_CONFIG, API_PATHS } from '../../utils/mapConfig';
import { convertWeatherListToMap, filterCitiesBySearchTerm } from '../../utils/CityUtils';
import SearchBox from '../../components/Weather/SearchBox';
import ToggleControl from '../../components/Weather/ToggleControl';
import LightPollutionLayer from '../../components/Weather/LightPollutionLayer';
import CityPoint from '../../components/Weather/CityPoint';
import { MapController, MapClickHandler } from '../../components/Weather/MapController';

/**
 * canadaWeatherMap Component
 * manages the entire weather map application
 * state management, data fetching, and renders child components
 * @returns {JSX.Element} Complete weather map with functions
 */
const CanadaWeatherMap = () => {
    //weather and city from backend
    const [weatherData, setWeatherData] = useState({});
    const [cities, setCities] = useState({});

    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    //for search box
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCity, setSelectedCity] = useState(null);
    const [filteredCities, setFilteredCities] = useState([]);

    //toggle for cloud and pollution layer
    const [showCloudLayer, setShowCloudLayer] = useState(false);
    const [showLightPollution, setShowLightPollution] = useState(false);


    /**
     * fetch weather data from API endpoints and store into cities and weather array
     */
    useEffect(() => {
        const abortController = new AbortController();
        let isSubscribed = true;

        const fetchData = async () => {
            try {
                setIsLoading(true);

                //get city coordinate data
                const citiesResponse = await fetch(API_PATHS.cities, {
                    signal: abortController.signal
                });

                if (!citiesResponse.ok) {
                    throw new Error('Failed to fetch city data');
                }

                const citiesData = await citiesResponse.json();
                if (isSubscribed) {
                    setCities(citiesData);
                }

                //get weather data for all cities
                const weatherResponse = await fetch(API_PATHS.weatherData, {
                    signal: abortController.signal
                });

                if (!weatherResponse.ok) {
                    throw new Error('Failed to fetch weather data');
                }

                const weatherList = await weatherResponse.json();

                if (isSubscribed) {
                    //convert array to object with city names as keys for faster lookups
                    const weatherMap = convertWeatherListToMap(weatherList);
                    setWeatherData(weatherMap);
                }

                setIsLoading(false);
            } catch (error) {
                //ignore abort errors (caused by component unmounting)
                if (error.name === 'AbortError') {
                    return;
                }

                console.error('Error fetching data:', error);
                setError(error.message);
                setIsLoading(false);
            }
        };

        fetchData();

        //cleanup function
        return () => {
            isSubscribed = false;
            abortController.abort();
        };
    }, []);

    /**
     * filter cities based on search term
     * update filtered city list whenever search term changes
     */
    useEffect(() => {
        const filtered = filterCitiesBySearchTerm(cities, searchTerm);
        setFilteredCities(filtered);
    }, [searchTerm, cities]);

    /**
     * handle city selection from search or and show selected city and clear search input
     *
     * @param {string} cityName - The name of the city to select
     */
    const handleCitySelect = (cityName) => {
        setSelectedCity(cityName);
        setSearchTerm('');
        setFilteredCities([]);
    };

    /**
     * clear city selection when clicking on empty map area
     */
    const handleMapClick = () => {
        setSelectedCity(null);
    };

    return (
        <div className="canada-weather-map-module">
            {/* background */}
            <div className="canada-weather-bg"></div>
            <div className="canada-weather-overlay"></div>

            {/* for show error */}
            {error && (
                <div className="canada-weather-error-banner">
                    Error: {error}. Please check your API configuration.
                </div>
            )}

            {/* search and toggles */}
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

            {/* map container */}
            <div className="canada-weather-map-wrapper">
                <div className="canada-weather-map-container">
                    {!isLoading && Object.keys(cities).length > 0 ? (
                        <MapContainer {...MAP_CONFIG}>
                            <MapController selectedCity={selectedCity} cities={cities} />
                            <MapClickHandler onMapClick={handleMapClick} />

                            {/* map layer */}
                            <TileLayer
                                url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"
                                attribution='&copy; <a href="https://carto.com/">CartoDB</a> contributors'
                                noWrap={true}
                                bounds={MAP_CONFIG.maxBounds}
                            />

                            {/* optional cloud coverage layer */}
                            {showCloudLayer && (
                                <TileLayer
                                    url="https://tile.openweathermap.org/map/clouds_new/{z}/{x}/{y}.png?appid=9c19f2e271b07792f731a738e41f1891"
                                    opacity={0.5}
                                    attribution='&copy; OpenWeatherMap'
                                />
                            )}

                            {/* optional light pollution layer */}
                            <LightPollutionLayer showLightPollution={showLightPollution} />

                            {/* Rendered for the selected city */}
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

export default CanadaWeatherMap;