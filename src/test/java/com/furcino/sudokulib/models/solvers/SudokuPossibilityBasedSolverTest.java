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

import com.furcino.sudokulib.TestHelper;
import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SudokuPossibilityBasedSolverTest extends TestCase {
	
	final static Logger logger = Logger.getLogger(SudokuPossibilityBasedSolverTest.class);
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public SudokuPossibilityBasedSolverTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SudokuPossibilityBasedSolverTest.class);
	}
	
	public void testMaxSolutions() {
		// create empty sudoku
		Sudoku sudoku = new Sudoku();
		
		// solutions with value 1
		SudokuPossibilityBasedSolver solver1 = new SudokuPossibilityBasedSolver(sudoku);
		solver1.setMaxSolutions(1);
		
		try {
			int solutions1 = solver1.solve();
			assertEquals(1, solutions1);
			assertNotNull(solver1.getSolutions());
			assertEquals(1, solver1.getSolutions().size());
		} catch (TimeoutException e) {
			fail("Timeout fail.");
		}
		
		// solutions with value 2
		SudokuPossibilityBasedSolver solver2 = new SudokuPossibilityBasedSolver(sudoku);
		solver2.setMaxSolutions(2);
		
		try {
			int solutions2 = solver2.solve();
			assertEquals(2, solutions2);
			assertNotNull(solver2.getSolutions());
			assertEquals(2, solver2.getSolutions().size());
		} catch (TimeoutException e) {
			fail("Timeout fail.");
		}
		
		// solutions with value 3
		SudokuPossibilityBasedSolver solver3 = new SudokuPossibilityBasedSolver(sudoku);
		solver3.setMaxSolutions(3);
		
		try {
			int solutions3 = solver3.solve();
			assertEquals(3, solutions3);
			assertNotNull(solver3.getSolutions());
			assertEquals(3, solver3.getSolutions().size());
		} catch (TimeoutException e) {
			fail("Timeout fail.");
		}
	}
	
	public void testPossibilityBasedSolverEasyTestData() {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getEasySudokus()) {
                logger.info(sudoku.getId() + " " + sudoku.toString());
                if (sudoku.getSolution() == null) {
                    //fail("No solution for sudoku given.");
                }
                SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(sudoku);
                solver.setTimeLimit(50000);
                solver.setMaxSolutions(2);
                try {
                    int results = solver.solve();
                    if (results == 0) {
                        logger.info(solver.getSudoku().toString());

                        fail("Stopped without solving. Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else if (results > 1) {
                        fail("Multiple solutions");
                    } else {
                        //assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved " + results + " in " + solver.getDuration() + "[ms]: " + solver.getFirstSolution().toString());
                    }
                } catch (TimeoutException e) {

                    fail("Timeout Exception thrown");
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
		logger.info("Count: " + count + "; Average time: " + ((double) sum)/count + "[ms]");
	}
	
	public void testPossibilityBasedSolverNormalTestData() {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getNormalSudokus()) {
                logger.info(sudoku.getId() + " " + sudoku.toString());
                if (sudoku.getSolution() == null) {
                    //fail("No solution for sudoku given.");
                }
                SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(new Sudoku(sudoku));
                solver.setTimeLimit(50000);
                solver.setMaxSolutions(2);
                try {
                    int results = solver.solve();
                    if (results == 0) {
                        logger.info(solver.getSudoku().toString());

                        fail("Stopped without solving. Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else if (results > 1) {
                        Sudoku solution = solver.getFirstSolution();
                        for (int i = 1; i < solver.getSolutions().size(); i++) {
                            if (solution.isSame(solver.getSolutions().get(i))) {
                                fail("Duplicate solution " + sudoku.getId());
                            }
                        }
                        fail("Multiple solutions " + sudoku.getId() + " " + solver.getSolutions().size());
                    } else {
                        //assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved " + results + " in " + solver.getDuration() + "[ms]: " + solver.getFirstSolution().toString());
                    }
                } catch (TimeoutException e) {
                    fail("Timeout Exception thrown");
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
		logger.info("Count: " + count + "; Average time: " + ((double) sum)/count + "[ms]");
	}
	
	public void testPossibilityBasedSolverHardTestData() {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getHardSudokus()) {
                logger.info(sudoku.getId() + " " + sudoku.toString());
                if (sudoku.getSolution() == null) {
                    //fail("No solution for sudoku given.");
                }
                SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(sudoku);
                solver.setTimeLimit(50000);
                solver.setMaxSolutions(1);
                try {
                    int results = solver.solve();
                    if (results == 0) {
                        logger.info(solver.getSudoku().toString());

                        fail("Stopped without solving. Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else {
                        //assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved " + results + " in " + solver.getDuration() + "[ms]: " + solver.getFirstSolution().toString());
                    }
                } catch (TimeoutException e) {

                    fail("Timeout Exception thrown");
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
		logger.info("Count: " + count + "; Average time: " + ((double) sum)/count + "[ms]");
	}
}
