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

/**
 * Sudoku object to represent the puzzle.
 *
 * @author Martin Furek
 */
public class Sudoku {
	
	/** Logger. */
	final static Logger logger = Logger.getLogger(Sudoku.class);
	
	/** Id for sudoku. */
	private String id = "UNDEFINED";
	
	/** Number of numbers in a puzzle. */
	public static int N = 9;
	
	/** Number of grids in a gridRow. */
	public static int GRIDS = 3;

	/** Grid holding solution values  rows (0-8) columns (0-8) 0 - empty cell, else equals value. */
	protected int grid[][] = new int[N][N];

	/** Possible values for cell (row, column) where (row, column, value-1) == 1 means values is possible and (row, colun, value-1) == 0 means value is not possible. */
	protected int possibleValues[][][] = new int[N][N][N];

	/** Possible user specified values for cell (row, column) where (row, column, value-1) == 1 means values is possible and (row, colun, value-1) == 0 means value is not possible. */
	protected int possibleUserValues[][][] = new int[N][N][N];
	
	/** Solved values in grids (0-2, 0-2) where (gridRow, gridColumn, value-1) == 1 means value is solved and (gridRow, gridColumn, value-1) == 0 means value is not solved. */
	protected int solvedValuesInGrid[][][] = new int[GRIDS][GRIDS][N];
	
	/** Solved values in rows (0-8) where (row, value-1) == 1 means value is solved and (row, value-1) == 0 means value is not solved. */
	protected int solvedValuesInRows[][] = new int[N][N];
	
	/** Solved values in columns (0-8) where (column, value-1) == 1 means value is solved and (column, value-1) == 0 means value is not solved. */
	protected int solvedValuesInCols[][] = new int[N][N];
	
	/**
	 * Check validity of sudoku. Can be changed after initial creation
	 * for user input (wrong values);
	 */
	protected boolean checkInputValidity = true;

	/** Solution to sudoku. */
	protected Sudoku solution;
	
	/**
	 * Empty sudoku constructor.
	 */
	public Sudoku() {
		initPossibleValues();
	}
	
	/**
	 * Create sudoku from a string representation where the string
	 * represents sudoku rows (0-8) one after another from left to right.
	 *
	 * @param sudoku string representation of puzzle
	 */
	public Sudoku(String sudoku) {
		initPossibleValues();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				String input = sudoku.substring((i*N + j), (i*N + j) + 1);
				if (input.equals(".")) { 
					input = "0";
				}
				int value = Integer.parseInt(input);
				setCellValue(i, j, value);
			}
		}
	}
	
	/**
	 * Create sudoku from a string representation and set id .
	 *
	 * @param sudoku string representation of puzzle
	 * @param id identification of new sudoku
	 */
	public Sudoku(String sudoku, String id) {
		initPossibleValues();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				String input = sudoku.substring((i*N + j), (i*N + j) + 1);
				if (input.equals(".")) { 
					input = "0";
				}
				int value = Integer.parseInt(input);
				setCellValue(i, j, value);
			}
		}
		this.setId(id);
	}
	
	/**
	 * Create sudoku from another puzzle.
	 *
	 * @param sudoku puzzle to copy
	 */
	public Sudoku(Sudoku sudoku) {
		initPossibleValues();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				setCellValue(i, j, sudoku.grid[i][j]);
			}
		}
		this.setId(sudoku.getId());
	}
	
	/**
	 * Create sudoku from another puzzle and set id.
	 *
	 * @param sudoku puzzle to copy
	 * @param id identification of new sudoku
	 */
	public Sudoku(Sudoku sudoku, String id) {
		initPossibleValues();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				setCellValue(i, j, sudoku.grid[i][j]);
			}
		}
		this.setId(id);
	}

	/**
	 * Create sudoku from an array.
	 *
	 * @param input puzzle as array
	 */
	public Sudoku(int input[][]) {
		initPossibleValues();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				setCellValue(i, j, input[i][j]);
			}
		}
	}

	/**
	 * Create sudoku from an array and set id.
	 *
	 * @param input puzzle as array
	 * @param id identification of new sudoku
	 */
	public Sudoku(int input[][], String id) {
		initPossibleValues();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				setCellValue(i, j, input[i][j]);
			}
		}
		this.setId(id);
	}

	/**
	 * Set all values as possible (for empty sudokus).
	 */
	private void initPossibleValues() {
		// set all values to possible
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					this.possibleValues[i][j][k] = 1;
				}
			}
		}
	}
	
	/**
	 * Check input value .
	 *
	 * @param value input
	 */
	private void checkInputValue(int value) {
		if (value <= 0 || value > N) {
			throw new IllegalArgumentException("Incorrect value (" + value + ")");
		}
	}
	
	/**
	 * Check input value (can be zero).
	 *
	 * @param value input
	 */
	private void checkInputValueWithZero(int value) {
		if (value < 0 || value > N) {
			throw new IllegalArgumentException("Incorrect value (" + value + ")");
		}
	}
	
	/**
	 * Check input row.
	 *
	 * @param row input
	 */
	private void checkInputRow(int row) {
		if (row < 0 || row >= N) {
			throw new IllegalArgumentException("Incorrect row (" + row + ")");
		}
	}
	
	/**
	 * Check input column.
	 *
	 * @param col input
	 */
	private void checkInputCol(int col) {
		if (col < 0 || col >= N) {
			throw new IllegalArgumentException("Incorrect column (" + col + ")");
		}
	}
	
	/**
	 * Check input grid row.
	 *
	 * @param gridRow input
	 */
	private void checkInputGridRow(int gridRow) {
		if (gridRow < 0 || gridRow >= N/GRIDS) {
			throw new IllegalArgumentException("Incorrect row (" + gridRow + ")");
		}
	}
	
	/**
	 * Check input grid column.
	 *
	 * @param gridCol input
	 */
	private void checkInputGridCol(int gridCol) {
		if (gridCol < 0 || gridCol >= N/GRIDS) {
			throw new IllegalArgumentException("Incorrect column (" + gridCol + ")");
		}
	}

	/**
	 * Removes the possibility of a solution based on basic rules
	 * (if value is set, it can not be in the same row, column, or grid).
	 *
	 * @param row puzzle row (0-8)
	 * @param col puzzle column (0-8)
	 * @param value added solution (1-9)
	 */
	public void resolvePossibleValuesBasic(int row, int col, int value) {
		checkInputValue(value);
		checkInputRow(row);
		checkInputCol(col);
		
		// set value representation
		int k = value - 1;
		
		// resolve cell
		for (int i=1; i <= N; i++) {
			if (i != value) {
				this.possibleValues[row][col][i-1] = 0;
			}
		}
		
		// resolve row
		for (int i=0; i < N; i++) {
			if (i != col) {
				this.possibleValues[row][i][k] = 0;
			}
		}
		
		// resolve col
		for (int i=0; i < N; i++) {
			if (i != row) {
				this.possibleValues[i][col][k] = 0;
			}
		}
		
		// resolve grid
		for (int i = row/3*3; i < row/3*3+3; i++) {
			for (int j= col/3*3; j < col/3*3+3; j++) {
				if (i != row && j != col) {
					this.possibleValues[i][j][k] = 0;
				}
			}
		}
	}
	
	/**
	 * Returns value for a cell(row, col).
	 *
	 * @param row cell row
	 * @param col cell column
	 * @return value for coordinates
	 */
	public int getCellValue(int row, int col) {
		checkInputRow(row);
		checkInputCol(col);
		return this.grid[row][col];
	}
	
	/**
	 * Set value in a cell according to coordinates.
	 *
	 * @param row cell row
	 * @param col cell column
	 * @param value cell value
	 */
	public void setCellValue(int row, int col, int value) {
		checkInputValueWithZero(value);
		checkInputRow(row);
		checkInputCol(col);

		int oldValue = this.grid[row][col];
		
		if (!checkInputValidity) {
			this.grid[row][col] = value;
		} else {
			if (this.isCellValuePossible(row, col, value)) {
				this.grid[row][col] = value;
			} else {
				throw new IllegalArgumentException("Cell [" + row + "][" + col + "] can not have value (" + value + ")");
			}
		}

		if (value != 0) {
			resolvePossibleValuesBasic(row, col, value);
			setGridValueAsSolved(row/3, col/3, value);
			setRowValueAsSolved(row, value);
			setColValueAsSolved(col, value);
		} else if (oldValue != 0){
		    // TODO: there could be a way to resolve possibilities when removing a value
            // (now we create new sudokus when removing values)
        }
	}

	/**
	 * Get number of solved cells in a sudoku.
	 *
	 * @return number of solved cells
	 */
	public int getClueCount() {
		int count = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (this.grid[i][j] != 0)
					count++;;
			}
		}

		return count;
	}

	/**
	 * Checks if two puzzles are the same.
	 *
	 * @param sudoku puzzle to check
	 * @return true if puzzles are the same or false otherwise
	 */
	public boolean isSame(Sudoku sudoku) {
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				if (this.grid[row][col] != sudoku.grid[row][col])
					return false;
			}
		}

		return true;
	}
	
	/**
	 * Sets value as solved in a grid.
	 *
	 * @param gridRow row coordinate of grid
	 * @param gridCol column coordinate of grid
	 * @param value number to set as solved
	 */
	public void setGridValueAsSolved(int gridRow, int gridCol, int value) {
		checkInputValue(value);
		checkInputGridRow(gridRow);
		checkInputGridCol(gridCol);
		this.solvedValuesInGrid[gridRow][gridCol][value-1] = 1;
	}

	/**
	 * Sets value as solved in a row.
	 *
	 * @param row puzzle row
	 * @param value number to solve
	 */
	public void setRowValueAsSolved(int row, int value) {
		checkInputValue(value);
		checkInputRow(row);
		this.solvedValuesInRows[row][value-1] = 1;
	}

	/**
	 * Sets value as solved in a column.
	 *
	 * @param col puzzle column
	 * @param value number to solve
	 */
	public void setColValueAsSolved(int col, int value) {
		checkInputValue(value);
		checkInputCol(col);
		this.solvedValuesInCols[col][value-1] = 1;
	}
	
	/**
	 * Check if value is solved in a grid.
	 *
	 * @param gridRow row for grid
	 * @param gridCol column for grid
	 * @param value number to be checked
	 * @return true if it is solved or false if it is not
	 */
	public boolean isValueSolvedInGrid(int gridRow, int gridCol, int value) {
		checkInputValue(value);
		checkInputGridRow(gridRow);
		checkInputGridCol(gridCol);
		if (this.solvedValuesInGrid[gridRow][gridCol][value-1] == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check if value is solved in a row.
	 *
	 * @param row puzzle row
	 * @param value number to be checked
	 * @return true if it is solved or false if it is not
	 */
	public boolean isValueSolvedInRow(int row, int value) {
		checkInputValue(value);
		checkInputRow(row);
		if (this.solvedValuesInRows[row][value-1] == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check if value is solved in a column.
	 *
	 * @param col puzzle column
	 * @param value number to be checked
	 * @return true if it is solved or false if it is not
	 */
	public boolean isValueSolvedInCol(int col, int value) {
		checkInputValue(value);
		checkInputCol(col);
		if (this.solvedValuesInCols[col][value-1] == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check if value is one of possibilities for a cell.
	 *
	 * @param row cell row
	 * @param col cell column
	 * @param value number to check
	 * @return true if value is possible, false if it is not
	 */
	public boolean isCellValuePossible(int row, int col, int value) {
		checkInputValueWithZero(value);
		checkInputCol(col);
		checkInputRow(row);
		
		if (value == 0) {
			return true;
		}
		
		return this.possibleValues[row][col][value-1] == 1;
	}
	
	/**
	 * Set value as impossible in a cell.
	 *
	 * @param row cell row
	 * @param col cell column
	 * @param value number to set as not possible for cell
	 */
	public void setCellValueAsImpossible(int row, int col, int value) {
		checkInputValue(value);
		checkInputCol(col);
		checkInputRow(row);
		this.possibleValues[row][col][value-1] = 0;
	}
	
	/**
	 * Check if value is one of possibilities set by user for a cell.
	 *
	 * @param row cell row
	 * @param col cell column
	 * @param value number to check
	 * @return true if value is possible, false if it is not
	 */
	public boolean isCellUserValuePossible(int row, int col, int value) {
		checkInputValueWithZero(value);
		checkInputCol(col);
		checkInputRow(row);
		
		if (value == 0) {
			return true;
		}
		
		return this.possibleUserValues[row][col][value-1] == 1;
	}
	
	/**
	 * Set user defined value as possible in a cell.
	 *
	 * @param row cell row
	 * @param col cell column
	 * @param value number to set as not possible for cell
	 */
	public void setCellUserValueAsPossible(int row, int col, int value) {
		checkInputValue(value);
		checkInputCol(col);
		checkInputRow(row);
		this.possibleUserValues[row][col][value-1] = 1;
	}
	
	/**
	 * Set user defined value as impossible in a cell.
	 *
	 * @param row cell row
	 * @param col cell column
	 * @param value number to set as not possible for cell
	 */
	public void setCellUserValueAsImpossible(int row, int col, int value) {
		checkInputValue(value);
		checkInputCol(col);
		checkInputRow(row);
		this.possibleUserValues[row][col][value-1] = 0;
	}
	
	/**
	 * Check if sudoku is solved.
	 *
	 * @return true if solved, false if not
	 */
	public boolean isSolved() {
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				if (this.grid[row][col] == 0) {
				    return false;
                }
			}
		}
		return true;
	}

	/**
	 *  
	 * Get sudoku solution.
	 *
	 * @return solution
	 */
	public Sudoku getSolution() {
	    return solution;
	}

	/**
	 * Set sudoku solution.
	 *
	 * @param solution puzzle solution
	 */
	public void setSolution(Sudoku solution) {
		this.solution = solution;
	}
	
	/**
	 * Helper function for debugging, which prints possible places
	 * for a given value as 1 and impossible as 0.
	 *
	 * @param value number to check
	 * @return string representation containing possibilities for a value
	 */
	public String printPossibilities(int value) {
		checkInputValue(value);
		
		StringBuilder output = new StringBuilder();
		output.append("Possibilities for " + value + ": \n");
		for (int row = 0; row < N; row++) {
			if (row > 0 && row % 3 == 0) {
				output.append("-----------\n");
			}
			for (int col = 0; col < N; col++) {
				if (col > 0 && col % 3 == 0) {
					output.append("|");
				}
				output.append(this.possibleValues[row][col][value-1]);
			}
			output.append("\n");
		}
		return output.toString();
	}

	/**
	 * Get sudoku id.
	 *
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set sudoku id.
	 *
	 * @param id sudoku identification
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get check validity value.
	 *
	 * @return if validity is checked each setValue
	 */
	public boolean isCheckInputValidity() {
		return checkInputValidity;
	}
	
	/**
	 * Set check validity value .
	 *
	 * @param checkValidity determines if to check validity each set value
	 */
	public void setCheckInputValidity(boolean checkValidity) {
		this.checkInputValidity = checkValidity;
	}

	/**
	 * Get string representation of a sudoku, where rows from 0 to 8
	 * are printed left to right .
	 *
	 * @return string representation of puzzle
	 */
	public String getStringRepresentation() {
		StringBuilder output = new StringBuilder();
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				output.append(this.grid[row][col]);
			}
		}
		return output.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Sudoku: \n");
		for (int row = 0; row < N; row++) {
			if (row > 0 && row % 3 == 0) {
				output.append("-----------\n");
			}
			for (int col = 0; col < N; col++) {
				if (col > 0 && col % 3 == 0) {
					output.append("|");
				}
				output.append(this.grid[row][col]);
			}
			output.append("\n");
		}
		return output.toString();
	}
}
