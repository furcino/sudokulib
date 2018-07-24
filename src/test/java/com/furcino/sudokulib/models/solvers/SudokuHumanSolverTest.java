package com.furcino.sudokulib.models.solvers;

import org.apache.log4j.Logger;

import com.furcino.sudokulib.TestHelper;
import com.furcino.sudokulib.models.SudokuDifficulty;
import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.exceptions.SolvingException;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SudokuHumanSolverTest extends TestCase {
	
	final static Logger logger = Logger.getLogger(SudokuHumanSolverTest.class);
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public SudokuHumanSolverTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SudokuHumanSolverTest.class);
	}
	
    public void testHumanSolverOnInput()
    {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getSmallTestData()) {
                if (sudoku.getSolution() == null) {
                    fail("No solution for sudoku given.");
                }
                SudokuHumanSolver solver = new SudokuHumanSolver(sudoku, SudokuDifficulty.VERY_HARD);
                try {
                    if (solver.solve() != 1) {
                        fail("Stopped without solving (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "). Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else {
                        assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved in " + solver.getDuration() + "[ms] (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "): " + solver.getSudoku().toString());
                    }
                } catch (SolvingException e) {
                    fail("Solving Exception thrown");
                } catch (TimeoutException e) {
                    fail("Timeout Exception thrown");
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
    	    fail("Got an exception");
        }
		logger.info("Average time: " + ((double) sum)/count + "[ms]");
    }
	
    public void testHumanSolverOnEasy()
    {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getEasySudokus()) {
                if (sudoku.getSolution() == null) {
                    fail("No solution for sudoku given.");
                }
                SudokuHumanSolver solver = new SudokuHumanSolver(sudoku, SudokuDifficulty.VERY_HARD);
                try {
                    if (solver.solve() != 1) {
                        fail("Stopped without solving (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "). Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else {
                        assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved in " + solver.getDuration() + "[ms] (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "): " + solver.getSudoku().toString());
                    }
                } catch (SolvingException e) {
                    fail("Solving Exception thrown");
                } catch (TimeoutException e) {
                    fail("Timeout Exception thrown");
                }
            }
        } catch (Exception e) {
    	    fail(e.getMessage());
        }
		logger.info("Average time: " + ((double) sum)/count + "[ms]");
    }
	
    public void testHumanSolverOnNormal()
    {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getNormalSudokus()) {
                if (sudoku.getSolution() == null) {
                    fail("No solution for sudoku given.");
                }
                SudokuHumanSolver solver = new SudokuHumanSolver(sudoku, SudokuDifficulty.VERY_HARD);
                try {
                    if (solver.solve() != 1) {
                        fail("Stopped without solving (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "). Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else {
                        assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved in " + solver.getDuration() + "[ms] (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "): " + solver.getSudoku().toString());
                    }
                } catch (SolvingException e) {
                    fail("Solving Exception thrown");
                } catch (TimeoutException e) {
                    fail("Timeout Exception thrown");
                }
            }
        } catch (Exception e) {
    	    fail(e.getMessage());
        }
		logger.info("Average time: " + ((double) sum)/count + "[ms]");
    }
	
    public void testHumanSolverOnHard()
    {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getHardSudokus()) {
                if (sudoku.getSolution() == null) {
                    fail("No solution for sudoku given.");
                }
                SudokuHumanSolver solver = new SudokuHumanSolver(sudoku, SudokuDifficulty.VERY_HARD);
                try {
                    if (solver.solve() != 1) {
                        fail("Stopped without solving (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "). Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else {
                        assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved in " + solver.getDuration() + "[ms] (" + solver.getBasicSolves() + ", " + solver.getBasicLockedCandidatesSolves() + ", " + solver.getAdvancedSolves() + "): " + solver.getSudoku().toString());
                    }
                } catch (SolvingException e) {
                    fail("Solving Exception thrown");
                } catch (TimeoutException e) {
                    fail("Timeout Exception thrown");
                }
            }
        } catch (Exception e) {
    	    fail(e.getMessage());
        }
		logger.info("Average time: " + ((double) sum)/count + "[ms]");
    }

}
