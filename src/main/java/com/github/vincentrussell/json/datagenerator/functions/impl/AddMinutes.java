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
 * addMinutes to Date function
 */
@Function(name = "addMinutes")
public class AddMinutes extends AbstractAddDates {

    /**
     * add minutes to date
     * @param format the date format
     * @param date the date
     * @param minutes the number of minutes to add
     * @return the new date
     */
    @FunctionInvocation
    public final String addMinutes(final String format, final String date, final String minutes) {
       return super.addInterval(format, date, minutes);
    }

    /**
     * add minutes to the date
     * @param date the date
     * @param minutes the number of minutes to add
     * @return the new date
     */
    @FunctionInvocation
    public final String addMinutes(final String date, final String minutes) {
       return super.addInterval(date, minutes);
    }

    @Override
    protected final String getMethodName() {
        return "addMinutes";
    }
}
