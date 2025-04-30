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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * functions with time
 */
@Function(name = "time")
public class Time {

    public static final String DEFAULT_INPUT_FORMAT = "h[:mm][ ]a";
    public static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter
            .ofPattern(DEFAULT_INPUT_FORMAT);

    /**
     * generate a time representing "now"
     * @return the result
     */
    @FunctionInvocation
    public String time() {
        return time(DEFAULT_INPUT_FORMAT);
    }

    /**
     * generate a time representing now with a particular time format
     * @param outputTimeFormat the output time format
     * @return the formatted time
     */
    @FunctionInvocation
    public String time(final String outputTimeFormat) {
        return format(LocalTime.now(), outputTimeFormat);
    }
    private String format(final LocalTime localTime, final String defaultInputFormat) {
        return localTime.format(DateTimeFormatter.ofPattern(defaultInputFormat));
    }


    /**
     * random time in between two ranges
     * @param startTime in the following format "h[:mm][ ]a"
     * @param endTime in the following format "h[:mm][ ]a"
     * @param outputFormat the output time format
     * @return the formatted time
     */
    @FunctionInvocation
    public String time(final String startTime, final String endTime,
                       final String outputFormat) {
        LocalTime time1 = LocalTime.parse(startTime, DEFAULT_FORMATTER);
        LocalTime time2 = LocalTime.parse(endTime, DEFAULT_FORMATTER);
        int secondOfDayTime1 = time1.toSecondOfDay();
        int secondOfDayTime2 = time2.toSecondOfDay();
        Random random = new Random();
        int randomSecondOfDay = secondOfDayTime1 + random
                .nextInt(secondOfDayTime2 - secondOfDayTime1);
        return LocalTime.ofSecondOfDay(randomSecondOfDay).format(DateTimeFormatter
                .ofPattern(outputFormat));
    }

}
