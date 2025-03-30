import React from 'react';

/**
 * ToggleControl for cloud and pollution layer
 *
 * @param {boolean} checked - state of toggle
 * @param {Function} onChange - function to handle
 * @param {string} label
 * @returns {JSX.Element}
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

export default ToggleControl;