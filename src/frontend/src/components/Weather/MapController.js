import { useEffect } from 'react';
import { useMap, useMapEvents } from 'react-leaflet';

/**
 * MapController
 * position the city to the center of map
 *
 * @param {string} selectedCity - selected city name
 * @param {Object} cities - include long/la of city
 * @returns {null}
 */
export const MapController = ({ selectedCity, cities }) => {
    const map = useMap();

    useEffect(() => {
        if (selectedCity && cities[selectedCity]) {
            map.setView(cities[selectedCity], 8);
        }
    }, [selectedCity, cities, map]);

    return null;
};

/**
 * MapClickHandler
 * if user click on the white space rollback
 */
export const MapClickHandler = ({ onMapClick }) => {
    useMapEvents({
        click: (e) => {
            const target = e.originalEvent.target;
            //only when user click the uninteraction element
            if (!target.classList?.contains('leaflet-interactive') &&
                !target.closest('.leaflet-tooltip')) {
                onMapClick();
            }
        },
    });
    return null;
};

export default { MapController, MapClickHandler };