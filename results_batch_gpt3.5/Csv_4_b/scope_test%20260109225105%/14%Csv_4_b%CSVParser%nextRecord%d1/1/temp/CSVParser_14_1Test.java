package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_14_1Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        // Mock dependencies
        lexerMock = mock(Lexer.class);

        // Create a real Token instance to be reused by parser
        token = new Token();

        // Create CSVFormat mock or real instance as needed (can be null for this test)
        CSVFormat format = mock(CSVFormat.class);

        // Use real CSVParser instance with mocked Lexer and Token injected later
        parser = new CSVParser(new StringReader(""), format);

        // Inject mocked lexer and reusableToken by reflection
        setField(parser, "lexer", lexerMock);
        setField(parser, "reusableToken", token);

        // Initialize headerMap and recordNumber for test stability
        setField(parser, "headerMap", null);
        setField(parser, "recordNumber", 0L);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEORECORD() throws Exception {
        // Setup token sequence: TOKEN, TOKEN, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            setContent(token, "value1");
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            setContent(token, "value2");
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            setContent(token, "value3");
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = createSpyWithAddRecordValueViaReflection(parser);

        doAnswer(invocation -> {
            spyParser.addRecordValueViaReflection();
            return null;
        }).when(spyParser).addRecordValueViaReflection();

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(3, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
        assertEquals(1, spyParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_andIsReadyTrue() throws Exception {
        doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.isReady = true;
            setContent(token, "endValue");
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = createSpyWithAddRecordValueViaReflection(parser);

        doAnswer(invocation -> {
            spyParser.addRecordValueViaReflection();
            return null;
        }).when(spyParser).addRecordValueViaReflection();

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.size());
        assertEquals("endValue", record.get(0));
        assertEquals(1, spyParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_andIsReadyFalse_returnsNull() throws IOException {
        doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord record = parser.nextRecord();

        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws IOException {
        doAnswer(invocation -> {
            token.type = Token.Type.INVALID;
            return null;
        }).when(lexerMock).nextToken(token);

        IOException exception = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(exception.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withComments() throws Exception {
        // Sequence: COMMENT, COMMENT, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            setContent(token, "comment1");
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            setContent(token, "comment2");
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            setContent(token, "value");
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = createSpyWithAddRecordValueViaReflection(parser);

        doAnswer(invocation -> {
            spyParser.addRecordValueViaReflection();
            return null;
        }).when(spyParser).addRecordValueViaReflection();

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.size());
        assertEquals("value", record.get(0));
        assertEquals("comment1\ncomment2", record.getComment());
    }

    // Utility methods for reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Object getField(Object target, String fieldName) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private void setContent(Token token, String value) throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Token.content is a final StringBuilder, so clear and append new value
        StringBuilder sb = (StringBuilder) contentField.get(token);
        sb.setLength(0);
        sb.append(value);
    }

    private String getContent(Token token) throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        StringBuilder sb = (StringBuilder) contentField.get(token);
        return sb.toString();
    }

    // Invoke private addRecordValue() method via reflection on CSVParser instance
    private void invokeAddRecordValue(CSVParser parser) throws Exception {
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);
    }

    // Helper method to create a spy with addRecordValueViaReflection method
    private CSVParser createSpyWithAddRecordValueViaReflection(CSVParser original) {
        return spy(new CSVParser(original) {
            public void addRecordValueViaReflection() throws Exception {
                invokeAddRecordValue(this);
            }
        });
    }
}