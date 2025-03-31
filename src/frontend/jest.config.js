export default {
    testEnvironment: "jsdom",
    transform: {
        "^.+\\.(js|jsx|ts|tsx)$": "babel-jest",
    },
    moduleNameMapper: {
        "^react-leaflet$": "<rootDir>/node_modules/react-leaflet/dist/react-leaflet.min.js"
    },
    transformIgnorePatterns: [
        "/node_modules/(?!(react-leaflet|leaflet)/)"
    ]
};
