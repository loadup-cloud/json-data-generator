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
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * random long within range
 */
@Function(name = "long")
public class RandomLong {

    public static final Pattern LONG_PATTERN = Pattern.compile("(\\d+)L");
    public static final RandomDataGenerator RANDOM_DATA_GENERATOR = new RandomDataGenerator();

    /**
     * random long within range
     * @param min minimum number
     * @param max maximum number
     * @return the result
     */
    @FunctionInvocation
    public String getRandomLong(final String min, final String max) {
        return getRandomLong(parseLong(min), parseLong(max));
    }

    private String getRandomLong(final Long min, final Long max) {
        long randomNumber = RANDOM_DATA_GENERATOR.nextLong(min, max);
        return Long.toString(randomNumber);
    }


    private Long parseLong(final String string) {
        Matcher matcher = LONG_PATTERN.matcher(string);
        if (matcher.matches()) {
            return Long.parseLong(matcher.group(1));
        }
        return Long.parseLong(string);
    }
}
