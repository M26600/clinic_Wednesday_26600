
import React from "react";
import "./App.css";
import Office2 from "./components/Office2";
import Office from "./components/Office";

import LibraryLogin from "./components/LibraryLogin";
import LibraryMain from "./components/LibraryMain";
import { Routes, Route } from "react-router-dom";


function App() {
  return (
    <div>

      <Routes>
        <Route path = "/" element = {<LibraryLogin/>}/>
        <Route path="/login" element={<LibraryLogin />} />
        <Route path = "/office" element = {<Office2/>}/>
        <Route path = "/main" element = {<LibraryMain/>}/>
        <Route path = "/save-office" element={<Office/>}/>

        
      </Routes>
    </div>
   

  );
}

export default App;
