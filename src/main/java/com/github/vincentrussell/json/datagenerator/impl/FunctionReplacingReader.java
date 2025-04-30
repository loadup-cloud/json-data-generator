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

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.nio.CharBuffer;

import com.github.vincentrussell.json.datagenerator.TokenResolver;

/**
 * {@link Reader} capable of finding functions in the reader and running them
 */
public class FunctionReplacingReader extends Reader {

    private static final int DEFAULT_PUSHBACK_BUFFER_SIZE = 200;
    private PushbackReader pushbackReader = null;
    private TokenResolver tokenResolver = null;
    private StringBuilder tokenNameBuffer = new StringBuilder();
    private String tokenValue = null;
    private int tokenValueIndex = 0;

    /**
     * Reader that can find the functions from another reader and replace them
     * @param source the source reader
     * @param resolver the token resolver
     */
    public FunctionReplacingReader(final Reader source, final TokenResolver resolver) {
        pushbackReader = new PushbackReader(source, DEFAULT_PUSHBACK_BUFFER_SIZE);
        tokenResolver = resolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(final CharBuffer target) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        if (this.tokenValue != null) {
            if (this.tokenValueIndex < this.tokenValue.length()) {
                return this.tokenValue.charAt(this.tokenValueIndex++);
            }
            if (this.tokenValueIndex == this.tokenValue.length()) {
                this.tokenValue = null;
                this.tokenValueIndex = 0;
            }
        }

        int data = this.pushbackReader.read();

        if (data != '{') {
            return data;
        }

        data = this.pushbackReader.read();
        if (data != '{') {
            this.pushbackReader.unread(data);
            return '{';
        }

        this.tokenNameBuffer.delete(0, this.tokenNameBuffer.length());

        data = this.pushbackReader.read();
        while (data != -1 && data != '}') {

            if (data == '\\') {
                data = this.pushbackReader.read();
                if (data != '}' && data != '{') {
                    this.tokenNameBuffer.append('\\');
                }
            }


            this.tokenNameBuffer.append((char) data);
            data = this.pushbackReader.read();
        }

        data = this.pushbackReader.read();

        //not a valid function no second '}'
        if (data != -1 && data != '}') {
            this.pushbackReader.unread(data);
            this.pushbackReader.unread('}');

            char[] chars = tokenNameBuffer.toString().toCharArray();

            for (int i = chars.length - 1; i >= 0; i--) {
                this.pushbackReader.unread(chars[i]);
            }

            this.pushbackReader.unread('{');
            this.tokenNameBuffer.delete(0, this.tokenNameBuffer.length());
            return '{';
        }

        try {
            this.tokenValue = this.tokenResolver
                    .resolveToken(this.tokenNameBuffer.toString());
        } catch (IllegalArgumentException e) {
            this.tokenValue = null;
        }

        if (this.tokenValue == null) {
            this.tokenValue = "{{" + this.tokenNameBuffer.toString() + "}}";
        }

        if (this.tokenValue.length() == 0) {
            return read();
        }
        return this.tokenValue.charAt(this.tokenValueIndex++);


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(final char[] cbuf) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        this.pushbackReader.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(final long n) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean ready() throws IOException {
        return this.pushbackReader.ready();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mark(final int readAheadLimit) throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() throws IOException {
        throw new RuntimeException("Operation Not Supported");
    }

}
