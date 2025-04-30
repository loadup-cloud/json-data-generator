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
import com.github.vincentrussell.json.datagenerator.functions.FunctionRegistry;
import com.github.vincentrussell.json.datagenerator.impl.IndexHolder;

import java.util.Map;

/**
 * an incrementing index integer
 */
@Function(name = "index")
public class Index {

    static final String DEFAULT = "DEFAULT";

    private final FunctionRegistry functionRegistry;

    public Index(final FunctionRegistry functionRegistry) {
        this.functionRegistry = functionRegistry;
    }

    /**
     * function call with default index name
     * @return the result
     */
    @FunctionInvocation
    public String getIndex() {
        return getIndex(DEFAULT);
    }

    /**
     * index for given index name
     * @param indexName name of index
     * @return the result
     */
    @FunctionInvocation
    public String getIndex(final String indexName) {
        try {
            return getIndex(DEFAULT, Integer.parseInt(indexName));
        } catch (NumberFormatException e) {
            return "" + getIndexHolder(indexName).getNextIndex();
        }
    }

    /**
     * index for index name
     * @param indexName name of index
     * @param startingPoint starting point integer for index
     * @return the result
     */
    @FunctionInvocation
    public String getIndex(final String indexName, final String startingPoint) {
        return getIndex(indexName, Integer.parseInt(startingPoint));
    }

    private String getIndex(final String indexName, final int startingPoint) {
        return "" + getIndexHolder(indexName, startingPoint).getNextIndex();
    }

    private IndexHolder getIndexHolder(final String indexName) {
        return getIndexHolder(indexName, 0);
    }

    private IndexHolder getIndexHolder(final String indexName, final int startingPoint) {
        Map<String, IndexHolder> stringIndexHolderMap = functionRegistry.getStringIndexHolderMap();
        if (stringIndexHolderMap.containsKey(indexName)) {
            return stringIndexHolderMap.get(indexName);
        }
        IndexHolder indexHolder = new IndexHolder(startingPoint);
        stringIndexHolderMap.put(indexName, indexHolder);
        return indexHolder;
    }
}
