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


import com.github.vincentrussell.json.datagenerator.TokenResolver;
import com.github.vincentrussell.json.datagenerator.functions.FunctionRegistry;
import com.github.vincentrussell.json.datagenerator.parser.FunctionParser;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

/**
 * {@link TokenResolver} implementation that will try to run the functions for the tockens
 */
public class FunctionTokenResolver implements TokenResolver {

    private final FunctionRegistry functionRegistry;

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionTokenResolver.class);

    public FunctionTokenResolver(final FunctionRegistry functionRegistry) {
        this.functionRegistry = functionRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveToken(final CharSequence s) {
        try {
            FunctionParser functionParser = new FunctionParser(
                new ByteArrayInputStream(s.toString().getBytes(Charsets.UTF_8)), Charsets.UTF_8);
            functionParser.setFunctionRegistry(functionRegistry);
            return functionParser.Parse();
        } catch (Throwable e) {
            LOGGER.warn(e.getMessage(), e);
            throw new IllegalArgumentException(new StringBuilder("cannot parse function: ")
                .append(s).toString(), e);
        }

    }
}
