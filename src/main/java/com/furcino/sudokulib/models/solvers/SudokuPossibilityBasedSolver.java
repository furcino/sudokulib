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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.furcino.sudokulib.models.Cell;
import com.furcino.sudokulib.models.CellProposal;
import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.exceptions.SolvingException;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

/**
 * The Class SudokuPossibilityBasedSolver solves a sudoku prioritizing based on number of 
 * possibilities in a cell. The cell with fewest possibilities gets solved first.
 * 
 * @author Martin Furek
 */
public class SudokuPossibilityBasedSolver extends BaseSolver {

	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(SudokuPossibilityBasedSolver.class);

	/** UsingHumanMethods indicator */
	private boolean usingHumanMethods = true;
	
	/**
	 * Instantiates a new sudoku possibility based solver.
	 *
	 * @param sudoku the sudoku
	 */
	public SudokuPossibilityBasedSolver(Sudoku sudoku) {
		super(sudoku);
	}

	/**
	 * Solving method that parses randomly every solution starting with most
	 * likely options (proposals that have the least number of possible solutions).
	 *
	 * @param toSolve the to solve
	 * @param cellToSolve            current cell (holds independent sudoku, coordinates and value)
	 * @param maxSolutions            max solutions the solver searches for (good for validating
	 *            sudokus)
	 * @return the number of solutions
	 * @throws TimeoutException if solver takes too long
	 */
	private int solve(Sudoku toSolve, Cell cellToSolve, int maxSolutions) throws TimeoutException {

		// check for timeout
		if (System.currentTimeMillis() - getTimeStart() > getTimeLimit()) {
			throw new TimeoutException();
		}

		// stop if enough solutions exist
		if (getSolutions().size() >= maxSolutions) {
			return 0;
		}
		
		// prepare sudoku to not rewrite original
		Sudoku sudoku = new Sudoku(toSolve);

		// add value to sudoku
		if (cellToSolve.getValue() != 0) {
			if (sudoku.isCellValuePossible(cellToSolve.getRow(), cellToSolve.getCol(), cellToSolve.getValue())) {
				sudoku.setCellValue(cellToSolve.getRow(), cellToSolve.getCol(), cellToSolve.getValue());
			} else {
				return 0;
			}
		}

		// solve faster with human methods
		if (isUsingHumanMethods()) {
			try {
				boolean solving = true;
				while (solving) {
					solving = solveBasic(sudoku);
					if (!solving) {
						solving = solveGridLockedCandidates(sudoku);
					}
				}
			} catch (SolvingException e) {
			    // if there is a solving exception,
	            // the sudoku is invalid
				return 0;
			}
		}

		// check if solved
		if (sudoku.isSolved()) {
			for (Sudoku solution : getSolutions()) {
				if (solution.isSame(sudoku)) {
				    // if same solution exists stop
					return 0;
				}
			}
			this.solutions.add(new Sudoku(sudoku));
			return 1;
		}

		// create proposals sorted by number of possibilities
		List<CellProposal> proposals = new ArrayList<CellProposal>();
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < N; y++) {
				if (sudoku.getCellValue(x, y) == 0) {
					int possibilities = 0;
					int values[] = new int[N];
					for (int value = 1; value <= N; value++) {
						if (sudoku.isCellValuePossible(x, y, value)) {
							possibilities++;
							values[value - 1] = 1;
						}
					}

					if (possibilities == 0) {
						return 0;
					}

					CellProposal proposal = new CellProposal(x, y, values, possibilities);
					proposals.add(proposal);
				}
			}
		}

		// sort proposals
		Collections.sort(proposals);

		// solve with proposals
		if (proposals.size() > 0) {
			int result = 0;
			for (CellProposal proposal : proposals) {
				int counter = 0;
				for (int value = 1; value <= N; value++) {
					if (proposal.isPossible(value)) {
						// solve
						result += this.solve(sudoku, new Cell(proposal.getRow(), proposal.getCol(), value), maxSolutions);

						if (result > 0) {
							// success check if enough solutions
							if (result >= maxSolutions) {
								return result;
							}
						} else {
							// failure (if all possibilities vanish, this is not
							// the right route)
							counter++;
							if (proposal.getNumberOfPossibilities() - counter == 0) {
								return 0;
							}
						}
					}
				}
			}
			return result;
		}

		// tried everything
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.furcino.sudokulib.models.solvers.BaseSolver#solve()
	 */
	public int solve() throws TimeoutException {
		this.setTimeStart(System.currentTimeMillis());
		int result = this.solve(this.getSudoku(), new Cell(0, 0), getMaxSolutions());
		this.setTimeFinished(System.currentTimeMillis());
		return result;

	}

	public boolean isUsingHumanMethods() {
		return usingHumanMethods;
	}

	public void setUsingHumanMethods(boolean usingHumanMethods) {
		this.usingHumanMethods = usingHumanMethods;
	}
}