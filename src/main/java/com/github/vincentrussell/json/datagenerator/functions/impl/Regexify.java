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
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.FakerContext;
import net.datafaker.service.RandomService;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Generate random strings based on regex
 */
@Function(name = "regexify")
public class Regexify {

    @SuppressWarnings("checkstyle:magicnumber")
    private static final LoadingCache<String, FakeValuesService>
            FAKE_VALUES_SERVICE_CACHE = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                       public FakeValuesService load(final String locale) {
                           return new FakeValuesService();
                       }
                   }
            );

    @FunctionInvocation
    public final String regexify(final String regex) {
        return regexify("en-US", regex);
    }

    @FunctionInvocation
    public final String regexify(final String locale, final String regex) {
        FakerContext context = new FakerContext(new Locale(locale), new RandomService());
        return getFakeValuesService(locale).regexify(regex, context);
    }

    private static FakeValuesService getFakeValuesService(final String locale) {
        try {
            return FAKE_VALUES_SERVICE_CACHE.get(locale);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}
