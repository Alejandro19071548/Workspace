import React, { useState } from 'react';
import Button from './Button';
import './Calculator.css'; // Assuming you will create a CSS file for styling

function Calculator() {
  const [input, setInput] = useState('');
  const [result, setResult] = useState('');

  const handleButtonClick = (value) => {
    if (value === '=') {
      try {
        setResult(eval(input)); // Caution: eval can be dangerous if input is not controlled
      } catch (error) {
        setResult('Error');
      }
      setInput('');
    } else if (value === 'C') {
      setInput('');
      setResult('');
    } else {
      setInput(input + value);
    }
  };

  return (
    <div className="calculator">
      <div className="display">
        <div className="input">{input}</div>
        <div className="result">{result}</div>
      </div>
      <div className="buttons">
        {['1', '2', '3', '+', '4', '5', '6', '-', '7', '8', '9', '*', 'C', '0', '=', '/'].map((btn) => (
          <Button key={btn} text={btn} onClick={() => handleButtonClick(btn)} />
        ))}
      </div>
    </div>
  );
}

export default Calculator;