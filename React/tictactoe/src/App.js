import { useState } from 'react';

/**
 * Componente Square: Representa un botón dentro del tablero del juego.
 * @param {string} value - Valor del botón ('X', 'O' o null).
 * @param {function} onSquareClick - Función que se ejecuta al hacer clic en el botón.
 */
function Square({ value, onSquareClick }) { // Componente funcional de React para representar un botón
  return (
    <button className="square" onClick={onSquareClick}>
      {value}
    </button>
  );
}

/**
 * Componente Board: Representa el tablero del juego y gestiona la lógica de juego.
 * @param {boolean} xIsNext - Indica si el siguiente turno es de 'X'.
 * @param {Array} squares - Estado actual del tablero.
 * @param {function} onPlay - Función para actualizar el estado del juego.
 */
function Board({ xIsNext, squares, onPlay }) {
  /**
   * Maneja el clic en una celda del tablero.
   * @param {number} i - Índice de la celda en la que se hizo clic.
   */
  function handleClick(i) { // Manejador de eventos para el clic en una celda del tablero
    if (calculateWinner(squares) || squares[i]) {
      return;
    }
    const nextSquares = squares.slice();
    nextSquares[i] = xIsNext ? 'X' : 'O';
    onPlay(nextSquares);
  }

  const winner = calculateWinner(squares);
  let status = winner ? `Winner: ${winner}` : `Next player: ${xIsNext ? 'X' : 'O'}`;

  return ( // Fragmento de React para devolver varios elementos sin un contenedor adicional
    <>
      <div className="status">{status}</div> 
      <div className="board-row">
        <Square value={squares[0]} onSquareClick={() => handleClick(0)} />
        <Square value={squares[1]} onSquareClick={() => handleClick(1)} />
        <Square value={squares[2]} onSquareClick={() => handleClick(2)} />
      </div>
      <div className="board-row">
        <Square value={squares[3]} onSquareClick={() => handleClick(3)} />
        <Square value={squares[4]} onSquareClick={() => handleClick(4)} />
        <Square value={squares[5]} onSquareClick={() => handleClick(5)} />
      </div>
      <div className="board-row">
        <Square value={squares[6]} onSquareClick={() => handleClick(6)} />
        <Square value={squares[7]} onSquareClick={() => handleClick(7)} />
        <Square value={squares[8]} onSquareClick={() => handleClick(8)} />
      </div>
    </>
  );
}

/**
 * Componente Game: Gestiona el estado del historial de movimientos y el flujo general del juego.
 */
export default function Game() {
  const [history, setHistory] = useState([Array(9).fill(null)]);
  const [currentMove, setCurrentMove] = useState(0);
  const xIsNext = currentMove % 2 === 0;
  const currentSquares = history[currentMove];

  /**
   * Maneja la actualización del estado del juego con el siguiente movimiento.
   * @param {Array} nextSquares - Nuevo estado del tablero después del movimiento.
   */
  function handlePlay(nextSquares) {
    const nextHistory = [...history.slice(0, currentMove + 1), nextSquares];
    setHistory(nextHistory);
    setCurrentMove(nextHistory.length - 1);
  }

  /**
   * Permite retroceder a un movimiento anterior.
   * @param {number} nextMove - Índice del movimiento al que se quiere retroceder.
   */
  function jumpTo(nextMove) {
    setCurrentMove(nextMove);
  }

  const moves = history.map((squares, move) => {
    const description = move > 0 ? `Go to move #${move}` : 'Go to game start';
    return (
      <li key={move}>
        <button onClick={() => jumpTo(move)}>{description}</button>
      </li>
    );
  });

  return (
    <div className="game">
      <div className="game-board">
        <Board xIsNext={xIsNext} squares={currentSquares} onPlay={handlePlay} />
      </div>
      <div className="game-info">
        <ol>{moves}</ol>
      </div>
    </div>
  );
}

/**
 * Calcula si hay un ganador en el tablero.
 * @param {Array} squares - Estado actual del tablero.
 * @returns {string|null} - Retorna 'X' o 'O' si hay un ganador, de lo contrario, retorna null.
 */
function calculateWinner(squares) { 
  const lines = [
    [0, 1, 2],  
    [3, 4, 5], 
    [6, 7, 8],
    [0, 3, 6], 
    [1, 4, 7], 
    [2, 5, 8],
    [0, 4, 8], 
    [2, 4, 6]
  ];
  for (let [a, b, c] of lines) {
    if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
      return squares[a];
    }
  }
  return null;
}

