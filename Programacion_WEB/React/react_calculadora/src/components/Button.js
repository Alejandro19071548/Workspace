import React from 'react';

function Button({ text, onClick }) {
  return (
    <button className="calculator-button" onClick={onClick}>
      {text}
    </button>
  );
}

export default Button;