import React, { useState } from "react";
import "./Popup.css";

function Popup({ onClose }) {
  const [company, setCompany] = useState("");
  const [link, setLink] = useState("");
  const [error, setError] = useState(null);

  const handleSubmit = (event) => {
    event.preventDefault();

    const data = {
      company,
      link,
    };

    fetch("http://localhost:8081/links/addLinks", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        if (response.ok) {
          const contentType = response.headers.get("Content-Type");
          if (contentType && contentType.includes("application/json")) {
            return response.json();
          } else {
            return response.text();
          }
        } else {
          return response.text().then((text) => {
            throw new Error(text || response.statusText);
          });
        }
      })
      .then((responseData) => {
        if (typeof responseData === "string") {
          console.log(responseData);
        }
        setCompany("");
        setLink("");
        onClose();
      })
      .catch((error) => {
        console.error("Fetch error:", error);
        setError(error);
      });
  };

  return (
    <div className="popup-overlay" onClick={onClose}>
      <div className="popup-content" onClick={(e) => e.stopPropagation()}>
        <h3>Add New Link</h3>
        {error && <p className="text-danger">Error: {error.message}</p>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="company">Company</label>
            <input
              type="text"
              id="company"
              value={company}
              onChange={(e) => setCompany(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="link">Link</label>
            <input
              type="url"
              id="link"
              value={link}
              onChange={(e) => setLink(e.target.value)}
              required
            />
          </div>
          <div className="button-group">
            <button type="submit" className="btn-add">
              Add
            </button>
            <button type="button" className="btn-close" onClick={onClose}>
              Close
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Popup;
