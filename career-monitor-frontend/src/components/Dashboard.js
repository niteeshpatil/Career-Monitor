import React, { useEffect, useState } from "react";
import "./WelcomeDashboard.css"; // Ensure this CSS file is updated as well
import Popup from "./Popup"; // Import the Popup component
import ConfirmationPopup from "./ConfirmationPopup"; // Import the ConfirmationPopup component

function WelcomeDashboard({ username }) {
  const [links, setLinks] = useState([]);
  const [filteredLinks, setFilteredLinks] = useState([]);
  const [error, setError] = useState(null);
  const [showPopup, setShowPopup] = useState(false);
  const [showConfirmationPopup, setShowConfirmationPopup] = useState(false); // State for confirmation popup
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedLinks, setSelectedLinks] = useState(new Set()); // Track selected links
  const [showDeleteIcon, setShowDeleteIcon] = useState(false); // Track visibility of delete icon
  const [isEditMode, setIsEditMode] = useState(false); // Track edit mode

  useEffect(() => {
    console.log("Fetching data from API...");

    fetch("http://localhost:8081/links/allLinks")
      .then((response) => {
        console.log("Response status:", response.status);
        if (!response.ok) {
          throw new Error("Network response was not ok " + response.statusText);
        }
        return response.text();
      })
      .then((text) => {
        console.log("Raw response text:", text);
        try {
          const data = JSON.parse(text);
          setLinks(data);
          setFilteredLinks(data); // Initialize filteredLinks with all links
        } catch (error) {
          console.error("JSON parsing error:", error);
          setError(new Error("Error parsing JSON"));
        }
      })
      .catch((error) => {
        console.error("Fetch error:", error);
        setError(error);
      });
  }, []);

  useEffect(() => {
    // Filter links based on the search term
    const lowercasedSearchTerm = searchTerm.toLowerCase();
    const filtered = links.filter((link) =>
      link.company.toLowerCase().includes(lowercasedSearchTerm)
    );
    setFilteredLinks(filtered);
  }, [searchTerm, links]);

  const formatDate = (dateString) => {
    if (!dateString) {
      return "-";
    }

    const date = new Date(dateString);

    if (isNaN(date.getTime())) {
      return "-";
    }

    const options = { day: "2-digit", month: "2-digit", year: "numeric" };
    return date.toLocaleDateString("en-GB", options);
  };

  const handleIconClick = () => {
    setShowPopup(!showPopup);
  };

  const handleCheckboxChange = (id) => {
    const updatedSelectedLinks = new Set(selectedLinks);
    if (updatedSelectedLinks.has(id)) {
      updatedSelectedLinks.delete(id);
    } else {
      updatedSelectedLinks.add(id);
    }
    setSelectedLinks(updatedSelectedLinks);
    setShowDeleteIcon(updatedSelectedLinks.size > 0);
  };

  const handleDeleteClick = () => {
    setShowConfirmationPopup(true); // Show confirmation popup
  };

  const handleConfirmDelete = () => {
    const selectedIds = Array.from(selectedLinks);

    if (selectedIds.length === 0) {
      alert("No links selected for deletion.");
      return;
    }

    fetch("http://localhost:8081/links/delete", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(selectedIds),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok " + response.statusText);
        }
        return response.text(); // Handle response as text
      })
      .then((text) => {
        console.log("Delete response text:", text);
        setLinks((prevLinks) =>
          prevLinks.filter((link) => !selectedIds.includes(link.id))
        );
        setFilteredLinks((prevFilteredLinks) =>
          prevFilteredLinks.filter((link) => !selectedIds.includes(link.id))
        );
        setSelectedLinks(new Set()); // Clear the selected links
        setShowDeleteIcon(false); // Hide the delete icon
        setShowConfirmationPopup(false); // Hide the confirmation popup
      })
      .catch((error) => {
        console.error("Delete request error:", error);
        alert("Failed to delete selected links.");
      });
  };

  const handleCancelDelete = () => {
    setShowConfirmationPopup(false); // Hide the confirmation popup
  };

  const toggleEditMode = () => {
    setIsEditMode(!isEditMode);
  };

  const handleLinkClick = (id) => {
    fetch(`http://localhost:8081/links/visitLink/${id}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok " + response.statusText);
        }
        return response.text(); // Handle response as text
      })
      .then((text) => {
        console.log("PATCH response text:", text);
        // Update the links state to reflect the updated last visit date
        setLinks((prevLinks) =>
          prevLinks.map((link) =>
            link.id === id
              ? { ...link, lastVisit: new Date().toISOString() }
              : link
          )
        );
        setFilteredLinks((prevFilteredLinks) =>
          prevFilteredLinks.map((link) =>
            link.id === id
              ? { ...link, lastVisit: new Date().toISOString() }
              : link
          )
        );
      })
      .catch((error) => {
        console.error("PATCH request error:", error);
        alert("Failed to update visit date.");
      });
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-content">
        <div className="heading-container">
          <div className="info-icon" onClick={handleIconClick}>
            +
          </div>
          <h2 className="dashboard-heading">Career Monitor</h2>
          {showDeleteIcon && (
            <div className="delete-icon" onClick={handleDeleteClick}>
              🗑️ {/* Trash icon */}
            </div>
          )}
        </div>
        {showPopup && <Popup onClose={() => setShowPopup(false)} />}
        {showConfirmationPopup && (
          <ConfirmationPopup
            onClose={handleCancelDelete}
            onConfirm={handleConfirmDelete}
          />
        )}
        <div className="dashboard-body">
          <div className="search-container">
            <input
              type="text"
              placeholder="Search by company name..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <span className="edit-icon" onClick={toggleEditMode}>
              ✎
            </span>{" "}
            {/* Pencil icon */}
          </div>
          {error ? (
            <p className="text-danger">Error: {error.message}</p>
          ) : (
            <div className="table-container">
              <table className="table table-striped">
                <thead>
                  <tr>
                    {isEditMode && <th scope="col">Select</th>}{" "}
                    {/* Conditionally render Select column */}
                    <th scope="col">Company</th>
                    <th scope="col">Last Visited</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredLinks.map((link) => (
                    <tr key={link.id}>
                      {isEditMode && (
                        <td>
                          <input
                            type="checkbox"
                            checked={selectedLinks.has(link.id)}
                            onChange={() => handleCheckboxChange(link.id)}
                          />
                        </td>
                      )}
                      <td>
                        <a
                          href={link.link}
                          target="_blank"
                          rel="noopener noreferrer"
                          onClick={(e) => {
                            e.preventDefault(); // Prevent the default link behavior
                            handleLinkClick(link.id); // Call the function to update visit date
                            window.open(link.link, "_blank"); // Open the link in a new tab
                          }}
                        >
                          {link.company}
                        </a>
                      </td>
                      <td>{formatDate(link.lastVisit)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default WelcomeDashboard;
