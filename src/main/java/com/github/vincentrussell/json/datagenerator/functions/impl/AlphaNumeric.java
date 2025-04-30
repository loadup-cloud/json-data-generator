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
import org.apache.commons.lang3.RandomStringUtils;

/**
 * random string with alpha-numeric characters (defaults to between 10 and 20 characters)
 */
@Function(name = "alphaNumeric")
public class AlphaNumeric {

    /**
     * Function call with min and max arguments
     * @param min minium length
     * @param max maximum length
     * @return the result
     */
    @FunctionInvocation
    public String getAlphaNumeric(final String min, final String max) {
        int randomInt = FunctionUtils.getRandomInteger(Integer.parseInt(min),
            Integer.parseInt(max));
        return getRandomAlphabetic(randomInt);
    }

    /**
     * Default function call
     * @return the result
     */
    @FunctionInvocation
    @SuppressWarnings("checkstyle:magicnumber")
    public String getAlphaNumeric() {
        int randomInt = FunctionUtils.getRandomInteger(10, 20);
        return getRandomAlphabetic(randomInt);
    }

    /**
     * Function call with length
     * @param length length of string
     * @return the result
     */
    @FunctionInvocation
    public String getAlphaNumeric(final String length) {
        return getRandomAlphabetic(Integer.parseInt(length));
    }

    private String getRandomAlphabetic(final int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

}
