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
 * CellProposal holds information about and cell and the possible solutions 
 * for the cell.
 * 
 * @author Martin Furek
 */
public class CellProposal implements Comparable<Object> {
	
	/** Numbers of puzzle. */
	public static int N = 9;

	/** Coordinates for cell. */
	private int row, col;

	/** Values that are possible for a cell where (value - 1) == 1 means value is possible and (value - 1) == 0 means value is impossible. */
	private int values[] = new int[N];

	/** Number of possible solutions for this cell. */
	private int numberOfPossibilities;
	
	/**
	 * Constructor .
	 *
	 * @param row cell row (0-8)
	 * @param col cell column (0-8)
	 * @param values numbers that are possible
	 * @param numberOfPossibilities number of possible values
	 */
	public CellProposal(int row, int col, int values[], int numberOfPossibilities) {
        super();
		this.row = row;
		this.col = col;
		this.values = values;
		this.numberOfPossibilities = numberOfPossibilities;
	}
	
	/**
	 * Get number of possible values .
	 *
	 * @return number of possible values
	 */
	public int getNumberOfPossibilities() {
		return this.numberOfPossibilities;
	}
	
	/**
	 * Check if value is possible.
	 *
	 * @param value number to check
	 * @return true if possible and false otherwise
	 */
	public boolean isPossible(int value) {
		return this.values[value-1] == 1;
	}
	
	/**
	 * Get cell row .
	 *
	 * @return cell row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Get cell column.
	 *
	 * @return cell column
	 */
	public int getCol() {
		return col;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object obj) {
		if (obj instanceof CellProposal) {
			CellProposal o1 = this;
			CellProposal o2 = (CellProposal) obj;
			if (o1.getNumberOfPossibilities() != o2.getNumberOfPossibilities()) {
				return o1.getNumberOfPossibilities() < o2.getNumberOfPossibilities() ? -1 : 1;
			} else if (o1.getRow() != o2.getRow()){
				return o1.getRow() < o2.getRow() ? -1 : 1;
			} else if (o1.getCol() != o2.getCol()){
				return o1.getCol() < o2.getCol() ? -1 : 1;
			} else {
				return 0;
			}
		}
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("CellProposal [" + row + "][" + col + "] (" + numberOfPossibilities + ") [");
		for (int i = 1; i <= N; i++) {
			if (isPossible(i)) {
				result.append(i + ",");
			}
		}
		result.append("]");
		return result.toString();
	}
}