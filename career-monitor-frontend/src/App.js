import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Page from "./components/Page"; // Import the Page component



function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Page/>} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
