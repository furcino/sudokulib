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
package com.furcino.sudokulib.util;

import org.apache.log4j.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CommonUtilTest extends TestCase {

	final static Logger logger = Logger.getLogger(CommonUtilTest.class);

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public CommonUtilTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CommonUtilTest.class);
	}

    public void testShuffleArray() {
	    int input[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
	    String inputString = "";
        for (int i = 0; i < input.length; i++) {
            inputString = inputString + input[i];
        }

        CommonUtil.shuffleArray(input);
        String outputString = "";
        for (int i = 0; i < input.length; i++) {
            outputString = outputString + input[i];
        }

        assertTrue("Shuffle output is the same as input!", !inputString.equals(outputString));
    }
}
