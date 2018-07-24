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
package com.furcino.sudokulib.models.solvers.exceptions;

import com.furcino.sudokulib.models.solvers.BaseSolver;

/**
 * The Class SolvingException.
 *
 * @author Martin Furek
 */
public class SolvingException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5545541222483116123L;
	
	/** The solver. */
	private BaseSolver solver;

	/**
	 * Instantiates a new solving exception.
	 *
	 * @param solver the solver
	 */
	public SolvingException(BaseSolver solver) {
		super();
		this.solver = solver;
	}

	/**
	 * Instantiates a new solving exception.
	 *
	 * @param message the message
	 */
	public SolvingException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new solving exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SolvingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new solving exception.
	 *
	 * @param cause the cause
	 */
	public SolvingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Gets the solver.
	 *
	 * @return the solver
	 */
	public BaseSolver getSolver() {
		return solver;
	}
}
