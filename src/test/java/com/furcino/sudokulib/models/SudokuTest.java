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
package com.furcino.sudokulib.models;

import org.apache.log4j.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test for sudoku object 
 * 
 * @author Martin Furek
 */
public class SudokuTest extends TestCase {
	
	/**
	 * Logger
	 */
	final static Logger logger = Logger.getLogger(SudokuTest.class);
	
	/**
	 * Test sudoku
	 */
	final static String SUDOKU = "802759106400300000700100009009508300000000000000000005205670801000000600000900003";
	
	/**
	 * Test invalid sudoku
	 */
	final static String INVALID_SUDOKU = "223456789456789123789123456214365897365897214897214365531642978642978531978531642";
	
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public SudokuTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SudokuTest.class);
	}
	
	/**
	 * Test string representation
	 */
	public void testStringRepresentation() {
		Sudoku sudoku = new Sudoku(SUDOKU);
		
		assertEquals("Sudoku creation is flawed", sudoku.getStringRepresentation(),SUDOKU);
	}
	
	/**
	 * Test input check
	 */
	public void testInputCheck() {
		boolean exception = false;

		try {
			Sudoku sudokuInvalid = new Sudoku(INVALID_SUDOKU);
			// we should not arrive here:
			sudokuInvalid.setCheckInputValidity(false);
			fail("Sudoku creation did not find inconsistency");
		} catch (IllegalArgumentException e) {
			exception = true;
		}
		
		assertTrue("Sudoku creation does not check for inconsistencies", exception);
	}
	
	/**
	 * Test user input 
	 */
	public void testUserInput() {
		Sudoku sudoku = new Sudoku(SUDOKU);
		sudoku.setCheckInputValidity(false);
		
		int row = 0;
		int col = 1;
		int value = 2;
		
		try {
			sudoku.setCellValue(row, col, value);
		} catch (IllegalArgumentException e) {
			fail("Wrong input should not throw exception.");
		}
		
		logger.info(sudoku);
		
		assertEquals("User should be able to set wrong values", value, sudoku.getCellValue(row, col));
		
		sudoku.setCellUserValueAsPossible(0, 7, 5);
		
		assertTrue("User possible value not set", sudoku.isCellUserValuePossible(0, 7, 5));
		
		sudoku.setCellUserValueAsImpossible(0, 7, 5);
		
		assertTrue("User impossible value not set", !sudoku.isCellUserValuePossible(0, 7, 5));
	}
	
	/**
	 * Tests resolving possible solutions after setting a value
	 */
	public void testSetValue() {
		Sudoku sudoku = new Sudoku();
		int row = 5;
		int col = 5;
		int value = 5;
		
		// set value
		sudoku.setCellValue(row, col, value);
		
		// print possible values for visual check
		logger.info(sudoku.printPossibilities(value));
		
		if (!sudoku.isCellValuePossible(row, col, value)) {
			fail("Failed when resolving cell [" + row + "," + col + "].");
		}
		
		for (int i = 0; i < Sudoku.N; i++) {
			if (sudoku.isCellValuePossible(row, col, i + 1)) {
				if ( (i+1) != value) {
					fail("Failed when resolving cell [" + row + "," + col + "].");
				}
			}
			if (sudoku.isCellValuePossible(row, i, value)) {
				if (i != col) {
					fail("Failed when resolving row [" + row + "," + i + "].");
				}
			}
			if (sudoku.isCellValuePossible(i, col, value)) {
				if (i != row) {
					fail("Failed when resolving col [" + i + "," + col + "].");
				}
			}
		}
		
		for (int i = row/3*3; i < row/3*3 + 3; i++) {
			for (int j = col/3*3; j < col/3*3 + 3; j++) {
				if (sudoku.isCellValuePossible(i, j, value)) {
					if (i != row || j != col) {
						fail("Failed when resolving grid [" + i + "," + j + "].");
					}
				}
			}
		}
	}

}
