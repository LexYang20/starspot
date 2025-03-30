import { useEffect, useRef } from 'react';
import { useMap } from 'react-leaflet';
import L from 'leaflet';

/**
 * LightPollutionLayer
 *
 * show light layer or clean when checkbox state change
 *
 * @param {boolean} showLightPollution - toggle
 * @returns {null}
 */
const LightPollutionLayer = ({ showLightPollution }) => {
    const map = useMap();
    const lightPollutionLayerRef = useRef(null);

    useEffect(() => {
        if (showLightPollution) {
            // if no layer create
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
            //if have and checkbox is false clean layer
            if (lightPollutionLayerRef.current) {
                map.removeLayer(lightPollutionLayerRef.current);
                lightPollutionLayerRef.current = null;
            }
        }

        //clean
        return () => {
            if (lightPollutionLayerRef.current) {
                map.removeLayer(lightPollutionLayerRef.current);
            }
        };
    }, [map, showLightPollution]);

    return null;
};

export default LightPollutionLayer;