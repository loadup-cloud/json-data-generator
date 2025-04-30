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

/**
 * random ipv6 address
 */
@Function(name = "ipv6")
public class Ipv6 {

    private static final String LOWER = "lower";
    private static final String UPPER = "upper";

    /**
     * random ipv6 address
     * @return the result
     */
    @FunctionInvocation
    public String ipv6() {
        return ipV6(LOWER);
    }

    /**
     * get random ipv6 address
     * @param hexCase "upper" or "lower"
     * @return the result
     */
    @FunctionInvocation
    public String ipV6(final String hexCase) {
        long leftLimit = 1L;
        long rightLimit = Long.MAX_VALUE;
        long generatedLong1 = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        long generatedLong2 = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return longToIP(new long[]{generatedLong1, generatedLong2}, hexCase);
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private static String longToIP(final long[] ip, final String hexCase) {
        StringBuilder ipString = new StringBuilder();
        for (long crtLong : ip) { //for every long: it should be two of them

            for (int i = 0; i < 4; i++) { //we display in total 4 parts for every long
                String hexString = Long.toHexString(crtLong & 0xFFFF);
                ipString.append(UPPER.equals(hexCase)
                    ? hexString.toUpperCase() : hexString).append(":");
                crtLong = crtLong >> 16;
            }
        }
        ipString.setLength(ipString.length() - 1);
        return ipString.toString();

    }

}
