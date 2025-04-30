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
 * functions with dates
 */
@Function(name = "date")
public class Date {

    public static final String DEFAULT_DATE_STRING = "EEE, d MMM yyyy HH:mm:ss z";
    public static final String DEFAULT_INPUT_FORMAT = "dd-MM-yyyy HH:mm:ss";

    /**
     * generate a date with "now"
     * @return the result
     */
    @FunctionInvocation
    public String date() {
        return getSimpleDateFormat().format(new java.util.Date());
    }

    /**
     * function call for now date with {@link SimpleDateFormat}
     * @param outputFormat simple date outputFormat
     * @return the result
     */
    @FunctionInvocation
    public String date(final String outputFormat) {
        return new SimpleDateFormat(outputFormat).format(new java.util.Date());
    }

    /**
     * random date between begin date and end date
     * @param beginDate date with the dd-MM-yyyy HH:mm:ss format
     * @param endDate date with the dd-MM-yyyy HH:mm:ss format
     * @param outputFormat output format in {@link SimpleDateFormat} format
     * @return formatted date
     */
    @FunctionInvocation
    public String date(final String beginDate, final String endDate, final String outputFormat) {
        return date(beginDate, endDate, DEFAULT_INPUT_FORMAT, outputFormat);
    }

    /**
     * random date between begin date and end date
     * @param beginDate date with the dd-MM-yyyy HH:mm:ss format
     * @param endDate date with the dd-MM-yyyy HH:mm:ss format
     * @param inputFormat input format in {@link SimpleDateFormat} format
     * @param outputFormat output format in {@link SimpleDateFormat} format
     * @return formatted date
     */
    @FunctionInvocation
    public String date(final String beginDate, final String endDate,
                       final String inputFormat, final String outputFormat) {
        try {
            DateFormat formatter = new SimpleDateFormat(inputFormat);
            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat);
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(beginDate));
            Long beginLong = cal.getTimeInMillis();
            cal.setTime(formatter.parse(endDate));
            Long endLong = cal.getTimeInMillis();
            long randomLong = (long) (beginLong + Math.random() * (endLong - beginLong));
            cal.setTimeInMillis(randomLong);
            return dateFormat.format(new java.util.Date(randomLong));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     *
     * @param beginDate date with the dd-MM-yyyy HH:mm:ss format
     * @param endDate date with the dd-MM-yyyy HH:mm:ss format
     * @return formated date in EEE, d MMM yyyy HH:mm:ss z format
     */
    @FunctionInvocation
    public String date(final String beginDate, final String endDate) {
        return date(beginDate, endDate, DEFAULT_DATE_STRING);
    }

    private SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(DEFAULT_DATE_STRING);
    }



}
