/*
 * Copyright 2018 Martin Furek
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.furcino.sudokulib.models.solvers;

import org.apache.log4j.Logger;

import com.furcino.sudokulib.models.Cell;
import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

/**
 * SudokuDeterministicSolver solves sudokus by assigning all possible values
 * to empty cells starting with (0,0) and incrementing columns fist and then 
 * rows and adding values starting from 1 up to 9. 
 * 
 * @author Martin Furek
 */
public class SudokuDeterministicSolver extends BaseSolver {
	
	/** Logger. */
	final static Logger logger = Logger.getLogger(SudokuDeterministicSolver.class);
	
	/**
	 * Constructor for random solver.
	 *
	 * @param sudoku puzzle to solve
	 */
	public SudokuDeterministicSolver(Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * Get next cell to solve.
	 *
	 * @param currentCell the cell that we are at now
	 * @return next cell to solve
	 */
	protected Cell getNextCell(Cell currentCell) {

		int row = currentCell.getRow();
		int col = currentCell.getCol();

		// next column
		col++;

		// reached end of row
		if (col >= N) {
			// next row
			col = 0;
			row++;
			
			// reached end of sudoku
			if (row >= N) {
				return null;
			}
		}

		return new Cell(row, col);
	}

	/**
	 * Solve function that solves a cell in a sudoku by deterministically 
	 * trying all possibilities .
	 *
	 * @param sudokuToSolve puzzle to solve
	 * @param cellToSolve cell to solve next
	 * @param maxSolutions maximum number of solutions to look for
	 * @return number of solutions
	 * @throws TimeoutException thrown if solving duration exceeds limit
	 */
	protected int solve(Sudoku sudokuToSolve, Cell cellToSolve, int maxSolutions) throws TimeoutException {

		// check timeout
		if (System.currentTimeMillis() - getTimeStart() > getTimeLimit()) {
			this.setTimeFinished(System.currentTimeMillis());
			throw new TimeoutException();
		}
		
		// check number of solutions
		if (this.getSolutions().size() >= getMaxSolutions()) {
			return this.getSolutions().size();
		}
		
		// if end reached, return number of solutions
		if (cellToSolve == null) {
			return addSolution(sudokuToSolve);
		}

		// check if value already assigned, if yes, continue with next cell
		if (sudokuToSolve.getCellValue(cellToSolve.getRow(), cellToSolve.getCol()) != 0) {
			// it is ok to return the next solve value directly
			Sudoku nextSolve = new Sudoku(sudokuToSolve);
			return solve(nextSolve, getNextCell(cellToSolve), maxSolutions);
		}

		// try each value 
		for (int value = 1; value <= N; value++) {
			// check if value is one of possible values
			if (!sudokuToSolve.isCellValuePossible(cellToSolve.getRow(), cellToSolve.getCol(), value)) {
				// continue with next value if value not possible
				continue;
			}

			// create new sudoku with new value assigned
			Sudoku nextSolve = new Sudoku(sudokuToSolve);
			nextSolve.setCellValue(cellToSolve.getRow(), cellToSolve.getCol(), value);

			// continue with next cell
			if (solve(nextSolve, getNextCell(cellToSolve), maxSolutions) >= getMaxSolutions()) {
				// if number of solutions reached stop
				// solving and return number of solutions
				return this.getSolutions().size();
			}
		}

		// tried every possible value
		return this.getSolutions().size();
	}
	
	/**
	 * Add solution.
	 *
	 * @param solution the proposed solution to the puzzle
	 * @return number of current solutions
	 */
	private int addSolution(Sudoku solution) {
		solutions.add(solution);
		return this.getSolutions().size();
	}
	
	/**
	 * Solving function.
	 *
	 * @return number of solutions
	 * @throws TimeoutException the timeout exception
	 */
	@Override
	public int solve() throws TimeoutException {
		this.setTimeStart(System.currentTimeMillis());
		int result = solve(this.getSudoku(), new Cell(0, 0), getMaxSolutions());
		this.setTimeFinished(System.currentTimeMillis());
		return result;
	}
}