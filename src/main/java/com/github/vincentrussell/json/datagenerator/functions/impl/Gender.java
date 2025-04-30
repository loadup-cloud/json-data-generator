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

/**
 * function to return a random gender
 */
@Function(name = "gender")
public class Gender {

    /**
     * random gender with 50/50 possibility of being male
     * @return the result
     */
    @SuppressWarnings("checkstyle:magicnumber")
    @FunctionInvocation
    public String gender() {
        return getMaleOrFemale((float) 0.5);
    }

    private String getMaleOrFemale(final float probability) {
        return (Math.random() < probability) ? "male" : "female";
    }

    /**
     * random gender with probability of being male
     * @param probability float probability of being male
     * @return the result
     */
    @FunctionInvocation
    public String gender(final String probability) {
        return Boolean.valueOf(Math.random() < Float.parseFloat(probability)).toString();
    }
}
