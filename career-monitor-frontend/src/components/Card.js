import React, { useState, useEffect } from "react";
import Heatmap from "./YearlyHeatmap";
import "./Card.css";

function Card({ data }) {
  const currentYear = new Date().getFullYear();
  const [selectedYear, setSelectedYear] = useState(currentYear);
  const [filteredData, setFilteredData] = useState(data);

  const handleYearChange = (e) => {
    setSelectedYear(parseInt(e.target.value, 10));
  };

  useEffect(() => {
    let filtered = [];

    if (selectedYear === currentYear) {
      // Calculate the date one year ago from today
      const oneYearAgo = new Date();
      oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);

      // Filter data to include only entries from the past year
      filtered = data.filter(
        (entry) =>
          new Date(entry.date) >= oneYearAgo &&
          new Date(entry.date) <= new Date()
      );
    } else {
      // Filter data for the selected year (e.g., 2022)
      filtered = data.filter(
        (entry) => new Date(entry.date).getFullYear() === selectedYear
      );
    }

    setFilteredData(filtered);
  }, [selectedYear, data, currentYear]);

  return (
    <div className="card">
      <div className="card-content">
        <div className="card-body">
          <Heatmap data={filteredData} year={selectedYear} />
        </div>
        <div className="card-footer">
          <div className="card-footer-left">
            <p>{/* {filteredData.length} submissions in {selectedYear} */}</p>
          </div>
          <div className="card-footer-right">
            <p>Total active days: {filteredData.length}</p>
            <p></p>
            <select value={selectedYear} onChange={handleYearChange}>
              <option value={currentYear}>Current</option>
              <option value={currentYear - 1}>{currentYear - 1}</option>
              <option value={currentYear - 2}>{currentYear - 2}</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Card;
