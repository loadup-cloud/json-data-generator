package com.github.vincentrussell.json.datagenerator.impl;

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

import org.apache.commons.lang3.mutable.MutableInt;

/**
 * class used to support auto-incrementing index function in test data json.
 */
public class IndexHolder {
    private final ResettingMutableInt index;

    /**
     * index holder with index starting with another number.
     * @param startingIndex index to start from
     */
    public IndexHolder(final int startingIndex) {
        this.index = new ResettingMutableInt(startingIndex);
    }

    /**
     * get next index.
     *
     * @return the next index
     */
    public int getNextIndex() {
        return index.getNextNumber();
    }

    /**
     * reset the index
     */
    public void resetIndex() {
        index.reset();
    }

    /**
     * {@link MutableInt} that can be reset
     */
    private static class ResettingMutableInt extends MutableInt {
        private final int startingPoint;

        /**
         * constructor
         * @param startingPoint starting integer
         */
        ResettingMutableInt(final int startingPoint) {
            super(startingPoint);
            this.startingPoint = startingPoint;
        }

        /**
         * reset the index to that starting point
         */
        public void reset() {
            setValue(startingPoint);
        }

        /**
         * get the next number for this index; then increment
         * @return
         */
        public int getNextNumber() {
            try {
                return toInteger();
            } finally {
                increment();
            }
        }
    }
}
