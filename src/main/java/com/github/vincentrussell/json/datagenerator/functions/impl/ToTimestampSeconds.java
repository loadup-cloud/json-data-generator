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
 * lower case a string
 */
@Function(name = "toTimestampSeconds")
public class ToTimestampSeconds {

    public static final int ONE_THOUSAND_MILLISECONDS = 1000;
    public static final String DEFAULT_DATE_STRING = "EEE, d MMM yyyy HH:mm:ss z";

    /**
     * convert date string to timestamp
     * @param string
     * @return the timestamp of that string
     */
    @FunctionInvocation
    public String toTimestampSeconds(final String string) {
        return toTimestampSeconds(string, DEFAULT_DATE_STRING);
    }


    /**
     * convert date string to timestamp
     * @param string
     * @param format
     * @return the timestamp of that string
     */
    @FunctionInvocation
    public String toTimestampSeconds(final String string, final String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(formatter.parse(string));
            Long timeInMillis = cal.getTimeInMillis();
            return "" + (timeInMillis / ONE_THOUSAND_MILLISECONDS);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
