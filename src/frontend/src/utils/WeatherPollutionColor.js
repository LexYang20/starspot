/**
 * function for cloud render, depend on the degree of cloud coverage return different color
 *
 * @param {number} coverage
 * @returns {string} color
 */
export const getCloudColor = (coverage) => {
    if (coverage >= 80) return '#0d47a1';
    if (coverage >= 60) return '#1976d2';
    if (coverage >= 40) return '#42a5f5';
    if (coverage >= 20) return '#90caf9';
    return '#bbdefb';
};

/**
 * same as cloud, have 4 degree
 *
 * @returns {string} color
 */
export const getLightPollutionColor = (lightPollution) => {
    switch (lightPollution) {
        case 1:
            return '#FFCCCC';
        case 2:
            return '#FF9999';
        case 3:
            return '#FF6666';
        case 4:
            return '#FF0000';
        default:
            return '#AAAAAA';
    }
};