/**
 * a convert let city name become key of map, convinient to search
 *
 * @param {Array} weatherList - weather array include informaiton of weather and cities
 * @returns {Object} map use cityname as key
 */
export const convertWeatherListToMap = (weatherList) => {
    const weatherMap = {};

    if (!Array.isArray(weatherList)) {
        console.error('Weather data is not an array', weatherList);
        return {};
    }

    weatherList.forEach(weather => {
        if (weather && weather.cityName) {
            weatherMap[weather.cityName] = weather;
        }
    });

    return weatherMap;
};

/**
 * for search case to show the filtered cities base on search city name
 *
 * @param {Object} cities - array of cities
 * @param {string} searchTerm - search city
 * @returns {Array} array after filter
 */
export const filterCitiesBySearchTerm = (cities, searchTerm) => {
    if (!searchTerm || searchTerm.trim() === '') {
        return [];
    }

    return Object.keys(cities).filter(city =>
        city.toLowerCase().includes(searchTerm.toLowerCase())
    );
};