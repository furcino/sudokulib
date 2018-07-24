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
package com.furcino.sudokulib.pdf;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.SudokuDifficulty;
import com.furcino.sudokulib.models.generators.SudokuFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PdfGeneratorTest extends TestCase {

	final static Logger logger = Logger.getLogger(PdfGeneratorTest.class);

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public PdfGeneratorTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PdfGeneratorTest.class);
	}

    public void testGeneratePdf() {
    	SudokuFactory generator = new SudokuFactory();
		List<Sudoku> sudokus = generator.generate(36, SudokuDifficulty.EASY);
		
		try {
			PdfGenerator.generateSudokuPdf("Furcino.com Sudokus", "easySudokus.pdf", sudokus);
		} catch (IOException e) {
			fail("Could not generate PDF");
		}
    }
}
