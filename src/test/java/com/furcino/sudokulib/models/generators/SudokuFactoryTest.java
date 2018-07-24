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

import java.util.List;

import org.apache.log4j.Logger;

import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.SudokuDifficulty;
import com.furcino.sudokulib.models.solvers.SudokuHumanSolver;
import com.furcino.sudokulib.models.solvers.SudokuPossibilityBasedSolver;
import com.furcino.sudokulib.models.solvers.exceptions.SolvingException;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SudokuFactoryTest extends TestCase {
	
	final static Logger logger = Logger.getLogger(SudokuFactoryTest.class);
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public SudokuFactoryTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SudokuFactoryTest.class);
	}
	
	public void testFactoryEasy() {
        logger.info("Generating...");
		SudokuFactory generator = new SudokuFactory();
		List<Sudoku> sudokus = generator.generate(1, SudokuDifficulty.EASY);
        logger.info("Generating Done");
		for (Sudoku sudoku : sudokus) {
			logger.info("Clue Count = " + sudoku.getClueCount());
			//logger.info(sudoku);
			SudokuHumanSolver solver = new SudokuHumanSolver(new Sudoku(sudoku), SudokuDifficulty.EASY);
			SudokuPossibilityBasedSolver solverP = new SudokuPossibilityBasedSolver(new Sudoku(sudoku));
			solverP.setMaxSolutions(2);
            solverP.setUsingHumanMethods(false);
			try {
				if(solver.solve() == 0) {
					fail("Can't solve sudoku (human solver)");
				} else {
					logger.info("Sudoku solved (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + ")");
					if (solver.getBasicLockedCandidatesSolves() > 0 || solver.getBasicSolves() > 5) {
					    fail("Generated Sudoku too difficult.");
                    }
				}
				if (solverP.solve() > 1) {
					fail("More than one solution probability.");
				}
			} catch (TimeoutException e) {
				fail("Can't solve sudoku (timeout)");
			} catch (SolvingException e) {
				fail("Can't solve sudoku (solving exception)");
			}
		}
	}

    public void testFactoryNormal() {
        logger.info("Generating...");
        SudokuFactory generator = new SudokuFactory();
        List<Sudoku> sudokus = generator.generate(1, SudokuDifficulty.NORMAL);
        logger.info("Generating Done");
        for (Sudoku sudoku : sudokus) {
            logger.info("Clue Count = " + sudoku.getClueCount());
            //logger.info(sudoku);
            SudokuHumanSolver solver = new SudokuHumanSolver(new Sudoku(sudoku), SudokuDifficulty.NORMAL);
            SudokuPossibilityBasedSolver solverP = new SudokuPossibilityBasedSolver(new Sudoku(sudoku));
            solverP.setMaxSolutions(2);
            solverP.setUsingHumanMethods(false);
            try {
                if(solver.solve() == 0) {
                    fail("Can't solve sudoku (human solver)");
                } else {
                    logger.info("Sudoku solved (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + ")");
                    if (solver.getBasicSolves() <= 5) {
                        fail("Generated Sudoku too easy.");
                    }
                    if (solver.getBasicLockedCandidatesSolves() > 0 || solver.getAdvancedSolves() > 0) {
                        fail("Generated Sudoku too difficult.");
                    }
                }
                if (solverP.solve() > 1) {
                    fail("More than one solution probability.");
                }
            } catch (TimeoutException e) {
                fail("Can't solve sudoku (timeout)");
            } catch (SolvingException e) {
                fail("Can't solve sudoku (solving exception)");
            }
        }
    }

    public void testFactoryHard() {
        logger.info("Generating...");
        SudokuFactory generator = new SudokuFactory();
        List<Sudoku> sudokus = generator.generate(1, SudokuDifficulty.HARD);
        logger.info("Generating Done");
        for (Sudoku sudoku : sudokus) {
            logger.info("Clue Count = " + sudoku.getClueCount());
            //logger.info(sudoku);
            SudokuHumanSolver solver = new SudokuHumanSolver(new Sudoku(sudoku), SudokuDifficulty.HARD);
            SudokuPossibilityBasedSolver solverP = new SudokuPossibilityBasedSolver(new Sudoku(sudoku));
            solverP.setMaxSolutions(2);
            solverP.setUsingHumanMethods(false);
            try {
                if(solver.solve() == 0) {
                    fail("Can't solve sudoku (human solver)");
                } else {
                    logger.info("Sudoku solved (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + ")");
                    if (solver.getBasicLockedCandidatesSolves() == 0) {
                        fail("Generated Sudoku too easy.");
                    }
                    if (solver.getAdvancedSolves() > 0) {
                        fail("Generated Sudoku too difficult.");
                    }
                }
                if (solverP.solve() > 1) {
                    fail("More than one solution probability.");
                }
            } catch (TimeoutException e) {
                fail("Can't solve sudoku (timeout)");
            } catch (SolvingException e) {
                fail("Can't solve sudoku (solving exception)");
            }
        }
    }

    public void testFactoryVeryHard() {
        logger.info("Generating...");
        SudokuFactory generator = new SudokuFactory();
        List<Sudoku> sudokus = generator.generate(1, SudokuDifficulty.VERY_HARD);
        logger.info("Generating Done");
        for (Sudoku sudoku : sudokus) {
            logger.info("Clue Count = " + sudoku.getClueCount());
            //logger.info(sudoku);
            SudokuHumanSolver solver = new SudokuHumanSolver(new Sudoku(sudoku), SudokuDifficulty.VERY_HARD);
            SudokuPossibilityBasedSolver solverP = new SudokuPossibilityBasedSolver(new Sudoku(sudoku));
            solverP.setMaxSolutions(2);
            solverP.setUsingHumanMethods(false);
            try {
                if(solver.solve() == 0) {
                    fail("Can't solve sudoku (human solver)");
                } else {
                    logger.info("Sudoku solved (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + ")");
                    if (solver.getAdvancedSolves() == 0) {
                        fail("Generated Sudoku too easy.");
                    }
                }
                if (solverP.solve() > 1) {
                    fail("More than one solution probability.");
                }
            } catch (TimeoutException e) {
                fail("Can't solve sudoku (timeout)");
            } catch (SolvingException e) {
                fail("Can't solve sudoku (solving exception)");
            }
        }
    }
}
