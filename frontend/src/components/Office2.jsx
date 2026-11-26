
import { useState, useEffect } from "react";
import axios from "axios";

function Office2() {
    const [offices, setOffices] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [editingOfficeId, setEditingOfficeId] = useState(null);
    const [editedOfficeName, setEditedOfficeName] = useState("");
    const [editedOfficeNumber, setEditedOfficeNumber] = useState("");

    // Fetch all offices
    async function fetchOffices() {
        try {
            setLoading(true);
            setError("");

            const response = await axios.get(
                "http://localhost:8080/api/offices"
            );

            setOffices(response.data);

        } catch (error) {
            console.error("Error fetching offices:", error);
            setError("Failed to load offices. Please try again.");

        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        fetchOffices();
    }, []);

    // Search offices
    const filteredOffices = offices.filter((office) => {
        const name = office.buildingName?.toLowerCase() || "";
        const number = office.officeNumber?.toString() || "";
        const search = searchTerm.toLowerCase().trim();

        return (
            name.includes(search) ||
            number.includes(search)
        );
    });

    // Delete an office
    async function deleteOffice(id) {
        const confirmed = window.confirm(
            "Are you sure you want to delete this office?"
        );

        if (!confirmed) {
            return;
        }

        try {
            await axios.delete(
                `http://localhost:8080/api/offices/${id}`
            );

            alert("Office deleted successfully");

            setOffices((previousOffices) =>
                previousOffices.filter((office) => office.id !== id)
            );

        } catch (error) {
            console.error("Error deleting office:", error);
            alert("Failed to delete office. Please try again.");
        }
    }

    // Select an office to update
    function editOffice(office) {
        setEditingOfficeId(office.id);
        setEditedOfficeName(office.buildingName);
        setEditedOfficeNumber(office.officeNumber);
    }

    // Update an office
    async function updateOffice(id) {
        try {
            const response = await axios.put(
                `http://localhost:8080/api/offices/${id}`,
                {
                    buildingName: editedOfficeName,
                    officeNumber: editedOfficeNumber
                }
            );

            setOffices((previousOffices) =>
                previousOffices.map((office) =>
                    office.id === id
                        ? {
                            ...office,
                            buildingName: editedOfficeName,
                            officeNumber: editedOfficeNumber
                        }
                        : office
                )
            );

            setEditingOfficeId(null);

            alert("Office updated successfully");

        } catch (error) {
            console.error("Error updating office:", error);
            console.log("Backend response:", error.response);
            alert("Failed to update office. Please try again.");
        }
    }

    return (
        <div className="office-container">

            <h1>OFFICE MANAGEMENT</h1>

            <div className="search-container">
                <label htmlFor="searchOffice">
                    Search Office:
                </label>

                <input
                    id="searchOffice"
                    type="text"
                    placeholder="Search by office name or number..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <h2>Available Offices</h2>

            {loading ? (

                <p>Loading offices...</p>

            ) : error ? (

                <p>{error}</p>

            ) : (

                <table border="1" cellPadding="10">

                    <thead>
                        <tr>
                            <th>Office Name</th>
                            <th>Office Number</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>

                        {filteredOffices.length > 0 ? (

                            filteredOffices.map((office) => (

                                <tr key={office.id}>

                                    <td>
                                        {editingOfficeId === office.id ? (
                                            <input
                                                type="text"
                                                value={editedOfficeName}
                                                onChange={(e) =>
                                                    setEditedOfficeName(e.target.value)
                                                }
                                            />
                                        ) : (
                                            office.buildingName
                                        )}
                                    </td>

                                    <td>
                                        {editingOfficeId === office.id ? (
                                            <input
                                                type="text"
                                                value={editedOfficeNumber}
                                                onChange={(e) =>
                                                    setEditedOfficeNumber(e.target.value)
                                                }
                                            />
                                        ) : (
                                            office.officeNumber
                                        )}
                                    </td>

                                    <td>
                                        <button
                                            type="button"
                                            onClick={() => deleteOffice(office.id)}
                                        >
                                            Delete
                                        </button>

                                        {editingOfficeId === office.id ? (

                                            <>
                                                <button
                                                    type="button"
                                                    onClick={() => updateOffice(office.id)}
                                                >
                                                    Save
                                                </button>

                                                <button
                                                    type="button"
                                                    onClick={() => setEditingOfficeId(null)}
                                                >
                                                    Cancel
                                                </button>
                                            </>

                                        ) : (

                                            <button
                                                type="button"
                                                onClick={() => editOffice(office)}
                                            >
                                                Update
                                            </button>

                                        )}

                                    </td>

                                </tr>

                            ))

                        ) : (

                            <tr>
                                <td colSpan="3">
                                    No offices found.
                                </td>
                            </tr>

                        )}

                    </tbody>

                </table>

            )}

        </div>
    );
}

export default Office2;
