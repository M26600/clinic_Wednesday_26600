
import React, { useState } from "react";
import LibraryButton from "./LibraryButton";
import axios from "axios";

function Office() {
  const [officeName, setOfficeName] = useState("");
  const [officeNmbr, setOfficeNmbr] = useState("");
  const [officeNmbrToDelete, setOfficeNmbrToDelete] = useState("");

  async function saveOffice() {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/offices",
        {
          buildingName: officeName,
          officeNumber: officeNmbr,
        }
      );

      if (response.status === 200 || response.status === 201) {
        alert("Office saved successfully");
      } else {
        alert("Office is not saved");
      }

    } catch (error) {
      console.log("ERROR:", error);
      console.log("RESPONSE:", error.response);
      alert("Office is not saved. Check the browser console.");
    }
  }

  async function deleteOffice() {
    const deleteOfficeNmbr = officeNmbrToDelete.trim();

    if (!deleteOfficeNmbr) {
      alert("Enter an office number");
      return;
    }

    const response = await axios.delete(
      `http://localhost:8080/api/offices/room/${deleteOfficeNmbr}`
    );

    if (response.status === 204) {
      alert("Office deleted successfully");
    } else {
      alert("Office is not deleted");
    }
  }

  return (
    <>
      <div>
        <h2>SAVE OFFICE</h2>
        <label>OFFICE NAME: </label>
        <input
          type="text"
          onChange={(e) => setOfficeName(e.target.value)}
        />
        <br />

        <label>OFFICE NUMBER: </label>
        <input
          type="text"
          onChange={(e) => setOfficeNmbr(e.target.value)}
        />

        <LibraryButton buttonAction={saveOffice} buttonLabel="SAVE" />
      </div>

      <div>
        <h2>DELETE OFFICE</h2>
        <input
          type="text"
          placeholder="Office number"
          onChange={(e) => setOfficeNmbrToDelete(e.target.value)}
        />

        <LibraryButton buttonAction={deleteOffice} buttonLabel="DELETE" />
      </div>
    </>
  );
}

export default Office;
