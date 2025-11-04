package com.github.vincentrussell.json.datagenerator;//package com.github.vincentrussell.json.datagenerator;

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
import com.google.gson.JsonObject;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static com.github.vincentrussell.json.datagenerator.CLIMain.ENTER_JSON_TEXT;
import static org.junit.jupiter.api.Assertions.*;

public class CLIMainTest {

    private File sourceFile;
    private File destinationFile;

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    // Temp directory provided by JUnit
    @BeforeEach
    public void setUp(@TempDir Path tempDir) throws IOException {
        sourceFile = tempDir.resolve("source.json").toFile();
        destinationFile = tempDir.resolve("dest.json").toFile();
        outContent = new ByteArrayOutputStream();
        // Use String-based charset for Java 8 compatibility
        try {
            System.setOut(new PrintStream(outContent, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // fallback to default encoding
            System.setOut(new PrintStream(outContent, true));
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    // Helper to provide system input
    private void provideInput(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        System.setIn(new ByteArrayInputStream(bytes));
    }


    @Test
    public void missingArgumentsThrowsExceptionAndPrintsHelp() {
        Exception ex = assertThrows(ParseException.class, () -> CLIMain.main(new String[0]));
        assertTrue(ex.getMessage().contains("Missing required option"));
    }

    @Test
    public void interactiveMode() throws Exception {
        String name = "A green door";
        String input = "{\n" +
                "    \"id\": \"{{uuid()}}\",\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"age\": {{integer(1,50)}},\n" +
                "    \"price\": 12.50,\n" +
                "    \"tags\": [\"home\", \"green\"]\n" +
                "}";
        provideInput(input + System.lineSeparator());

        // call run() which returns an exit code instead of calling System.exit
        int rc = CLIMain.run(new String[]{"-i"});
        assertEquals(0, rc);
        // give main a moment if it spawns threads (original test had sleep)
        Thread.sleep(200);
        String log = outContent.toString(StandardCharsets.UTF_8.name());
        assertTrue(log.startsWith(ENTER_JSON_TEXT));
        String result = log.replaceFirst(ENTER_JSON_TEXT, "");
        JsonObject obj = com.google.gson.JsonParser.parseString(result).getAsJsonObject();
        assertEquals(name, obj.get("name").getAsString());
    }

    @Test
    public void pipeMode() throws Exception {
        String name = "A green door";
        String input = "{\n" +
                "    \"id\": \"{{uuid()}}\",\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"age\": {{integer(1,50)}},\n" +
                "    \"price\": 12.50,\n" +
                "    \"tags\": [\"home\", \"green\"]\n" +
                "}";
        provideInput(input + System.lineSeparator());

        int rc = CLIMain.run(new String[]{"-p"});
        assertEquals(0, rc);
        Thread.sleep(200);
        String result = outContent.toString(StandardCharsets.UTF_8.name());
        JsonObject obj = com.google.gson.JsonParser.parseString(result).getAsJsonObject();
        assertEquals(name, obj.get("name").getAsString());
    }

    @Test
    public void sourceFileNotFound() {
        // sourceFile does not exist
        // ensure destination file exists so behaviour matches previous test (it expected FileNotFoundException)
        assertThrows(FileNotFoundException.class, () -> CLIMain.run(new String[]{"-s", sourceFile.getAbsolutePath(), "-d", destinationFile.getAbsolutePath()}));
    }

    @Test
    public void destinationExists() throws Exception {
        // create both files so destination exists and triggers IOException in main
        try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
            IOUtils.write("{}", fos, StandardCharsets.UTF_8);
        }
        try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            IOUtils.write("{}", fos, StandardCharsets.UTF_8);
        }
        assertThrows(IOException.class, () -> CLIMain.run(new String[]{"-s", sourceFile.getAbsolutePath(), "-d", destinationFile.getAbsolutePath()}));
    }

    @Test
    public void successfulRun() throws Exception {
        // write source file
        try (FileOutputStream fileOutputStream = new FileOutputStream(sourceFile)) {
            IOUtils.write("{\n" +
                    "    \"id\": \"{{uuid()}}\",\n" +
                    "    \"name\": \"A green door\",\n" +
                    "    \"age\": {{integer(1,50)}},\n" +
                    "    \"price\": 12.50,\n" +
                    "    \"tags\": [\"home\", \"green\"]\n" +
                    "}", fileOutputStream, StandardCharsets.UTF_8);
        }
        CLIMain.main(new String[]{"-s", sourceFile.getAbsolutePath(), "-d", destinationFile.getAbsolutePath()});
        assertTrue(destinationFile.exists());
        try (FileInputStream fileInputStream = new FileInputStream(destinationFile)) {
            List<?> list = IOUtils.readLines(fileInputStream, StandardCharsets.UTF_8);
            assertEquals(7, list.size());
        }
    }

    @Test
    public void successfulRunSystemOut() throws Exception {
        try (FileOutputStream fileOutputStream = new FileOutputStream(sourceFile)) {
            String name = "A green door";
            IOUtils.write("{\n" +
                    "    \"id\": \"{{uuid()}}\",\n" +
                    "    \"name\": \"" + name + "\",\n" +
                    "    \"age\": {{integer(1,50)}},\n" +
                    "    \"price\": 12.50,\n" +
                    "    \"tags\": [\"home\", \"green\"]\n" +
                    "}", fileOutputStream, StandardCharsets.UTF_8);
        }
        int rc = CLIMain.run(new String[]{"-s", sourceFile.getAbsolutePath()});
        assertEquals(0, rc);
        JsonObject obj = com.google.gson.JsonParser.parseString(outContent.toString(StandardCharsets.UTF_8)).getAsJsonObject();
        assertEquals("A green door", obj.get("name").getAsString());
    }

    @Test
    public void registerAdditionalFunction() throws Exception {
        try (FileOutputStream fileOutputStream = new FileOutputStream(sourceFile)) {
            IOUtils.write("{\n" +
                    "    \"test-function\": \"{{test-function()}},\"\n" +
                    "    \"test-function2\": \"{{test-function2()}}\"\n" +
                    "}", fileOutputStream, StandardCharsets.UTF_8);
        }
        int rc = CLIMain.run(new String[]{"-s", sourceFile.getAbsolutePath(), "-d", destinationFile.getAbsolutePath(),
                "-f", TestFunctionClazzWithNoArgsMethod.class.getName(), TestFunctionClazzWithNoArgsMethod2.class.getName()});
        assertEquals(0, rc);
        assertTrue(destinationFile.exists());
        String result = FileUtils.readFileToString(destinationFile, StandardCharsets.UTF_8);
        assertEquals("{\n" +
                "    \"test-function\": \"ran successfully,\"\n" +
                "    \"test-function2\": \"ran successfully 2\"\n" +
                "}", result);
    }

    @Test
    public void setTimezoneAsGMTPlus() throws Exception {
        timezoneTest("GMT+10:00", new ValidatorTrue() {
            @Override
            public boolean isTrue(JsonObject obj) {
                return obj.get("date").getAsString().endsWith("GMT+10:00");
            }
        });
    }

    @Test
    public void setTimezoneAsCity() throws Exception {
        timezoneTest("Europe/Paris", new ValidatorTrue() {
            @Override
            public boolean isTrue(JsonObject obj) {
                String dateAsString = obj.get("date").getAsString();
                return dateAsString.endsWith("CET") || dateAsString.endsWith("CEST");
            }
        });
    }

    @Test
    public void setTimezoneAsGMT() throws Exception {
        timezoneTest("GMT", new ValidatorTrue() {
            @Override
            public boolean isTrue(JsonObject obj) {
                return obj.get("date").getAsString().endsWith("GMT");
            }
        });
    }

    private void timezoneTest(String timeZone, ValidatorTrue validatorTrue) throws IOException, ParseException, Exception, ClassNotFoundException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(sourceFile)) {
            IOUtils.write("{\n" +
                    "    \"date\": \"{{date()}}\"\n" +
                    "}", fileOutputStream, StandardCharsets.UTF_8);
        }
        int rc = CLIMain.run(new String[]{"-s", sourceFile.getAbsolutePath(), "-d", destinationFile.getAbsolutePath(), "-t", timeZone});
        assertEquals(0, rc);
        assertTrue(destinationFile.exists());
        try (FileInputStream fileInputStream = new FileInputStream(destinationFile)) {
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(fileInputStream, stringWriter, StandardCharsets.UTF_8);
            JsonObject obj = com.google.gson.JsonParser.parseString(stringWriter.toString()).getAsJsonObject();
            assertTrue(validatorTrue.isTrue(obj));
        }
    }

    private static interface ValidatorTrue {
        public boolean isTrue(JsonObject obj);
    }

    @Function(name = "test-function")
    public static class TestFunctionClazzWithNoArgsMethod {

        @FunctionInvocation
        public String invocation() {
            return "ran successfully";
        }


    }

    @Function(name = "test-function2")
    public static class TestFunctionClazzWithNoArgsMethod2 {

        @FunctionInvocation
        public String invocation() {
            return "ran successfully 2";
        }
    }
}
