import React from "react";
import "./ConfirmationPopup.css"; // Add styling for the popup

function ConfirmationPopup({ onClose, onConfirm }) {
  return (
    <div className="confirmation-popup-overlay">
      <div className="confirmation-popup">
        <h3>Are you sure you want to delete?</h3>
        <div className="confirmation-buttons">
          <button className="btn btn-confirm" onClick={onConfirm}>
            Yes
          </button>
          <button className="btn btn-cancel" onClick={onClose}>
            No
          </button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmationPopup;
