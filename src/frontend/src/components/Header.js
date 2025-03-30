import React from 'react';
import { Link } from 'react-router-dom';
import "../styles/globals.css"
function Header() {
    const breadcrumbItems = [
        { name: 'Home', to: '/' },
        { name: 'Meteor Shower', to: '/meteor_shower' },
        { name: 'Aurora', to: '/aurora' },
        { name: 'Eclipse', to: '/eclipse' },
        { name: 'Weather/Light Pollution Map', to: '/weatherMap' }
    ];

    return (
        <header className="header">
            <nav>
                <ul className="breadcrumb">
                    {breadcrumbItems.map((item, index) => (
                        <li key={index}>
                            <Link to={item.to}>{item.name}</Link>
                        </li>
                    ))}
                </ul>
            </nav>
        </header>
    );
}

export default Header;
