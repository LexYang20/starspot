import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Home from "./pages/Home";
import MeteorShowerPage from "./pages/MeteorShowerPage";
import AuroraPage from "./pages/AuroraPage";
import EclipsePage from "./pages/EclipsePage";
import axios from "axios";
import './index.css';
import CanadaMap from "./pages/Weather/CanadaWeatherMap";

function App() {
    const [inputValue, setInputValue] = useState("");

    useEffect(() => {
        axios
            .get("/api/mock-data")
            .then((response) => {
                setInputValue(response.data.message);
            })
            .catch((error) => {
                console.error("Error fetching data:", error);
            });
    }, []);

    return (
        <Router>
            <div className="app">
                <Header />
                <main className="main-content">
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/meteor_shower" element={<MeteorShowerPage />} />
                        <Route path="/aurora" element={<AuroraPage />} />
                        <Route path="/eclipse" element={<EclipsePage />} />
                        <Route path="/weatherMap" element={<CanadaMap />} />
                    </Routes>
                </main>
                <Footer />
            </div>
        </Router>
    );
}

export default App;