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
 * Puzzle difficulty .
 *
 * @author Martin Furek
 */
public enum SudokuDifficulty {
    
    /** The easy. */
    EASY(1),
    
    /** The normal. */
    NORMAL(2),
    
    /** The hard. */
    HARD(3),
    
    /** The very hard. */
    VERY_HARD(4);

    /** The num val. */
    private int numVal;

    /**
     * Instantiates a new sudoku difficulty.
     *
     * @param numVal the num val
     */
    SudokuDifficulty(int numVal) {
        this.numVal = numVal;
    }

    /**
     * Gets the numerical value.
     *
     * @return the numerical value
     */
    public int getNumVal() {
        return numVal;
    }
}
