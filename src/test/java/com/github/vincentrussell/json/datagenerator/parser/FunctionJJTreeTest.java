package com.github.vincentrussell.json.datagenerator.parser;

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
import com.google.common.base.Charsets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class FunctionJJTreeTest {

    static FunctionRegistry functionRegistry;

    @BeforeAll
    public static void beforeClass() {
        functionRegistry = new FunctionRegistry();
        functionRegistry.registerClass(TestConcat.class);
        functionRegistry.registerClass(TestGender.class);
        functionRegistry.registerClass(TestRandomFloat.class);
        functionRegistry.registerClass(TestRandomInteger.class);
    }


    @Test
    public void testFunctionWithTwoStringsArgs() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-concat(\"a\",\"b\")".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        assertEquals("ab", result);
    }

    @Test
    public void testFunctionWithTwoNumbersArgs() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-concat(1,23)".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        assertEquals("123", result);
    }

    @Test
    public void testFunctionWithOneStringOneNumberArgs() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-concat(\"a\",23)".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        assertEquals("a23", result);
    }

    @Test
    public void testFunctionWithOneStringOneFloatArgs() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-concat(\"a\",21231.342342)".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        assertEquals("a21231.342342", result);
    }

    @Test
    public void testFunctionWithNoArgs() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-gender()".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        assertTrue("male".equals(result) || "female".equals(result));
    }

    @Test
    public void testFunctionWithArgsNested() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-concat(\"a \",test-gender())".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        assertTrue("a male".equals(result) || "a female".equals(result));
    }

    @Test
    public void testFunctionWithArgsMultipleNested() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-concat(test-concat(\"a \",test-gender()),\" is cool\")".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        assertTrue("a male is cool".equals(result) || "a female is cool".equals(result));
    }

    @Test
    public void canParseFloatingPointNumbers() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-floating(1.232,2.543)".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        Float flo = Float.valueOf(result);
        assertTrue(flo >= 1.232 && flo <= 2.543);
    }

    @Test
    public void canParseNegativeFloatingPointNumbers() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-floating(-3.543,-2.543)".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        Float flo = Float.valueOf(result);
        assertTrue(flo >= -3.543 && flo <= -2.543);
    }

    @Test
    public void canParseNegativeIntegers() throws ParseException, InvocationTargetException, IllegalAccessException {
        FunctionParser functionParser = new FunctionParser(new ByteArrayInputStream("test-integer(-20,-10)".getBytes()), Charsets.UTF_8);
        functionParser.setFunctionRegistry(functionRegistry);
        String result = functionParser.Parse();
        Integer integer = Integer.valueOf(result);
        assertTrue(integer >= -20 && integer <= -10);
    }

    @Function(name = "test-concat")
    public static class TestConcat {
        @FunctionInvocation
        public String concat(String... objects) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object object : objects) {
                stringBuilder.append(object.toString());
            }
            return stringBuilder.toString();
        }
    }

    @Function(name = "test-gender")
    public static class TestGender {

        @FunctionInvocation
        public String gender() {
            return (Math.random() < 0.5) ? "male" : "female";
        }
    }

    @Function(name = {"test-float", "test-floating"})
    public static class TestRandomFloat {

        private static final Random RANDOM = new Random();

        private String getRandomFloat(Float min, Float max, String format) {
            float randomNumber = min + (max - min) * RANDOM.nextFloat();

            if (format != null) {
                return String.format(format, randomNumber);
            }
            return Float.toString(randomNumber);
        }

        @FunctionInvocation
        public String getRandomFloat(String min, String max) {
            return getRandomFloat(min, max, null);
        }

        @FunctionInvocation
        public String getRandomFloat(String min, String max, String format) {
            return getRandomFloat(Float.parseFloat(min), Float.parseFloat(max), format);
        }

    }

    @Function(name = "test-integer")
    public static class TestRandomInteger {

        private static final Random RANDOM = new Random();

        @FunctionInvocation
        public String getRandomInteger(String min, String max) {
            return getRandomInteger(Integer.parseInt(min), Integer.parseInt(max));
        }

        private String getRandomInteger(Integer min, Integer max) {
            int randomNumber = RANDOM.nextInt(max - min) + min;
            return Integer.toString(randomNumber);
        }
    }


}
