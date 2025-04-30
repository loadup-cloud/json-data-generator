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

import java.util.Formatter;
import java.util.Random;

/**
 * get random double in range
 */
@Function(name = "double")
public class RandomDouble {

    private static final Random RANDOM = new Random();

    private String getRandomDouble(final Double min, final Double max, final String format) {
        double randomNumber = min + (max - min) * RANDOM.nextDouble();

        if (format != null) {
            return String.format(format, randomNumber);
        }
        return Double.toString(randomNumber);
    }

    /**
     * get random double in range
     * @param min min number
     * @param max max number
     * @return the result
     */
    @FunctionInvocation
    public String getRandomDouble(final String min, final String max) {
        return getRandomDouble(Double.parseDouble(min), Double.parseDouble(max), null);
    }

    /**
     * get random number with format (eg. "%.2f")
     * @param min min number
     * @param max max number
     * @param format format in {@link Formatter} format
     * @return the result
     */
    @FunctionInvocation
    public String getRandomDouble(final String min, final String max, final String format) {
        return getRandomDouble(Double.parseDouble(min), Double.parseDouble(max), format);
    }

}
