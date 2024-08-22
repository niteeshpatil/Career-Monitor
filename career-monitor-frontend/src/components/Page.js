import React, { useEffect, useState } from "react";
import WelcomeDashboard from "./Dashboard";
import Card from "./Card";
import "./Page.css"; // Import the CSS file

const Page = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          "http://localhost:8081/links/allDataEntry"
        );
        const result = await response.json();
        setData(result);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchData();
  }, []); // Empty dependency array ensures this runs once on mount

  return (
    <div className="page-container">
      <WelcomeDashboard className="welcome-dashboard" />
      <Card data={data} />
    </div>
  );
};

export default Page;
