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

import com.furcino.sudokulib.models.SudokuDifficulty;
import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.exceptions.SolvingException;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

/**
 * Solver that simulates a human solving the puzzle.
 * 
 * @author Martin Furek
 */
public class SudokuHumanSolver extends BaseSolver {
	
	/** Logger. */
	final static Logger logger = Logger.getLogger(SudokuHumanSolver.class);
	
	/** Difficulty. */
    private SudokuDifficulty difficulty = SudokuDifficulty.NORMAL;

    /** Number of basic solves. */
    protected int basicSolves = 0;

    /** Number of basic locked candidates. */
    protected int basicLockedCandidates = 0;
    
    /** Number of advanced techniques used. */
    protected int advancedSolves = 0;
	
    /**
     * Constructor with default difficulty set to normal.
     *
     * @param sudoku puzzle to solve
     */
	public SudokuHumanSolver(Sudoku sudoku) {
		super(sudoku);
	}
	
	/**
	 * Instantiates a new sudoku human solver.
	 *
	 * @param sudoku the sudoku
	 * @param difficulty the difficulty
	 */
    public SudokuHumanSolver(Sudoku sudoku, SudokuDifficulty difficulty) {
        super(sudoku);
        this.difficulty = difficulty;
    }
    
    /**
     * Solve function.
     *
     * @return the int
     * @throws TimeoutException the timeout exception
     * @throws SolvingException the solving exception
     */
    @Override
	public int solve() throws TimeoutException, SolvingException {
		logger.debug("Human solver started");
		this.setTimeStart(System.currentTimeMillis());
		boolean solving = true;
		int loops = 0;
		while (solving) {
			if (System.currentTimeMillis() - getTimeStart() > getTimeLimit()) {
				throw new TimeoutException();
			}
			loops++;
			logger.debug("In loop: " + loops);
			if(solveBasic(this.sudoku)) {
			    solving = true;
                basicSolves++;
            } else {
			    solving = false;
            }
			if (!solving && this.difficulty.getNumVal() >= SudokuDifficulty.HARD.getNumVal()) {
                logger.debug("Using Locked Candidates Grid");
                if (solveGridLockedCandidates(this.sudoku)) {
                    solving = true;
                    basicLockedCandidates++;
                } else {
                    solving = false;
                }
            }
			if (!solving && this.difficulty.getNumVal() >= SudokuDifficulty.HARD.getNumVal()) {
                logger.debug("Using Locked Candidates Rows and Cols");
                if (solveRowAndColLockedCandidates(this.sudoku)) {
                    solving = true;
                    basicLockedCandidates++;
                } else {
                    solving = false;
                }
            }
            if (!solving && this.difficulty.getNumVal() >= SudokuDifficulty.VERY_HARD.getNumVal()) {
                logger.debug("Using X-Wing and Skyscraper");
                if (solveAdvancedXWingAndSkyscraper(this.sudoku)) {
                    solving = true;
                    advancedSolves++;
                } else {
                    solving = false;
                }
            }
			if (this.sudoku.isSolved()) {
				this.solutions.add(this.sudoku);
				logger.debug("Setting Sudoku as Solved. Time[ms]: " + (System.currentTimeMillis() - getTimeStart()));
				this.setTimeFinished(System.currentTimeMillis());
				return 1;
			}
		}
		this.setTimeFinished(System.currentTimeMillis());
		return 0;
	}

    /**
     * Gets the difficulty.
     *
     * @return the difficulty
     */
    public SudokuDifficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty.
     *
     * @param difficulty the new difficulty
     */
    public void setDifficulty(SudokuDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Gets the basic solves.
     *
     * @return the basic solves
     */
    public int getBasicSolves() {
        return basicSolves;
    }

    /**
     * Gets the basic locked candidates solves.
     *
     * @return the basic locked candidatess solves
     */
    public int getBasicLockedCandidatesSolves() {
        return basicLockedCandidates;
    }

    /**
     * Gets the advanced solves.
     *
     * @return the advanced solves
     */
    public int getAdvancedSolves() {
        return advancedSolves;
    }

}
