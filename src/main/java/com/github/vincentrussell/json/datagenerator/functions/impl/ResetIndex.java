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
 * reset an index
 */
@Function(name = "resetIndex")
public class ResetIndex {

    private final FunctionRegistry functionRegistry;

    public ResetIndex(final FunctionRegistry functionRegistry) {
        this.functionRegistry = functionRegistry;
    }


    /**
     * reset the default index
     * @return empty string
     */
    @FunctionInvocation
    public String resetIndex() {
        return resetIndex(Index.DEFAULT);
    }

    /**
     * reset index for given index name
     * @param indexName name of index
     * @return empty string
     */
    @FunctionInvocation
    public String resetIndex(final String indexName) {
        IndexHolder indexHolder = getIndexHolder(indexName);
        indexHolder.resetIndex();
        return "";
    }


    private IndexHolder getIndexHolder(final String indexName) {
        Map<String, IndexHolder> stringIndexHolderMap = functionRegistry.getStringIndexHolderMap();
        if (stringIndexHolderMap.containsKey(indexName)) {
            return stringIndexHolderMap.get(indexName);
        }
        throw new IllegalStateException("could not find index with name " + indexName);
    }
}
