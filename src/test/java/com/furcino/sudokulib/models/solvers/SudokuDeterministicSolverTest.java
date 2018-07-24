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

public class SudokuDeterministicSolverTest extends TestCase {
	
	final static Logger logger = Logger.getLogger(SudokuDeterministicSolverTest.class);
	
	final static String SOLUTION_1 = "123456789456789123789123456214365897365897214897214365531642978642978531978531642";
	
	final static String SOLUTION_2 = "123456789456789123789123456214365897365897214897214365531642978648971532972538641";
	
	final static String SOLUTION_3 = "123456789456789123789123456214365897365897214897214365531642978672938541948571632";
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public SudokuDeterministicSolverTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SudokuDeterministicSolverTest.class);
	}
	
	public void testMaxSolutions() {
		// create empty sudoku
		Sudoku sudoku = new Sudoku();
		
		// solutions with value 1
		SudokuDeterministicSolver solver1 = new SudokuDeterministicSolver(sudoku);
		solver1.setMaxSolutions(1);
		
		try {
			int solutions1 = solver1.solve();
			assertEquals(1, solutions1);
			assertNotNull(solver1.getSolutions());
			assertEquals(1, solver1.getSolutions().size());
			assertEquals(solver1.getSolutions().get(0).getStringRepresentation(), SOLUTION_1);
		} catch (TimeoutException e) {
			fail("Timeout fail.");
		}
		
		// solutions with value 2
		SudokuDeterministicSolver solver2 = new SudokuDeterministicSolver(sudoku);
		solver2.setMaxSolutions(2);
		
		try {
			int solutions2 = solver2.solve();
			assertEquals(2, solutions2);
			assertNotNull(solver2.getSolutions());
			assertEquals(2, solver2.getSolutions().size());
			assertEquals(solver2.getSolutions().get(0).getStringRepresentation(), SOLUTION_1);
			assertEquals(solver2.getSolutions().get(1).getStringRepresentation(), SOLUTION_2);
		} catch (TimeoutException e) {
			fail("Timeout fail.");
		}
		
		// solutions with value 3
		SudokuDeterministicSolver solver3 = new SudokuDeterministicSolver(sudoku);
		solver3.setMaxSolutions(3);
		
		try {
			int solutions3 = solver3.solve();
			assertEquals(3, solutions3);
			assertNotNull(solver3.getSolutions());
			assertEquals(3, solver3.getSolutions().size());
			assertEquals(solver3.getSolutions().get(0).getStringRepresentation(), SOLUTION_1);
			assertEquals(solver3.getSolutions().get(1).getStringRepresentation(), SOLUTION_2);
			assertEquals(solver3.getSolutions().get(2).getStringRepresentation(), SOLUTION_3);
		} catch (TimeoutException e) {
			fail("Timeout fail.");
		}
	}
	
	public void testDeterministicSolver() {
    	long count = 0;
    	long sum = 0;
    	try {
            for (Sudoku sudoku : TestHelper.getSmallTestData()) {
                logger.info(sudoku.getId() + " " + sudoku.toString());
                if (sudoku.getSolution() == null) {
                    //fail("No solution for sudoku given.");
                }
                SudokuDeterministicSolver solver = new SudokuDeterministicSolver(sudoku);
                solver.setTimeLimit(50000);
                try {
                    if (solver.solve() == 0) {
                        fail("Stopped without solving. Id: " + sudoku.getId() + "\n" + sudoku.toString());
                    } else {
                        //assertTrue(solver.getSudoku().isSame(solver.getSudoku().getSolution()));
                        count++;
                        sum += solver.getDuration();
                        logger.info("Sudoku solved in " + solver.getDuration() + "[ms]: " + solver.getFirstSolution().toString());
                    }
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
