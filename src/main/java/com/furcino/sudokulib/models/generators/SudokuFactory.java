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
package com.furcino.sudokulib.models.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.furcino.sudokulib.models.SudokuDifficulty;
import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.SudokuHumanSolver;
import com.furcino.sudokulib.models.solvers.SudokuPossibilityBasedSolver;
import com.furcino.sudokulib.models.solvers.exceptions.SolvingException;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;
import com.furcino.sudokulib.util.CommonUtil;

/**
 * A factory for creating Sudoku objects.
 * 
 * @author Martin Furek
 */
public class SudokuFactory {
	
	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(SudokuFactory.class);
	
	/** The random. */
	private Random random = new Random();

    /**
     * Generate.
     *
     * @param numberOfPuzzles the number of puzzles
     * @param difficulty the difficulty
     * @return the list of sudokus
     */
    public List<Sudoku> generate(int numberOfPuzzles, SudokuDifficulty difficulty) {
    	List<Sudoku> sudokus = new ArrayList<Sudoku>();
		for (int n=0; n < numberOfPuzzles; n++) {
			sudokus.add(this.generateSmallestSolvableSudoku(difficulty));
		}
		return sudokus;
    }
	
	/**
	 * Generate smallest solvable sudoku.
	 *
	 * @param difficulty the difficulty
	 * @return the sudoku
	 */
	public Sudoku generateSmallestSolvableSudoku(SudokuDifficulty difficulty) {
		Sudoku sudoku = null;
		Sudoku solution = null;
		int rows[] = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        int cols[] = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        CommonUtil.shuffleArray(rows);
        CommonUtil.shuffleArray(cols);

		while (true) {
            sudoku = getRandomInitialSudoku();
            solution = new Sudoku(sudoku);
            CommonUtil.shuffleArray(rows);
            CommonUtil.shuffleArray(cols);

			for (int i = 0; i < 9; i++){
				for (int j = 0; j < 9; j++){
					Sudoku lastSudoku = new Sudoku(sudoku);
					
					sudoku.setCellValue(rows[i], cols[j], 0);
					sudoku = new Sudoku(sudoku);
					
					SudokuHumanSolver humanSolver = new SudokuHumanSolver(new Sudoku(sudoku), difficulty);
					
					try {
						int solutions = humanSolver.solve();
						
						if (solutions != 1) {
							sudoku = lastSudoku;
						}
					} catch (TimeoutException e) {
						sudoku = lastSudoku;
					} catch (SolvingException e) {
						sudoku = lastSudoku;
					}
				}
			}

            SudokuHumanSolver humanSolver = new SudokuHumanSolver(new Sudoku(sudoku), difficulty);
            SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(new Sudoku(sudoku));
			solver.setMaxSolutions(2);
			
            try {
                humanSolver.solve();
                if (solver.solve() > 1) {
                	continue;
                }
            } catch (Exception e) {
			    // do nothing
            }

            switch (humanSolver.getDifficulty()) {
                case EASY:
                    if (humanSolver.getBasicSolves() <= 5) {
                        sudoku.setSolution(solution);
                        return sudoku;
                    }
                    break;
                case NORMAL:
                    if (humanSolver.getBasicSolves() > 5 && humanSolver.getBasicLockedCandidatesSolves() == 0 && humanSolver.getAdvancedSolves() == 0) {
                        sudoku.setSolution(solution);
                        return sudoku;
                    }
                    break;
                case HARD:
                    if (humanSolver.getBasicLockedCandidatesSolves() > 0 && humanSolver.getAdvancedSolves() == 0) {
                        sudoku.setSolution(solution);
                        return sudoku;
                    }
                    break;
                case VERY_HARD:
                    if (humanSolver.getAdvancedSolves() > 0) {
                        sudoku.setSolution(solution);
                        return sudoku;
                    }
                    break;
                default:
                    break;
            }
		}
	}
	
	/**
	 * Gets the random initial sudoku.
	 *
	 * @return the random initial sudoku
	 */
	public Sudoku getRandomInitialSudoku() {
		Sudoku sudoku = getUncheckedInitialSudoku();
		
		while (true) {			
			SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(new Sudoku(sudoku));
			solver.setMaxSolutions(1);
			int solutions;
			try {
				solutions = solver.solve();
			} catch (TimeoutException e) {
				solutions = 0;
			}

			if (solutions >= 1) {;
				return solver.getFirstSolution();
			}
			
			if (solutions == 0) {
				sudoku = getUncheckedInitialSudoku();
			}
		}
	}
	
	/**
	 * Gets the unchecked initial sudoku.
	 *
	 * @return the unchecked initial sudoku
	 */
	private Sudoku getUncheckedInitialSudoku() {
		Sudoku sudoku = new Sudoku();
		int clues = 0;
		int value = 1;
		
		while (clues < 17) {
			int row = random.nextInt(9);
			int col = random.nextInt(9);
			if (value > 9) {
				value = 1;
			}
			
			if (sudoku.isCellValuePossible(row, col, value)) {
				sudoku.setCellValue(row, col, value);
				clues++;
				value++;
			} else {
				continue;
			}
		}
		
		return sudoku;
	}
} 
