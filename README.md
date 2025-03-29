# Astronomy Event Observation Recommendation Project

A platform for recommending astronomy event observation spots based on **React + Spring Boot**. Users can select a geographic area on an interactive map, and the backend will recommend the best cities for observation based on **astronomical event data, weather conditions, and light pollution estimates**. The platform supports events such as **meteor showers, auroras, solar and lunar eclipses**, helping users quickly locate the best observation spots.

## Table of Contents
- Project Overview
- Features
- Tech Stack
- Project Structure
- Setup & Deployment
- Usage Guide
- Future Plans
- Contribution Guide

## Project Overview
This project aims to address the challenge of **"how to quickly find the best observation locations for astronomical events."** The system returns a list of recommended cities with their geographic coordinates, allowing the frontend to highlight them on the map. Through this process, users can receive optimal observation recommendations even if they are unfamiliar with the area.

## Features
- **Interactive Map**: Users can zoom, pan, and select the desired observation area via the **Leaflet-based map**.
- **Recommendation Logic**: The backend filters observation cities based on **astronomical event dates, cloud coverage, and light pollution levels**.
- **Event Support**: Includes recommendations and information for **meteor showers, auroras, solar and lunar eclipses**.
- **Weather & Light Pollution Analysis**: Displays weather conditions and estimated light pollution to enhance observation accuracy.
- **Scalable Data Source**: The backend uses **Firebase (Firestore / Realtime Database)** for storing and retrieving data, but it can be replaced with other databases.
- **City Visualization**: Recommended cities are marked in **red** on the map for intuitive display.

## Tech Stack
- **Frontend**: React, Leaflet
- **Backend**: Spring Boot (Java 17), Gradle
- **Database**: Firebase Admin SDK (Firestore + Realtime Database)
- **Tools & Libraries**:
  - Lombok (Simplifies Java Bean and boilerplate code)
  - OpenCSV (For parsing CSV files if needed)
  - Other common Spring Boot dependencies

## Project Structure
The project follows a **React frontend + Spring Boot backend** layered architecture. Example structure:

```
.
├── src
│   ├── frontend
│   │   ├── package.json
│   │   ├── src
│   │   │   ├── components          # Common components
│   │   │   ├── pages               # Page components
│   │   │   └── utils               # Helper functions, e.g., fetchEvents
│   │   └── public
│   ├── main
│   │   ├── java
│   │   │   └── starspot
│   │   │        ├── StarSpotApplication.java  # Spring Boot application entry
│   │   │        ├── controller
│   │   │        ├── service
│   │   │        ├── repository
│   │   │        └── model
│   │   ├── resources
│   │   │   └── application.yml / application.properties
│   ├── sql  # SQL scripts or data management
│   ├── test/java/starspot  # Unit and integration tests
```
- **Controller**: Provides REST API endpoints, processes requests, and returns responses to the frontend.
- **Service**: Contains business logic, such as filtering observable cities based on geographic bounds, event dates, weather conditions, and light pollution levels.
- **Repository**: Handles data access, querying city and astronomical event data from Firebase.
- **Model**: Includes entity classes like `City`, `MeteorShowerEvent`, `AuroraEvent`, `EclipseEvent`, and `ObservationConditions` for data exchange.

## Setup & Deployment
### **Frontend Requirements**
- Node.js (Recommended **16+**)
- npm or yarn

### **Backend Requirements**
- Java 17
- Gradle
- Network access to Firebase (if using Firebase as the database)

### **Deployment Steps**
#### **1. Clone the Project**
```bash
git clone <repository_url>
cd <project_directory>
```
#### **2. Start the Frontend**
Navigate to the `frontend` folder:
```bash
cd src/frontend
```
Install dependencies:
```bash
npm install
# or
yarn
```
Start the development server:
```bash
npm run start
# or
yarn start
```
By default, the frontend will run at **http://localhost:3000**.

#### **3. Start the Backend**
Navigate to the `main` backend folder:
```bash
cd src/main
```
Configure Firebase credentials and other settings in `src/main/resources/application.yml` or `application.properties` if needed.

Run the backend:
```bash
gradle bootRun
```
By default, the backend will start at **http://localhost:8080**.

## Usage Guide
1. Open the frontend map interface. The system will automatically detect the user's location or set a default city as the starting point.
2. **Zoom and pan** the map to select a region. The frontend will calculate the geographic bounds (`minLat, maxLat, minLng, maxLng`).
3. Click the **"Recommend Observation Points"** button to send a request to the backend:
   - **API Path**: `/api/eclipse/recommend`
   - **Method**: `POST` (or `GET`)
   - **Parameters**: Geographic bounds, event date, etc.
4. **Backend Processing Logic**:
   - Validate if the request falls within valid astronomical event dates.
   - Query city information from **Firebase** (or another database).
   - Filter cities with **cloud coverage < 50%** and low light pollution.
   - Return a list of suitable observation locations.
5. **Frontend renders results**:
   - Display recommended cities as **red markers** on the map.
   - Show a list of recommended city names in the sidebar.

## Future Plans
- **Support for Additional Events**: Extend recommendations to include **comets, supernovae, and other celestial phenomena**.
- **Advanced Weather Analysis**: Include parameters like **temperature, humidity, and atmospheric transparency**.
- **Real-time Light Pollution Estimation**: Incorporate data sources for **current and forecasted light pollution levels**.
- **Geographic Selection Enhancements**: Enable users to select areas using **polygon or rectangle drawing tools**.
- **Error Handling & Suggestions**: Provide user-friendly messages and automatic suggestions if the selected area has no results.

## Contribution Guide
We welcome contributions! You can:
- **Submit an Issue** to discuss improvements or report bugs.
- **Fork the repository** and open a Pull Request.
- **Contact the maintainers**.

