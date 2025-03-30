//Unit Testing
//use Jest and React Testing Library
import React from 'react';
import { render, screen, act, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import CanadaWeatherMap from './CanadaWeatherMap';

//mock react-leaflet components to avoid map rendering issues
jest.mock('react-leaflet', () => ({
    MapContainer: ({ children }) => <div data-testid="map-container">{children}</div>,
    TileLayer: () => <div data-testid="tile-layer" />,
}));

describe('CanadaWeatherMap Component GUI Test', () => {
    //avoid show error and disrupt ci
    beforeEach(() => {
        jest.spyOn(console, 'error').mockImplementation(() => {});
    });

    afterEach(() => {
        console.error.mockRestore();
    });

    //test the searchbox, when no city be typed in
    it('need to render search box with correct placeholder', async () => {
        await act(async () => {
            render(<CanadaWeatherMap />);
        });

        const searchInput = screen.getByPlaceholderText('Search Cities');
        expect(searchInput).toBeInTheDocument();
        expect(searchInput).toHaveValue('');
    });

    //test cloud and light pollution check box label and render
    it('need to render both toggle buttons with correct labels', async () => {
        await act(async () => {
            render(<CanadaWeatherMap />);
        });

        const cloudLayerToggle = screen.getByText('Show Cloud Layer');
        const lightPollutionToggle = screen.getByText('Show Light Pollution Layer');

        expect(cloudLayerToggle).toBeInTheDocument();
        expect(lightPollutionToggle).toBeInTheDocument();
    });

    //test the interaction of checkbox
    it('need to toggle cloud layer when clicked', async () => {
        await act(async () => {
            render(<CanadaWeatherMap />);
        });

        const cloudLayerCheckbox = screen.getByLabelText('Show Cloud Layer');

        //init state
        expect(cloudLayerCheckbox).not.toBeChecked();

        //mock click the checkbox
        await act(async () => {
            fireEvent.click(cloudLayerCheckbox);
        });

        //check the state after click checkbox
        expect(cloudLayerCheckbox).toBeChecked();
    });

    //test if fail to get api
    it('need to display error message when API fails', async () => {
        //mock fail
        global.fetch = jest.fn().mockRejectedValueOnce(new Error('Failed to fetch city data'));

        await act(async () => {
            render(<CanadaWeatherMap />);
        });

        //test if error correct
        const errorMessage = screen.getByText('No data available. Please check API connection.');
        expect(errorMessage).toBeInTheDocument();
    });

    //test search box with city name
    it('need to update search input value when typing', async () => {
        await act(async () => {
            render(<CanadaWeatherMap />);
        });

        const searchInput = screen.getByPlaceholderText('Search Cities');

        //mock type in city name
        await act(async () => {
            fireEvent.change(searchInput, { target: { value: 'Toronto' } });
        });

        //chec if changed
        expect(searchInput).toHaveValue('Toronto');
    });

    //test render of map
    it('need to render map container', async () => {
        await act(async () => {
            render(<CanadaWeatherMap />);
        });

        const { container } = render(<CanadaWeatherMap />);
        const mapContainer = container.getElementsByClassName('canada-weather-map-container')[0];
        expect(mapContainer).toBeInTheDocument();
    });
});