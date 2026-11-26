import React, { useState } from "react";
import LibraryButton from "./LibraryButton";
import LibraryHeader from "./LibraryHeader";


function LibraryMain() {
  const [count, setCount] = useState(0);

  function incrementNumber() {
    setCount(count + 1);
    console.log("This is the increment value" + count);
  }
  return (
    <div>
      <main>
        <h2>WELCOMING PAGE</h2>
        <LibraryHeader/>
        <LibraryButton buttonLabel="Increment" buttonAction={incrementNumber} />
        <div>Increment value is {count}</div>
      </main>
    </div>
  );
}

export default LibraryMain;
