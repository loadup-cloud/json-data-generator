package com.github.vincentrussell.json.datagenerator.functions.impl;

/*-
 * #%L
 * json-data-generator
 * %%
 * Copyright (C) 2022 - 2025 loadup_cloud
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.github.vincentrussell.json.datagenerator.functions.Function;
import com.github.vincentrussell.json.datagenerator.functions.FunctionInvocation;

import java.util.Random;

/**
 * function used to handle repeats within the json.  Just returns the number
 * of repeats that should be done.
 */
@Function(name = "repeat")
public final class Repeat {

    /**
     * gets the string of a random integer between
     * lower range (inclusive) and upper range (inclusive)
     * @param lowerRange lower range
     * @param upperRange upper range
     * @return the string of a random integer between
     * lower range (inclusive) and upper range (inclusive)
     */
    @FunctionInvocation
    public String repeat(final String lowerRange, final String upperRange) {
            Integer integer = Integer.parseInt(lowerRange);
            Integer integer2 = Integer.parseInt(upperRange);
            if (integer >= integer2 && !integer.equals(integer2)) {
                throw new IllegalArgumentException(
                        "the second number, " + upperRange
                                + ", must be greater than the first number, " + upperRange);
            } else if (integer.equals(integer2)) {
                return integer.toString();
            } else {
                return Integer.valueOf((
                        new Random().nextInt((integer2 - integer) + 1) + integer)).toString();
            }
    }

    /**
     * gets the string form of the passed in integer
     * @param integer integer as string
     * @return the string form of the passed in integer
     */
    @FunctionInvocation
    public String repeat(final String integer) {
       return Integer.valueOf(integer).toString();
    }

}
