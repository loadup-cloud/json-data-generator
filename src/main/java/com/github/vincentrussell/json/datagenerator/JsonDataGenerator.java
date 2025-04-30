package com.github.vincentrussell.json.datagenerator;

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


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * interface for class that streams json generation to OutputStream
 */
public interface JsonDataGenerator {

    /**
     * Generate json test data
     *
     * @param text         source json text
     * @param outputStream stream to write the test data to.
     *                     You are responsible for closing your own OutputStream.
     * @throws JsonDataGeneratorException default exception thrown when using the data generator
     */
    void generateTestDataJson(String text, OutputStream outputStream)
        throws JsonDataGeneratorException;

    /**
     * Generate json test data
     *
     * @param classPathResource url of source json text on classpath
     * @param outputStream      stream to write the test data to.
     *                          You are responsible for closing your own OutputStream.
     * @throws JsonDataGeneratorException default exception thrown when using the data generator
     */
    void generateTestDataJson(URL classPathResource, OutputStream outputStream)
        throws JsonDataGeneratorException;

    /**
     * Generate json test data
     *
     * @param file         file of source json text
     * @param outputStream stream to write the test data to.
     *                     You are responsible for closing your own OutputStream.
     * @throws JsonDataGeneratorException default exception thrown when using the data generator
     */
    void generateTestDataJson(File file, OutputStream outputStream)
        throws JsonDataGeneratorException;


    /**
     * Generate json test data
     *
     * @param file         file of source json text
     * @param outputFile file to write the test data to.
     * @throws JsonDataGeneratorException default exception thrown when using the data generator
     */
    void generateTestDataJson(File file, File outputFile)
        throws JsonDataGeneratorException;


    /**
     * Generate json test data
     *
     * @param inputStream  inputstream source json text. You are responsible for closing
     *                     your own InputStream.
     * @param outputStream stream to write the test data to. You are responsible for
     *                     closing your own OutputStream.
     * @throws JsonDataGeneratorException default exception thrown when using the data generator
     */
    void generateTestDataJson(InputStream inputStream, OutputStream outputStream)
        throws JsonDataGeneratorException;
}
