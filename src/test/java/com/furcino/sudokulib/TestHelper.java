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
package com.furcino.sudokulib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.SudokuDeterministicSolver;
import com.furcino.sudokulib.models.solvers.SudokuPossibilityBasedSolver;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

import junit.framework.TestCase;

public class TestHelper extends TestCase {
	
	static int easy[][] = { 
			{ 0, 5, 0, 0, 0, 0, 8, 1, 6 }, //
			{ 0, 0, 0, 8, 5, 7, 0, 9, 0 }, //
			{ 8, 9, 4, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 6, 4, 3 }, //
			{ 3, 0, 0, 1, 9, 4, 0, 5, 0 }, //
			{ 0, 8, 7, 0, 0, 0, 0, 2, 0 }, //
			{ 0, 0, 3, 2, 0, 0, 0, 0, 7 }, //
			{ 2, 6, 8, 9, 7, 1, 0, 0, 0 }, //
			{ 0, 7, 9, 0, 8, 3, 0, 6, 1 },};
	
	static int normal[][] = { 
			{ 4, 5, 1, 9, 0, 2, 0, 0, 0 }, //
			{ 0, 0, 0, 8, 0, 6, 0, 0, 5 }, //
			{ 0, 0, 0, 7, 0, 0, 1, 2, 0 }, //
			{ 0, 6, 2, 0, 0, 0, 3, 4, 7 }, //
			{ 0, 1, 4, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 2, 3, 0, 0, 6 }, //
			{ 5, 0, 6, 3, 4, 7, 0, 9, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 6, 7, 4 }, //
			{ 2, 0, 0, 0, 0, 9, 0, 0, 0 },};
	
	static int hard[][] = { 
			{ 0, 0, 0, 0, 0, 6, 0, 1, 0 }, //
			{ 0, 0, 0, 0, 0, 1, 0, 8, 0 }, //
			{ 2, 0, 4, 0, 0, 0, 0, 3, 0 }, //
			{ 4, 0, 0, 0, 3, 0, 5, 0, 0 }, //
			{ 0, 0, 0, 0, 4, 8, 2, 0, 0 }, //
			{ 5, 0, 6, 0, 0, 0, 0, 0, 0 }, //
			{ 7, 3, 0, 8, 0, 0, 0, 0, 4 }, //
			{ 0, 2, 0, 0, 0, 0, 0, 5, 8 }, //
			{ 0, 0, 1, 9, 0, 0, 0, 0, 0 },};
	
	static int veryHard[][] = { 
			{ 0, 0, 1, 2, 0, 7, 0, 0, 0 }, //
			{ 0, 6, 2, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 9, 4, 0 }, //
			{ 0, 0, 0, 9, 8, 0, 0, 0, 3 }, //
			{ 5, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 7, 0, 0, 0, 3, 0, 0, 2, 1 }, //
			{ 0, 0, 0, 1, 0, 2, 0, 0, 0 }, //
			{ 0, 7, 0, 8, 0, 0, 4, 1, 0 }, //
			{ 3, 0, 4, 0, 0, 0, 0, 8, 0 },};
    
	/**
	 * Test sudoku data
	 */
	public void testTestHelper() {
		try {
			assertEquals(4, getSmallTestData().size());
			assertEquals(50, getEasySudokus().size());
			assertEquals(50, getNormalSudokus().size());
			assertEquals(50, getHardSudokus().size());
		} catch (Exception e) {
			fail("Helper does not work");
		}
	}
	
	/**
	 * Returns sudokus with unique solutions
	 * 
	 * @return sudokus
	 */
	public static List<Sudoku> getSmallTestData() throws Exception {
		
		// easy
		Sudoku easySud = new Sudoku(easy);
		easySud.setId("easy");
		SudokuDeterministicSolver solverEasy = new SudokuDeterministicSolver(easySud);
		try {
			int solutions = solverEasy.solve();
			if (solutions == 1) {
				easySud.setSolution(solverEasy.getSolutions().get(0));
			} else {
                throw new Exception("No solution for easy sudoku.");
			}
		} catch (TimeoutException e) {
			throw new Exception("Fail getting test data easy.");
		}
		
		// normal
		Sudoku normalSud = new Sudoku(normal);
		normalSud.setId("normal");
		SudokuDeterministicSolver solverNormal = new SudokuDeterministicSolver(normalSud);
		try {
			int solutions = solverNormal.solve();
			if (solutions == 1) {
				normalSud.setSolution(solverNormal.getSolutions().get(0));
			} else {
				throw new Exception("No solution for normal sudoku.");
			}
		} catch (TimeoutException e) {
			throw new Exception("Fail getting test data normal.");
		}
		
		// hard
		Sudoku hardSud = new Sudoku(hard);
		hardSud.setId("hard");
		SudokuDeterministicSolver solverHard = new SudokuDeterministicSolver(hardSud);
		try {
			int solutions = solverHard.solve();
			if (solutions == 1) {
				hardSud.setSolution(solverHard.getSolutions().get(0));
			} else {
				throw new Exception("No solution for hard sudoku.");
			}
		} catch (TimeoutException e) {
			throw new Exception("Fail getting test data hard.");
		}
		
		// very hard
		Sudoku veryHardSud = new Sudoku(veryHard);
		veryHardSud.setId("veryHard");
		SudokuDeterministicSolver solverVeryHard = new SudokuDeterministicSolver(veryHardSud);
		try {
			int solutions = solverVeryHard.solve();
			if (solutions == 1) {
				veryHardSud.setSolution(solverVeryHard.getSolutions().get(0));
			} else {
				throw new Exception("No solution for very hard sudoku.");
			}
		} catch (TimeoutException e) {
			throw new Exception("Fail getting test data very hard.");
		}
		
		List<Sudoku> result = new ArrayList<Sudoku>();
		result.add(easySud);
		result.add(normalSud);
		result.add(hardSud);
		result.add(veryHardSud);
		
		return result;
	}
	
	/**
	 * Returns easy sudokus
	 * 
	 * @return sudokus
	 */
	public static List<Sudoku> getEasySudokus() throws Exception {
		InputStream in = TestHelper.class.getResourceAsStream("furcino_easy_sudokus");
		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		String line = null;

		List<Sudoku> inputSudokus = new ArrayList<Sudoku>();
		int counter = 0;
		try {
			while((line = buff.readLine()) != null) {
				counter++;
				Sudoku sudoku = new Sudoku(line, "easy_sudoku_" + counter);
				SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(sudoku);
				solver.setMaxSolutions(1);
				solver.solve();
				sudoku.setSolution(solver.getFirstSolution());
			    inputSudokus.add(sudoku);
			}
		} catch (IOException e) {
			throw new Exception("Can not read input data hard sudokus");
		}
		
		return inputSudokus;
	}
	
	/**
	 * Returns normal sudokus
	 * 
	 * @return sudokus
	 */
	public static List<Sudoku> getNormalSudokus() throws Exception {
		InputStream in = TestHelper.class.getResourceAsStream("furcino_normal_sudokus");
		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		String line = null;

		List<Sudoku> inputSudokus = new ArrayList<Sudoku>();
		int counter = 0;
		try {
			while((line = buff.readLine()) != null) {
				counter++;
				Sudoku sudoku = new Sudoku(line, "normal_sudoku_" + counter);
				SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(sudoku);
				solver.setMaxSolutions(1);
				solver.solve();
				sudoku.setSolution(solver.getFirstSolution());
			    inputSudokus.add(sudoku);
			}
		} catch (IOException e) {
			throw new Exception("Can not read input data hard sudokus");
		}
		
		return inputSudokus;
	}
	
	/**
	 * Returns hard sudokus (can have multiple solutions)
	 * 
	 * @return sudokus
	 */
	public static List<Sudoku> getHardSudokus() throws Exception {
		InputStream in = TestHelper.class.getResourceAsStream("furcino_hard_sudokus");
		BufferedReader buff = new BufferedReader(new InputStreamReader(in));
		String line = null;

		List<Sudoku> inputSudokus = new ArrayList<Sudoku>();
		int counter = 0;
		try {
			while((line = buff.readLine()) != null) {
				counter++;
				Sudoku sudoku = new Sudoku(line, "hard_sudoku_" + counter);
				SudokuPossibilityBasedSolver solver = new SudokuPossibilityBasedSolver(sudoku);
				solver.setMaxSolutions(1);
				solver.solve();
				sudoku.setSolution(solver.getFirstSolution());
			    inputSudokus.add(sudoku);
			}
		} catch (IOException e) {
			throw new Exception("Can not read input data hard sudokus");
		}
		
		return inputSudokus;
	}
}
