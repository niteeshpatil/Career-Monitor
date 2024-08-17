import React from "react";
import Card from "./components/Card"; // Import the Card component
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// Example data for the heatmap
const data = [
  { date: "2024-01-01", count: 5 },
  { date: "2024-01-15", count: 3 },
  { date: "2024-02-20", count: 2 },
  { date: "2023-04-01", count: 5 },
  { date: "2023-03-15", count: 3 },
  { date: "2023-02-20", count: 2 },
  { date: "2022-01-12", count: 5 },
  { date: "2022-05-20", count: 3 },
  { date: "2022-06-04", count: 2 },
  // Add more data points as needed
];

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Card data={data} />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
