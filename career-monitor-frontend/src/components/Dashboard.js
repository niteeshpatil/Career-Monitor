import React, { useEffect, useState } from "react";

function WelcomeDashboard({ username }) {
  const [links, setLinks] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    console.log("Fetching data from API..."); // Debug log

    fetch("http://localhost:8081/links/allLinks")
      .then((response) => {
        console.log("Response status:", response.status); // Debug log
        if (!response.ok) {
          throw new Error("Network response was not ok " + response.statusText);
        }
        return response.text(); // Get raw text
      })
      .then((text) => {
        console.log("Raw response text:", text); // Debug log
        try {
          const data = JSON.parse(text); // Parse JSON text
          setLinks(data); // Set parsed data into state
        } catch (error) {
          console.error("JSON parsing error:", error); // Debug log
          setError(new Error("Error parsing JSON"));
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error); // Debug log
        setError(error);
      });
  }, []);

  return (
    <div className="d-flex justify-content-center align-items-center vh-100">
      <div
        className="border rounded-lg p-4"
        style={{ width: "500px", height: "400px" }}
      >
        <h2 className="mb-4 text-center">Welcome to Dashboard</h2>
        <p className="mb-4 text-center">Hello, {username}!</p>
        <p className="text-center">You are logged in successfully.</p>
        <div className="text-center">
          {error ? (
            <p className="text-danger">Error: {error.message}</p>
          ) : (
            <ul>
              {links.map((link) => (
                <li key={link.id}>
                  <a href={link.link} target="_blank" rel="noopener noreferrer">
                    {link.company}
                  </a>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}

export default WelcomeDashboard;
