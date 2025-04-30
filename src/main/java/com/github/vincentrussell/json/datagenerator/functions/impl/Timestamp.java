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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * timestamp (milliseconds, between the current time and midnight, January 1, 1970 UTC):
 */
@Function(name = "timestamp")
public class Timestamp {

    /**
     * timestamp based on "now"
     * @return the result
     */
    @FunctionInvocation
    public String timestamp() {
        return new java.util.Date().getTime() + "";
    }

    /**
     * random timestamp between two dates
     * @param beginDate beginning date in format "dd-MM-yyyy HH:mm:ss"
     * @param endDate ending date in format "dd-MM-yyyy HH:mm:ss"
     * @return the result
     */
    @FunctionInvocation
    public String timestamp(final String beginDate, final String endDate) {
        try {
            DateFormat formatter = new SimpleDateFormat(Date.DEFAULT_INPUT_FORMAT);
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(beginDate));
            Long beginLong = cal.getTimeInMillis();
            cal.setTime(formatter.parse(endDate));
            Long endLong = cal.getTimeInMillis();
            long randomLong = (long) (beginLong + Math.random() * (endLong - beginLong));
            return randomLong + "";
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
