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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.furcino.sudokulib.models.Cell;
import com.furcino.sudokulib.models.Sudoku;
import com.furcino.sudokulib.models.solvers.exceptions.SolvingException;
import com.furcino.sudokulib.models.solvers.exceptions.TimeoutException;

/**
 * The Class BaseSolver.
 * 
 * @author Martin Furek
 */
public abstract class BaseSolver {

    /** The Constant logger. */
    final static Logger logger = Logger.getLogger(BaseSolver.class);

    /** The sudoku dimensions. */
    static int N = 9;

    /** The sudoku. */
    protected Sudoku sudoku;

    /** The original. */
    protected Sudoku original;

    /** The time start. */
    protected long timeStart = 0;

    /** The time finished. */
    protected long timeFinished = 0;

    /** The time limit. */
    protected long timeLimit = 10000;

    /** The max solutions. */
    protected int maxSolutions = 1;

    /** The solutions. */
    protected List<Sudoku> solutions = new ArrayList<Sudoku>();

    /**
     * Instantiates a new base solver.
     *
     * @param sudoku the sudoku
     */
    public BaseSolver(Sudoku sudoku) {
        this.sudoku = new Sudoku(sudoku);
        this.sudoku.setSolution(sudoku.getSolution());
        this.original = new Sudoku(sudoku);
    }

    /**
     * Instantiates a new base solver.
     *
     * @param sudoku the sudoku
     * @param timeLimit the time limit
     * @param maxSolutions the max solutions
     */
    public BaseSolver(Sudoku sudoku, long timeLimit, int maxSolutions) {
        this.sudoku = sudoku;
        this.original = new Sudoku(sudoku);
        this.timeLimit = timeLimit;
        this.maxSolutions = maxSolutions;
    }

    /**
     * Solve.
     *
     * @return the number of solutions
     * @throws TimeoutException the timeout exception
     * @throws SolvingException the solving exception
     */
    public abstract int solve() throws TimeoutException, SolvingException;

    /**
     * Solve basic.
     *
     * @param toSolve the sudoku to solve
     * @return true, if successful
     * @throws SolvingException the solving exception
     */
    protected boolean solveBasic(Sudoku toSolve) throws SolvingException {
        int solvedCount = 0;

        // check grid
        for (int gridRow = 0; gridRow < 3; gridRow++) {
            for (int gridCol = 0; gridCol < 3; gridCol++) {
                for (int val = 1; val <= N; val++) {
                    if (!toSolve.isValueSolvedInGrid(gridRow, gridCol, val)) {
                        if (this.checkGridForSolutions(toSolve, gridRow, gridCol, val)) {
                            solvedCount++;
                        }
                    }
                }
            }
        }

        // check row
        for (int row = 0; row < N; row++) {
            for (int val = 1; val <= N; val++) {
                if (!toSolve.isValueSolvedInRow(row, val)) {
                    if (this.checkRowForSolutions(toSolve, row, val)) {
                        solvedCount++;
                    }
                }
            }
        }

        // check col
        for (int col = 0; col < N; col++) {
            for (int val = 1; val <= N; val++) {
                if (!toSolve.isValueSolvedInCol(col, val)) {
                    if (this.checkColForSolutions(toSolve, col, val)) {
                        solvedCount++;
                    }
                }
            }
        }

        if (solvedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check grid for solutions.
     *
     * @param toSolve the sudoku to solve
     * @param gridRow the grid row
     * @param gridCol the grid col
     * @param value the value
     * @return true, if successful
     * @throws SolvingException the solving exception
     */
    protected boolean checkGridForSolutions(Sudoku toSolve, int gridRow, int gridCol,
            int value) throws SolvingException {
        int possibilities = 0;
        int setX = 0;
        int setY = 0;

        // check if only one possibility
        for (int i = gridRow * 3; i < gridRow * 3 + 3; i++) {
            for (int j = gridCol * 3; j < gridCol * 3 + 3; j++) {
                if (toSolve.isCellValuePossible(i, j, value)) {
                    possibilities += 1;
                    setX = i;
                    setY = j;
                }
            }
        }

        // if yes, set and resolve
        if (possibilities == 1) {
            // if result set print result val
            if (this.sudoku.getSolution() != null) {
                logger.debug("Result value [" + setX + "][" + setY + "] is " 
                		+ this.sudoku.getSolution().getCellValue(setX, setY));
            }

            // if result set check first, else set new value
            if (this.sudoku.getSolution() != null && this.sudoku.getSolution().getCellValue(setX, setY) != value) {
                logger.error("Wrong Value Solved by Grid [" + setX + "][" + setY + "] with " + value);
                throw new SolvingException(this);
            } else {
                logger.debug("Solved by Grid [" + setX + "][" + setY + "] with " + value);
                toSolve.setCellValue(setX, setY, value);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check row for solutions.
     *
     * @param toSolve the sudoku to solve
     * @param row the row
     * @param value the value
     * @return true, if successful
     * @throws SolvingException the solving exception
     */
    protected boolean checkRowForSolutions(Sudoku toSolve, int row, int value) throws SolvingException {
        int possibilities = 0;
        int setX = 0;
        int setY = 0;

        // check if only one possibility
        for (int i = 0; i < N; i++) {
            if (toSolve.isCellValuePossible(row, i, value)) {
                possibilities += 1;
                setX = row;
                setY = i;
            }
        }

        // if yes, set and resolve
        if (possibilities == 1) {
            // if result set print result val
            if (this.sudoku.getSolution() != null) {
                logger.debug("Result value [" + setX + "][" + setY + "] is " 
                		+ this.sudoku.getSolution().getCellValue(setX,setY));
            }

            // if result set check first, else set new value
            if (this.sudoku.getSolution() != null && this.sudoku.getSolution().getCellValue(setX, setY) != value) {
                logger.error("Wrong Value Solved by Row  [" + setX + "][" + setY + "] with " + value);
                throw new SolvingException(this);
            } else {
                logger.debug("Solved by Row [" + setX + "][" + setY + "] with " + value);
                toSolve.setCellValue(setX, setY, value);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check column for solutions.
     *
     * @param toSolve the sudoku to solve
     * @param col the column
     * @param value the value
     * @return true, if successful
     * @throws SolvingException the solving exception
     */
    protected boolean checkColForSolutions(Sudoku toSolve, int col, int value) throws SolvingException {
        int possibilities = 0;
        int setX = 0;
        int setY = 0;

        // check if only one possibility
        for (int i = 0; i < N; i++) {
            if (toSolve.isCellValuePossible(i, col, value)) {
                possibilities += 1;
                setX = i;
                setY = col;
            }
        }

        // if yes, set and resolve
        if (possibilities == 1) {
            // if result set print result val
            if (this.sudoku.getSolution() != null) {
                logger.debug("Result value [" + setX + "][" + setY + "] is " 
                		+ this.sudoku.getSolution().getCellValue(setX,setY));
            }

            // if result set check first, else set new value
            if (this.sudoku.getSolution() != null && this.sudoku.getSolution().getCellValue(setX, setY) != value) {
                logger.error("Wrong Value Solved by Col  [" + setX + "][" + setY + "] with " + value);
                throw new SolvingException(this);
            } else {
                logger.debug("Solved by Col [" + setX + "][" + setY + "] with " + value);
                toSolve.setCellValue(setX, setY, value);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Solve grid locked candidates.
     *
     * @param toSolve the sudoku to solve
     * @return true, if successful
     */
    protected boolean solveGridLockedCandidates(Sudoku toSolve) {
        int solvedCount = 0;
        for (int gridRow = 0; gridRow < 3; gridRow++) {
            for (int gridCol = 0; gridCol < 3; gridCol++) {
                for (int val = 1; val <= N; val++) {
                    if (!toSolve.isValueSolvedInGrid(gridRow, gridCol, val)) {
                        if (resolveGridLockedCandidates(toSolve, gridRow, gridCol, val)) {
                            solvedCount++;
                        }
                    }
                }
            }
        }

        if (solvedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Resolve grid locked candidates.
     *
     * @param toSolve the sudoku to solve
     * @param gridRow the grid row
     * @param gridCol the grid col
     * @param value the value
     * @return true, if successful
     */
    protected boolean resolveGridLockedCandidates(Sudoku toSolve, int gridRow, int gridCol, int value) {
        int possibilities = 0;
        int[] row = new int[N];
        int[] col = new int[N];

        // check rows and cols for possibilities
        for (int i = gridRow * 3; i < gridRow * 3 + 3; i++) {
            for (int j = gridCol * 3; j < gridCol * 3 + 3; j++) {
                if (toSolve.isCellValuePossible(i, j, value)) {
                    row[possibilities] = i;
                    col[possibilities] = j;
                    possibilities++;
                }
            }
        }

        // if more than 3 possibilities, or just one, this method does not apply
        if (possibilities > 3 || possibilities == 1) {
            return false;
        }

        // check if all possibilities are in one row and resolve
        for (int i = 0; i < possibilities - 1; i++) {
            if (row[i] != row[i + 1]) {
                // exit since there is no solution here
                break;
            }

            // solution found
            if (i == possibilities - 2) {
                int changes = 0;
                for (int j = 0; j < N; j++) {
                    if ((j < gridCol * 3 || j >= gridCol * 3 + 3) && toSolve.isCellValuePossible(row[i], j, value)) {
                        toSolve.setCellValueAsImpossible(row[i], j, value);
                        logger.debug("Removing row [" + row[i] + "][" + j + "] with " + value);
                        changes++;
                    }
                }
                if (changes > 0) {
                    if (logger.isDebugEnabled()) {
                        for (int check = 0; check < possibilities; check++) {
                            logger.debug("Based on row " + (check + 1) + "/" + possibilities + " [" + row[check] + "][" + col[check] + "] with " + value);
                        }
                        logger.debug(toSolve.printPossibilities(value) + toSolve.toString());
                    }
                    return true;
                }
            }
        }

        // check if all possibilities are in one column and resolve
        for (int i = 0; i < possibilities - 1; i++) {
            if (col[i] != col[i + 1]) {
                // exit since there is no solution here
                break;
            }

            // solution found
            if (i == possibilities - 2) {
                int changes = 0;
                for (int j = 0; j < N; j++) {
                    if ((j < gridRow * 3 || j >= gridRow * 3 + 3) && this.sudoku.isCellValuePossible(j, col[i], value)) {
                        this.sudoku.setCellValueAsImpossible(j, col[i], value);
                        logger.debug("Removing col [" + j + "][" + col[i] + "] for " + value);
                        changes++;
                    }
                }
                if (changes > 0) {
                    if (logger.isDebugEnabled()) {
                        for (int check = 0; check < possibilities; check++) {
                            logger.debug("Based on col " + (check + 1) + "/" + possibilities + " [" + row[check] + "][" + col[check] + "] with " + value);
                        }
                        logger.debug(this.sudoku.printPossibilities(value) + this.sudoku.toString());
                    }
                    return true;
                }
            }
        }

        return false;
    }
    
    /**
     * Solve row and col locked candidates.
     *
     * @param toSolve the sudoku to solve
     * @return true, if successful
     */
    protected boolean solveRowAndColLockedCandidates(Sudoku toSolve) {
        int solvedCount = 0;
        for (int val = 1; val <= N; val++) {
	        for (int row = 0; row < N; row++) {
	        	if (!toSolve.isValueSolvedInRow(row, val)) {
		        	if (resolveRowLockedCandidates(toSolve, row, val)) {
		        		solvedCount++;
		        	}
                }
            }
	        for (int col = 0; col < N; col++) {
	        	if (!toSolve.isValueSolvedInCol(col, val)) {
		        	if (resolveColLockedCandidates(toSolve, col, val)) {
		        		solvedCount++;
		        	}
                }
            }
        }

        if (solvedCount > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Resolve row locked candidates.
     *
     * @param toSolve the sudoku to solve
     * @param row the row
     * @param value the value
     * @return true, if successful
     */
    protected boolean resolveRowLockedCandidates(Sudoku toSolve, int row, int value) {
    	int possibilities = 0;
    	int gridRow = row/3;
    	int gridCol = 0;
    	boolean allInOneGrid = true;
        int changes = 0;
        
        for (int col = 0; col < N; col++) {
            if (!toSolve.isCellValuePossible(row, col, value)) {
            	if (possibilities == 0) {
            		gridCol = col/3;
            	}
            	possibilities++;
            	if (gridCol != col/3) {
            		allInOneGrid = false;
            	}
            }
        }
        
        if (possibilities > 1 && possibilities <= 3 && allInOneGrid) {
        	for (int r = gridRow*3; r < gridRow*3+3; r++) {
        		if (r != row) {
	        		for (int c = gridCol*3; c < gridCol*3+3; c++) {
	        			if (toSolve.isCellUserValuePossible(r, c, value)) {
	        				logger.debug("Removing locked row [" + r + "][" + c + "] for " + value);
		        			toSolve.setCellValueAsImpossible(r, c, value);
		            		changes++;
	            		}
	            	}
        		}
        	}
        }
        
        if (changes > 0) {
        	return true;
        }
        return false;
    }
    
    /**
     * Resolve col locked candidates.
     *
     * @param toSolve the sudoku to solve
     * @param col the col
     * @param value the value
     * @return true, if successful
     */
    protected boolean resolveColLockedCandidates(Sudoku toSolve, int col, int value) {
    	int possibilities = 0;
    	int gridRow = 0;
    	int gridCol = col/3;
    	boolean allInOneGrid = true;
        int changes = 0;
        
        for (int row = 0; row < N; row++) {
            if (!toSolve.isCellValuePossible(row, col, value)) {
            	if (possibilities == 0) {
            		gridRow = row/3;
            	}
            	possibilities++;
            	if (gridRow != row/3) {
            		allInOneGrid = false;
            	}
            }
        }
        
        if (possibilities > 1 && possibilities <= 3 && allInOneGrid) {
        	for (int r = gridRow*3; r < gridRow*3+3; r++) {
        		for (int c = gridCol*3; c < gridCol*3+3; c++) {
            		if (c != col) {
	        			if (toSolve.isCellUserValuePossible(r, c, value)) {
		        			logger.debug("Removing locked col [" + r + "][" + c + "] for " + value);
		            		toSolve.setCellValueAsImpossible(r, c, value);
		            		changes++;
	        			}
	            	}
        		}
        	}
        }
        
        if (changes > 0) {
        	return true;
        }
        return false;
    }

    /**
     * Solve advanced X wing and skyscraper.
     *
     * @param toSolve the sudoku to solve
     * @return true, if successful
     */
    protected boolean solveAdvancedXWingAndSkyscraper(Sudoku toSolve) {

        int changes = 0;
        for (int val = 1; val <= N; val++) {
            List<List<Cell>> cellsRows = new ArrayList<>();
            for (int row = 0; row < N; row++) {
                List<Cell> temp = new ArrayList<>();
                int possibilities = 0;
                for (int col = 0; col < N; col++) {
                    if (sudoku.isCellValuePossible(row, col, val)) {
                        temp.add(new Cell(row, col, val));
                        possibilities++;
                    }
                }
                if (possibilities == 2) {
                    cellsRows.add(temp);
                }
            }
            if (cellsRows.size() >= 2) {
                changes += resolveXWingAndSkyscraperRows(toSolve, cellsRows);
            }

            List<List<Cell>> cellsCols = new ArrayList<>();
            for (int col = 0; col < N; col++) {
                List<Cell> temp = new ArrayList<>();
                int possibilities = 0;
                for (int row = 0; row < N; row++) {
                    if (sudoku.isCellValuePossible(row, col, val)) {
                        temp.add(new Cell(row, col, val));
                        possibilities++;
                    }
                }
                if (possibilities == 2) {
                    cellsCols.add(temp);
                }
            }
            if (cellsCols.size() >= 2) {
                changes += resolveXWingAndSkyscraperCols(toSolve, cellsCols);
            }
        }

        if (changes > 0) {
            return true;
        }
        return false;
    }

    /**
     * Resolve X wing and skyscraper rows.
     *
     * @param toSolve the sudoku to solve
     * @param cellsRows the cells rows
     * @return the int
     */
    private int resolveXWingAndSkyscraperRows(Sudoku toSolve, List<List<Cell>> cellsRows) {
        int changes = 0;
        for (int index = 0; index < cellsRows.size(); index++) {
            int colOne = cellsRows.get(index).get(0).getCol();
            int colTwo = cellsRows.get(index).get(1).getCol();
            int row = cellsRows.get(index).get(0).getRow();
            int value = cellsRows.get(index).get(0).getValue();

            for (int indexTested = index + 1; indexTested < cellsRows.size(); indexTested++) {
                int rowTested = cellsRows.get(indexTested).get(0).getRow();
                int colTestedOne = cellsRows.get(indexTested).get(0).getCol();
                int colTestedTwo = cellsRows.get(indexTested).get(1).getCol();

                if (colTestedOne == colOne || colTestedTwo == colTwo) {
                    changes += resolveAlternativesOverlap(toSolve, value, row, colOne, rowTested, colTestedTwo, row, colTwo, rowTested, colTestedOne);
                }
            }
        }
        return changes;
    }

    /**
     * Resolve X wing and skyscraper cols.
     *
     * @param toSolve the sudoku to solve
     * @param cellsCols the cells cols
     * @return the int
     */
    private int resolveXWingAndSkyscraperCols(Sudoku toSolve, List<List<Cell>> cellsCols) {
        int changes = 0;
        for (int index = 0; index < cellsCols.size(); index++) {
            int rowOne = cellsCols.get(index).get(0).getRow();
            int rowTwo = cellsCols.get(index).get(1).getRow();
            int col = cellsCols.get(index).get(0).getCol();
            int value = cellsCols.get(index).get(0).getValue();

            for (int indexTested = index + 1; indexTested < cellsCols.size(); indexTested++) {
                int colTested = cellsCols.get(indexTested).get(0).getCol();
                int rowTestedOne = cellsCols.get(indexTested).get(0).getRow();
                int rowTestedTwo = cellsCols.get(indexTested).get(1).getRow();

                if (rowTestedOne == rowOne || rowTestedTwo == rowTwo) {
                    changes += resolveAlternativesOverlap(toSolve, value, rowOne, col, rowTestedTwo, colTested, rowTwo, col, rowTestedOne, colTested);
                }
            }
        }
        return changes;
    }

    /**
     * Resolve alternatives overlap.
     *
     * @param toSolve the sudoku to solve
     * @param value the value
     * @param rowOne the row one
     * @param colOne the column one
     * @param rowTwo the row two
     * @param colTwo the column two
     * @param alternativeRowOne the alternative row one
     * @param alternativeColOne the alternative column one
     * @param alternativeRowTwo the alternative row two
     * @param alternativeColTwo the alternative column two
     * @return the int
     */
    private int resolveAlternativesOverlap(Sudoku toSolve, int value, int rowOne, int colOne, int rowTwo, int colTwo,
            int alternativeRowOne, int alternativeColOne, int alternativeRowTwo, int alternativeColTwo){

        int changes = 0;
        Sudoku posibilitiesOne = new Sudoku();
        Sudoku posibilitiesTwo = new Sudoku();
        
        try {
	        // set possible values scenario 1
	        posibilitiesOne.setCellValue(rowOne,colOne,value);
	        posibilitiesOne.setCellValue(rowTwo,colTwo,value);
	        // set possible values scenario 2
	        posibilitiesTwo.setCellValue(alternativeRowOne,alternativeColOne,value);
	        posibilitiesTwo.setCellValue(alternativeRowTwo,alternativeColTwo,value);
        } catch (IllegalArgumentException e) {
        	return changes;
        }

        for(int solutionRow = 0; solutionRow<N; solutionRow++) {
            for (int solutionCol = 0; solutionCol < N; solutionCol++) {
                if (!posibilitiesOne.isCellValuePossible(solutionRow, solutionCol, value)
                        && !posibilitiesTwo.isCellValuePossible(solutionRow, solutionCol, value)) {
                    if (toSolve.isCellValuePossible(solutionRow, solutionCol, value)) {
                        toSolve.setCellValueAsImpossible(solutionRow, solutionCol, value);
                        logger.debug("Setting cell value as impossible [" + solutionRow 
                        		+ "][" + solutionCol + "], value: (" + value + ")");
                        logger.debug(toSolve.printPossibilities(value));
                        logger.debug(toSolve);
                        changes++;
                    }
                }
            }
        }
        return changes;
    }

	/**
	 * Gets the time limit.
	 *
	 * @return the time limit
	 */
	public long getTimeLimit() {
		return timeLimit;
	}

	/**
	 * Sets the time limit.
	 *
	 * @param timeLimit the new time limit
	 */
	public void setTimeLimit(long timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * Gets the sudoku.
	 *
	 * @return the sudoku
	 */
	public Sudoku getSudoku() {
		return sudoku;
	}

	/**
	 * Gets the original.
	 *
	 * @return the original
	 */
	public Sudoku getOriginal() {
		return original;
	}

	/**
	 * Gets the time start.
	 *
	 * @return the time start
	 */
	public long getTimeStart() {
		return timeStart;
	}

	/**
	 * Sets the time start.
	 *
	 * @param timeStart the new time start
	 */
	protected void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}
	
	/**
	 * Gets the time finished.
	 *
	 * @return the time finished
	 */
	public long getTimeFinished() {
		return timeFinished;
	}
	
	/**
	 * Sets the time finished.
	 *
	 * @param timeFinished the new time finished
	 */
	protected void setTimeFinished(long timeFinished) {
		this.timeFinished = timeFinished;
	}

	/**
	 * Gets the solutions.
	 *
	 * @return the solutions
	 */
	public List<Sudoku> getSolutions() {
		return solutions;
	}
	
	/**
	 * Gets the first solution.
	 *
	 * @return the first solution
	 */
	public Sudoku getFirstSolution() {
		if (solutions != null && solutions.size() > 0) {
			return solutions.get(0);
		}
		return null;
	}

	/**
	 * Gets the max solutions.
	 *
	 * @return the max solutions
	 */
	public int getMaxSolutions() {
		return maxSolutions;
	}

	/**
	 * Sets the max solutions.
	 *
	 * @param maxSolutions the new max solutions
	 */
	public void setMaxSolutions(int maxSolutions) {
		this.maxSolutions = maxSolutions;
	}
	
	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public long getDuration() {
		return timeFinished - timeStart;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Solver [solutions=" + this.solutions.size() + "]";
	}
}
