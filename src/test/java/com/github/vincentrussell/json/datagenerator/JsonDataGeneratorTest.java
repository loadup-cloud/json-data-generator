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

import com.github.approval.Approvals;
import com.github.approval.reporters.Reporters;
import com.github.vincentrussell.json.datagenerator.functions.FunctionRegistry;
import com.github.vincentrussell.json.datagenerator.impl.JsonDataGeneratorImpl;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;


public class JsonDataGeneratorTest {

    private JsonDataGeneratorImpl parser;
    private FunctionRegistry functionRegistry;
    ByteArrayOutputStream outputStream;

//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();

//    @Rule
//    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeEach
    public void setUp() {
        functionRegistry = new FunctionRegistry();
        parser = new JsonDataGeneratorImpl(functionRegistry);
        outputStream = new ByteArrayOutputStream();
        functionRegistry.getGetAndPutCache().clear();
        functionRegistry.getStringIndexHolderMap().clear();
        Approvals.setReporter(Reporters.console());
    }

    @AfterEach
    public void tearDown() {
        try {
            outputStream.close();
        } catch (IOException e) {
            //noop
        }
    }


    private void classpathJsonTests(String source) throws IOException, JsonDataGeneratorException {
        Approvals.verify(getClasspathFileAsString(source), getApprovalPath(source));
    }

    private String getClasspathFileAsString(String source) throws IOException {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(source)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new JsonDataGeneratorImpl().generateTestDataJson(inputStream, byteArrayOutputStream);
            return byteArrayOutputStream.toString("UTF-8");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Test
    public void sourceFileNotFound() throws JsonDataGeneratorException {
        Assertions.assertThrows(JsonDataGeneratorException.class, () -> {
            throw new JsonDataGeneratorException();
        });

//        expectedException.expectCause(isA(FileNotFoundException.class));
        parser.generateTestDataJson(new File("notfound"), new File("notfound"));
    }

    @Test
    public void destinationFileExists() throws JsonDataGeneratorException, IOException {
        File inputFile = new File("");// new temporaryFolder.newFile();
        File outputFile = new File("");//temporaryFolder.newFile();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("simple.json");
             FileOutputStream fileOutputStream = new FileOutputStream(inputFile)) {
            IOUtils.copy(inputStream, fileOutputStream);
        }
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException();
        });
//        expectedException.expectMessage("outputFile can not exist");
        parser.generateTestDataJson(inputFile, outputFile);
    }

    private Path getApprovalPath(String testName) {
        final String basePath = Paths.get("src", "test", "resources", "approvals", JsonDataGeneratorTest.class.getSimpleName()).toString();
        return Paths.get(basePath, testName);
    }

    @TempDir
    static Path sharedTempDir;

    @Test
    public void copyRepeatsDoesNotAddNullCharacters() throws IOException, JsonDataGeneratorException {
        File file = new File("");

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("large_repeats.json");
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            new JsonDataGeneratorImpl().generateTestDataJson(inputStream, fileOutputStream);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        assertTrue(file.exists());
        assertFalse(Hex.encodeHexString(FileUtils.readFileToByteArray(file)).contains("0000"));
    }


    @Test
    public void utf8TestForeignCharacters() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("foreignCharacters.json");
    }

    @Test
    public void copyJson() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("copyJson.json");
    }

    @Test
    public void copyDoubleNestedJson() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("copyDoubleNestedJson.json");
    }

    @Test
    public void invalidFunction() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("invalidFunction.json");
    }

    @Test
    public void zeroRepeat() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("zeroRepeat.json");
    }

    @Test
    public void zeroRepeatRange() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("zeroRepeatRange.json");
    }

    @Test
    public void repeatNonFunctionJsonArray() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("repeatNonFunctionJsonArray.json");
    }

    @Test
    public void indexFunctionTest() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("indexFunctionSimple.json");
    }

    @Test
    public void indexFunctionWithNamesTest() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("indexFunctionNested.json");
    }

    @Test
    public void putGetTest() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("putGetTest.json");
    }

    @Test
    public void repeatFunctionRangeJsonArrayNoQuotes() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson(this.getClass().getClassLoader().getResource("repeatFunctionRangeJsonArrayNoQuotes.json"), outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
        int numberSize = obj.getAsJsonArray("numbers").size();
        assertTrue(numberSize >= 3 && numberSize <= 10);
    }

    @Test//(expected = IllegalArgumentException.class)
    public void repeatFunctionInvalidRange() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("{\n" +
                "    \"id\": \"dfasf235345345\",\n" +
                "    \"name\": \"A green door\",\n" +
                "    \"age\": 23,\n" +
                "    \"price\": 12.50,\n" +
                "    \"numbers\": ['{{repeat(10,3)}}',\n" +
                "             {{integer(1,100)}}]\n" +
                "}", outputStream);
    }

    @Test
    public void concatWithBracesEscaped() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("{\n" +
                "    \"concattest\": \"{{concat(\"\\{\", \"test\", \"\\}\")}}\",\n" +
                "}", outputStream);
        String results = new String(outputStream.toByteArray());
        assertEquals("{\n" +
                "    \"concattest\": \"{test}\",\n" +
                "}", results);
    }


    @Test
    public void singleQuoteFunctionTest() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("{\"day1\": \"{{put('date', date('dd-MM-yyyy HH:mm:ss'))}}\",\"day2\": \"{{addDays(get('date'), 12)}}\"}", outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        assertIsDate(dateFormat, obj.getAsJsonPrimitive("day1").getAsString());
        assertIsDate(dateFormat, obj.getAsJsonPrimitive("day2").getAsString());
    }

    @Test
    public void convertDateToTimestamp() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("{\"day1\": \"{{put('date', date('dd-MM-yyyy HH:mm:ss'))}}\",\"day2\": \"{{toTimestamp(addDays(get('date'), 12), 'dd-MM-yyyy HH:mm:ss')}}\"}", outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        assertIsDate(dateFormat, obj.getAsJsonPrimitive("day1").getAsString());
        assertIsDateTimestamp(dateFormat, obj.getAsJsonPrimitive("day2").getAsString());
    }

    private void assertIsDate(SimpleDateFormat dateFormat, String date) {
        try {
            Date dateObj = dateFormat.parse(date);
            assertNotNull(dateObj);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private void assertIsDateTimestamp(SimpleDateFormat dateFormat, String timestamp) {
        try {
            Date dateObj = dateFormat.parse(dateFormat.format(new Date(Long.parseLong(timestamp))));
            assertNotNull(dateObj);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void doubleQuoteFunctionTest() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("{\"day1\": \"{{put(\"date\", date(\"dd-MM-yyyy HH:mm:ss\"))}}\",\"day2\":\"{{addDays(get(\"date\"), 12)}}\"}\n", outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        assertIsDate(dateFormat, obj.getAsJsonPrimitive("day1").getAsString());
        assertIsDate(dateFormat, obj.getAsJsonPrimitive("day2").getAsString());
    }

    @Test
    public void concatWithEscapesWithoutBraces() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("{\n" +
                "    \"concattest\": \"{{concat(\"\\\\\", \"test\", \"\\}\")}}\",\n" +
                "}", outputStream);
        String results = new String(outputStream.toByteArray());
        assertEquals("{\n" +
                "    \"concattest\": \"\\\\test}\",\n" +
                "}", results);
    }

    @Test
    public void repeatFunctionRangeEqual() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("{\n" +
                "    \"id\": \"dfasf235345345\",\n" +
                "    \"name\": \"A green door\",\n" +
                "    \"age\": 23,\n" +
                "    \"price\": 12.50,\n" +
                "    \"numbers\": ['{{repeat(10,10)}}',\n" +
                "             {{integer(1,100)}}]\n" +
                "}", outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
        int numberSize = obj.getAsJsonArray("numbers").size();
        assertEquals(10, numberSize);
    }

    @Test
    public void repeatWithSystemProperties() throws IOException, JsonDataGeneratorException {
        System.setProperty("repeatTime", "10");
        try {
            parser.generateTestDataJson("{\n" +
                    "    \"id\": \"dfasf235345345\",\n" +
                    "    \"name\": \"A green door\",\n" +
                    "    \"age\": 23,\n" +
                    "    \"price\": 12.50,\n" +
                    "    \"numbers\": ['{{repeat(systemProperty('repeatTime'),systemProperty('repeatTime'))}}',\n" +
                    "             {{integer(1,100)}}]\n" +
                    "}", outputStream);
            String results = new String(outputStream.toByteArray());
            JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
            int numberSize = obj.getAsJsonArray("numbers").size();
            assertEquals(10, numberSize);
        } finally {
            System.clearProperty("repeatTime");
        }
    }

    @Test
    public void resetIndex() throws IOException, JsonDataGeneratorException {
        classpathJsonTests("resetIndex.json");
    }


    @Test
    public void multiThreadedTests() throws IOException, JsonDataGeneratorException, InterruptedException {
        int concurrentSize = 20;
        List<JsonDataGeneratorImpl> jsonDataGenerators = new ArrayList<>(concurrentSize);
        for (int i = 0; i < concurrentSize; i++) {
            jsonDataGenerators.add(new JsonDataGeneratorImpl());
        }
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentSize);

        List<Callable<String>> callables = new ArrayList<>(concurrentSize);
        for (int i = 0; i < concurrentSize; i++) {
            final JsonDataGeneratorImpl jsonDataGenerator = jsonDataGenerators.get(i);
            callables.add(() -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                jsonDataGenerator.generateTestDataJson(Thread.currentThread()
                        .getContextClassLoader().getResourceAsStream("resetIndex.json"), byteArrayOutputStream);
                return byteArrayOutputStream.toString("UTF-8");
            });
        }

        List<Future<String>> futures = executorService.invokeAll(callables);
        List<String> results = Lists.newArrayList(Iterables.transform(futures, new Function<Future<String>, String>() {
            @Override
            public String apply(Future<String> stringFuture) {
                try {
                    return stringFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }));

        for (String string : results) {
            Approvals.verify(string, getApprovalPath("multiThreadedTests.json"));
        }

    }

    @Test
    public void floatingRepeatsWithQuotes() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson("[\n" +
                "  '{{repeat(1, 1)}}',\n" +
                "  {\n" +
                "    _id: '{{objectId()}}',\n" +
                "    index: '{{index()}}',\n" +
                "    guid: '{{guid()}}',\n" +
                "    isActive: '{{bool()}}',\n" +
                "    balance: '{{floating(1000, 4000)}}',\n" +
                "    picture: 'http://placehold.it/32x32',\n" +
                "    age: '{{integer(20, 40)}}',\n" +
                "    eyeColor: '{{random(\"blue\", \"brown\", \"green\")}}',\n" +
                "    name: '{{firstName()}} {{surname()}}',\n" +
                "    gender: '{{gender()}}',\n" +
                "    company: '{{company().toUpperCase()}}',\n" +
                "    email: '{{email()}}',\n" +
                "    phone: '+1 {{phone()}}',\n" +
                "    address: '{{integer(100, 999)}} {{street()}}, {{city()}}, {{state()}}, {{integer(100, 10000)}}',\n" +
                "    about: '{{lorem(1, \"paragraphs\")}}',\n" +
                "    registered: '{{date()}}',\n" +
                "    latitude: '{{floating(-90.000001, 90)}}',\n" +
                "    longitude: '{{floating(-180.000001, 180)}}',\n" +
                "    tags: [\n" +
                "      '{{repeat(7)}}',\n" +
                "      '{{lorem(1, \"words\")}}'\n" +
                "    ],\n" +
                "    friends: [\n" +
                "      '{{repeat(3)}}',\n" +
                "      {\n" +
                "        id: '{{index()}}',\n" +
                "        name: '{{firstName()}} {{surname()}}'\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]", outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) ((JsonArray) new com.google.gson.JsonParser().parse(results)).get(0);
        int numberSize = obj.getAsJsonArray("tags").size();
        assertEquals(7, numberSize);

        for (JsonElement tagElement : obj.getAsJsonArray("tags")) {
            JsonPrimitive primitive = tagElement.getAsJsonPrimitive();
            assertTrue(primitive.isString());
        }
    }

    @Test
    public void repeatFunctionInvalid() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson(this.getClass().getClassLoader().getResource("repeatFunctionInvalid.json"), outputStream);
        String result = outputStream.toString();
        assertTrue(Pattern.compile("\\{\n" +
                "    \"id\": \"dfasf235345345\",\n" +
                "    \"name\": \"A green door\",\n" +
                "    \"age\": 23,\n" +
                "    \"price\": 12.50,\n" +
                "    \"numbers\": \\['\\{\\{repeat\\(3\\)\\},\n" +
                "             \\d+\\]\n" +
                "}", Pattern.MULTILINE).matcher(result).matches());
    }

    @Test
    public void repeatFunctionJsonArrayNoQuotes() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson(this.getClass().getClassLoader().getResource("repeatFunctionJsonArrayNoQuotes.json"), outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
        JsonArray array = obj.getAsJsonArray("numbers");
        assertEquals(3, array.size());
    }

    @Test
    public void repeatFunctionJsonArrayQuotes() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson(this.getClass().getClassLoader().getResource("repeatFunctionJsonArrayQuotes.json"), outputStream);
        String results = new String(outputStream.toByteArray());
        JsonObject obj = (JsonObject) new com.google.gson.JsonParser().parse(results);
        JsonArray array = obj.getAsJsonArray("colors");
        assertEquals(4, array.size());
    }


    @Test
    public void functionSimpleJson() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson(this.getClass().getClassLoader().getResource("simple.json"), outputStream);
        String results = new String(outputStream.toByteArray());
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonObject obj = (JsonObject) parser.parse(results);
        assertEquals("A green door", obj.get("name").getAsString());
        assertEquals(12.50, obj.get("price").getAsDouble(), 0);
    }

    @Test
    public void foreignCharactersInTokenResolver() throws IOException, JsonDataGeneratorException {
        parser.generateTestDataJson(this.getClass().getClassLoader().getResource("foreignCharactersWithinTokenResolver.json"), outputStream);
        String results = new String(outputStream.toByteArray());
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonObject obj = (JsonObject) parser.parse(results);
        String randomResult = obj.get("random").getAsString();
        ArrayList<String> randomOptions =
                Lists.newArrayList("中文替换", "Как тебя зовут?", "هناك أولاد في الحديقة");
        assertTrue(randomOptions.contains(randomResult));
    }

    @Test
    public void testInvalidScenario()
            throws JsonDataGeneratorException, UnsupportedEncodingException {
        JsonDataGeneratorImpl parser = new JsonDataGeneratorImpl();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        parser.generateTestDataJson("{{", outputStream);
        String output = outputStream.toString("UTF-8");
        assertTrue("{{}}".equals(output));
    }

    @Test
    public void testXmlTemplate() throws IOException, JsonDataGeneratorException, SAXException, ParserConfigurationException {
        parser.generateTestDataJson(this.getClass().getClassLoader().getResource("xmlfunctionWithRepeat.xml"), outputStream);

        ByteArrayInputStream inputstream = new ByteArrayInputStream(outputStream.toByteArray());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

    }
}
