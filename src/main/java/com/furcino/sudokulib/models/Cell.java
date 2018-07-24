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

/**
 * Cell holds information about a cell.
 *
 * @author Martin Furek
 */
public class Cell {
	
	/** Cell information (row, column, value). */
	private int row, col, value;

	/**
	 * Constructor .
	 *
	 * @param row cell row
	 * @param col cell column
	 */
	public Cell(int row, int col) {
		super();
		this.row = row;
		this.col = col;
	}

	/**
	 * Constructor.
	 *
	 * @param row cell row
	 * @param col cell column
	 * @param value cell number
	 */
	public Cell(int row, int col, int value) {
		super();
		this.row = row;
		this.col = col;
        this.value = value;
	}

	/**
	 * Get cell row.
	 *
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Set cell row.
	 *
	 * @param row cell row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Get cell column.
	 *
	 * @return column
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Set cell column.
	 *
	 * @param col cell column
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * Get cell value.
	 *
	 * @return cell value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Set cell value.
	 *
	 * @param value cell value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + ", value = " + value + "]";
	}
}
