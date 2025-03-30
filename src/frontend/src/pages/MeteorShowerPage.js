import React, {useEffect, useState} from 'react';
import { useLocation } from 'react-router-dom';
import EclipseMap from "../components/EclipseMap";
import "../styles/meteorShowerPage.css"

const MeteorShowerPage = () => {
    // User's current location (lat/lng)
    const [userLocation, setUserLocation] = useState(null);

    // A list of observation points to display on the map
    const [observations, setObservations] = useState([]);

    // Recommended city names to display in the right panel
    const [recommendedCities, setRecommendedCities] = useState([]);

    // Current bounds of the map (a Leaflet LatLngBounds object), updated on map move
    const [mapBounds, setMapBounds] = useState(null);

    // Use useLocation to get current URL
    const location = useLocation();
    // User URLSearchParams to parse the query string
    const queryParams = new URLSearchParams(location.search);
    const eventId = queryParams.get('event_id');
    const [selectedEventId, setSelectedEventId] = useState(eventId || '');

    const [meteorShowers, setMeteorShowers] = useState([]);
    useEffect(() => {

        const fetchMeteorShowers = async () => {
            const response = await fetch('/api/meteorshowers');
            const data = await response.json();
            console.log('Fetched meteor showers:', data);
            setMeteorShowers(data);
        };

        fetchMeteorShowers();

        // Attempt to get the user's geolocation from the browser
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    const location = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    };
                    setUserLocation(location);
                },
                (error) => {
                    console.error('Error fetching user location:', error);
                    // If geolocation fails, use Toronto as a default
                    setUserLocation({ lat: 43.65107, lng: -79.347015 });
                }
            );
        } else {
            console.error('Geolocation is not supported by this browser.');
            setUserLocation({ lat: 43.65107, lng: -79.347015 });
        }
    }, []);

    // Handle clicking the "Recommend Observation Points" button
    const handleRecommendClick = async () => {
        console.log('Button clicked, showing recommended cities.');

        setRecommendedCities([
            'Waterloo',
            'Kitchener',
            'Cambridge',
            'Guelph',
            'St. Jacobs Park'
        ]);
        const recommendedPoints = [
            { lat: 43.4643, lng: -80.5204, name: 'Waterloo' },  // Waterloo
            { lat: 43.2561, lng: -79.8729, name: 'Kitchener' }, // Kitchener
            { lat: 43.3616, lng: -80.3144, name: 'Cambridge' }, // Cambridge
            { lat: 43.5448, lng: -80.2482, name: 'Guelph' },    // Guelph
            { lat: 43.1832, lng: -80.3843, name: 'St. Jacobs Park' } // St. Jacobs Park
        ];
        // **如果 `mapBounds` 为空，则跳过地图观测点处理**
        if (!mapBounds) {
            console.warn('No map bounds available yet. Skipping observation points.');
            return;
        }

        console.log('Current map bounds:', mapBounds);

        try {
            //   const recommendedPoints = await getEclipseObservations(mapBounds);
            //   console.log('Recommended Points:', recommendedPoints);

            //   if (!Array.isArray(recommendedPoints) || recommendedPoints.some(p => isNaN(p.lat) || isNaN(p.lng))) {
            //     console.error('Invalid observation data:', recommendedPoints);
            //     return;
            //   }

            setObservations(recommendedPoints);
        } catch (error) {
            console.error('Error fetching observations:', error);
        }
    };

    const handleSelectChange = (event) => {
        setSelectedEventId(event.target.value);
    };


    // 4) Mock recommended cities near Waterloo (or any region)
    //    This simulates the backend returning city names based on the bounding box



    return (
        <div className="h-screen bg-gray-900 text-white flex flex-row items-stretch p-4">
            {/* Left side: Map container */}
            <div className="w-2/3 border-2 border-red-500" style={{ height: '600px' }}>
                <EclipseMap
                    userLocation={userLocation}
                    observations={observations}
                    setMapBounds={setMapBounds}
                />
            </div>

            {/* Right side: Panel */}
            <div className="w-1/3 pl-4 flex flex-col justify-start space-y-4">
                <div className="event-picker" > Please select an event:
                <select value={selectedEventId} onChange={handleSelectChange}>
                    <option value="" disabled>Select an event</option>
                    {meteorShowers.map((shower) => (
                        <option key={shower.eventId} value={shower.eventId}>
                            {/*Use UTC time not local time*/}
                            {`METEOR on ${new Date(shower.peakTime * 1000).toISOString().split('T')[0]}`}
                        </option>
                    ))}
                </select>
                </div>

                <button className={"recommend-btn"}
                    onClick={handleRecommendClick}
                >
                    Recommend Observation Points
                </button>

                {/* Dynamic Recommended Cities table */}
                <div className="bg-gray-800 p-4 rounded-lg shadow-lg">
                    <h2 className="text-lg font-semibold mb-2">Recommended Cities</h2>
                    <ul>
                        {recommendedCities.length > 0 ? (
                            recommendedCities.map((city, index) => (
                                <li key={index}>{city}</li>
                            ))
                        ) : (
                            <li>No recommendations yet.</li>
                        )}
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default MeteorShowerPage;
