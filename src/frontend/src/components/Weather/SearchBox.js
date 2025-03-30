import React from 'react';

/**
 * SearchBox
 *
 * @param {string} searchTerm
 * @param {Function} setSearchTerm
 * @param {Array} filteredCities
 * @param {Function} handleCitySelect
 * @returns {JSX.Element} with a result of filtered cities
 */
const SearchBox = ({ searchTerm, setSearchTerm, filteredCities, handleCitySelect }) => (
    <div className="canada-weather-search-container">
        <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Search Cities"
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

export default SearchBox;