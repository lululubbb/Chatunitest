package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_14_2Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws Exception {
        formatMock = mock(CSVFormat.class);
        lexerMock = mock(Lexer.class);
        parser = new CSVParser(mock(java.io.Reader.class), formatMock);

        // Inject the mocked lexer and token via reflection
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        token = new Token();
        tokenField.set(parser, token);

        // Clear the record list
        java.lang.reflect.Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Reset recordNumber
        java.lang.reflect.Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEORECORD() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Setup token sequence: TOKEN, TOKEN, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content.setLength(0);
            token.content.append("value1");
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content.setLength(0);
            token.content.append("value2");
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content.setLength(0);
            token.content.append("value3");
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to add token content to record list
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);

        // Override addRecordValue to add token content to record list
        // We do this by spying parser and calling original addRecordValue
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            List<String> record = (List<String>) CSVParser.class.getDeclaredField("record").get(spyParser);
            record.add(token.content.toString());
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's lexer and token
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, token);

        // Call nextRecord
        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, record.values().toArray());

        // recordNumber field should be incremented
        long recordNumber = (long) CSVParser.class.getDeclaredField("recordNumber").get(spyParser);
        assertEquals(1L, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_isReadyTrue() throws IOException {
        // Setup token sequence: TOKEN, EOF with isReady = true
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content.setLength(0);
            token.content.append("value1");
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content.setLength(0);
            token.content.append("value2");
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to add token content to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            List<String> record = (List<String>) CSVParser.class.getDeclaredField("record").get(spyParser);
            record.add(token.content.toString());
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's lexer and token
        try {
            java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
            lexerField.setAccessible(true);
            lexerField.set(spyParser, lexerMock);
            java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
            tokenField.setAccessible(true);
            tokenField.set(spyParser, token);
        } catch (Exception e) {
            fail(e);
        }

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
        assertArrayEquals(new String[] { "value1", "value2" }, record.values().toArray());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_isReadyFalse_returnsNull() throws IOException {
        // Setup token sequence: EOF with isReady = false
        doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content.setLength(0);
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord record = parser.nextRecord();

        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws IOException {
        // Setup token sequence: INVALID token
        doAnswer(invocation -> {
            token.type = Token.Type.INVALID;
            token.content.setLength(0);
            token.content.append("bad");
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        IOException thrown = assertThrows(IOException.class, () -> {
            parser.nextRecord();
        });
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withComments_appendsComments() throws IOException {
        // Setup token sequence: COMMENT, COMMENT, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content.setLength(0);
            token.content.append("comment1");
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content.setLength(0);
            token.content.append("comment2");
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content.setLength(0);
            token.content.append("value1");
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            List<String> record = (List<String>) CSVParser.class.getDeclaredField("record").get(spyParser);
            record.add(token.content.toString());
            return null;
        }).when(spyParser).addRecordValue();

        try {
            java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
            lexerField.setAccessible(true);
            lexerField.set(spyParser, lexerMock);
            java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
            tokenField.setAccessible(true);
            tokenField.set(spyParser, token);
        } catch (Exception e) {
            fail(e);
        }

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
        assertEquals("comment1\ncomment2", record.getComment());
        assertArrayEquals(new String[] { "value1" }, record.values().toArray());
    }
}