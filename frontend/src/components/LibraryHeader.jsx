
import React from "react";
import { Link } from "react-router-dom";

function LibraryHeader() {

  const libraryHeader = "LIBRARY MANAGEMENT SYSTEM";

  return (
    <div>
      <div>
        <h1>{libraryHeader}</h1>
      </div>

      <nav>
        <Link to="/main" style={{ paddingRight: "10px" }}>
          Main
        </Link>

        <Link to="/office" style={{ paddingRight: "10px" }}>
          Office
        </Link>

        <Link to="/login" style={{ paddingRight: "10px" }}>
          Login
        </Link>

        <Link to="/save-office" style={{ paddingRight: "10px" }}>
          Save Office
        </Link>
      </nav>
    </div>
  );
}

export default LibraryHeader;
