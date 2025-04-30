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

import java.util.Random;

/**
 * lowercase hex string
 */
@Function(name = "hex")
public class Hex {

    private final Random random = new Random();

    /**
     * random hex string for random 16 bytes
     * @return the result
     */
    @FunctionInvocation
    @SuppressWarnings("checkstyle:magicnumber")
    public String hex() {
        return getHexString(16);
    }

    /**
     * random hexstring with specified byte size
     * @param byteSize size in bytes of hex string
     * @return the result
     */
    @FunctionInvocation
    public String hex(final String byteSize) {
        return getHexString(Integer.parseInt(byteSize));
    }

    private String getHexString(final int byteSize) {
        byte[]  resBuf = new byte[byteSize];
        random.nextBytes(resBuf);
        StringBuilder sb = new StringBuilder();
        for (byte b : resBuf) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
