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

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * convert dates to different formats
 */
@Function(name = "dateFormat")
public class DateFormat {

    /**
     * function call to change format of date
     * @param dateToParse the date to convert
     * @param inputFormat the {@link SimpleDateFormat} to convert from
     * @param outputFormat the{@link SimpleDateFormat} to convert to
     * @return the converted date
     */
    @FunctionInvocation
    public String dateFormat(final String dateToParse, final String inputFormat,
        final String outputFormat) {
        try {
            java.text.DateFormat incoming = new SimpleDateFormat(inputFormat);
            java.text.DateFormat outgoingFormat = new SimpleDateFormat(outputFormat);
            java.util.Date date = incoming.parse(dateToParse);
            return outgoingFormat.format(date);
        } catch (NullPointerException | ParseException e) {
            throw new IllegalArgumentException(e);
        }

    }




}
