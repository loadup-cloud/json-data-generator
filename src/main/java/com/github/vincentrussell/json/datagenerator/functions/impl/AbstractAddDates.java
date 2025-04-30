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

import org.apache.commons.lang3.time.DateUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * abstract add time intervals to Date function
 */
public abstract class AbstractAddDates {

    private Method method;

    /**
     * call this method from the subclasses to add an interval to a date
     * @param format the date format
     * @param date the date
     * @param interval the interval to add (or subtract)
     * @return
     */
    protected String addInterval(final String format, final String date, final String interval) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            java.util.Date myDate = dateFormat.parse(date);
            return dateFormat.format(addInterval(myDate, Integer.valueOf(interval)));
        } catch (ParseException | NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * call this method from the subclasses to add an interval to a date
     * @param date the date
     * @param interval the interval to add (or subtract)
     * @return
     */
    protected String addInterval(final String date, final String interval) {
        return addInterval(Date.DEFAULT_INPUT_FORMAT, date, interval);
    }


    private synchronized Method getMethod() throws NoSuchMethodException {
        return DateUtils.class.getMethod(getMethodName(), java.util.Date.class, int.class);
    }

    private java.util.Date addInterval(final java.util.Date oldDate, final Integer amount) {
        try {
            if (method == null) {
                method = getMethod();
                method.setAccessible(true);
            }
            return (java.util.Date) method.invoke(null, oldDate, amount);
        } catch (IllegalAccessException | NoSuchMethodException
            | SecurityException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * override this method to indicate with method on {@link DateUtils} could be called
     * @return the method name on {@link DateUtils}
     */
    protected abstract String getMethodName();

}
