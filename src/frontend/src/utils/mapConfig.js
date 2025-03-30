/**
 * canada map config
 * - center: canada center
 * - maxBounds: canada bound
 * - maxBoundsViscosity: Control the "hardness" of map boundaries (1.0 = no translation outside boundaries)
 */
export const MAP_CONFIG = {
    center: [56.130366, -106.346771],
    zoom: 4,
    maxBounds: [
        [40, -150],
        [85, -50]
    ],
    minZoom: 3,
    maxZoom: 6,
    maxBoundsViscosity: 1.0
};

/**
 * API TO connect with backend
 */
export const API_PATHS = {
    cities: '/api/weather/cities',
    weatherData: '/api/weather/all'
};