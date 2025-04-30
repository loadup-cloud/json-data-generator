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

import java.text.DecimalFormat;
import java.util.Random;

/**
 * random social security number
 */
@Function(name = "ssn")
public class Ssn {

    private static final Random RANDOM = new Random();

    /**
     * get random social security number
     * @return the result
     */
    @FunctionInvocation
    @SuppressWarnings("checkstyle:magicnumber")
    public String ssn() {
        int num1 = RANDOM.nextInt(799 - 1) + 1;
        int num2 = RANDOM.nextInt(99 - 1) + 1;
        int num3 = RANDOM.nextInt(9999 - 1) + 1;

        DecimalFormat df1 = new DecimalFormat("000");
        DecimalFormat df2 = new DecimalFormat("00");
        DecimalFormat df3 = new DecimalFormat("0000");

        String ssn = df1.format(num1) + "-" + df2.format(num2) + "-" + df3.format(num3);
        return ssn;
    }

}
