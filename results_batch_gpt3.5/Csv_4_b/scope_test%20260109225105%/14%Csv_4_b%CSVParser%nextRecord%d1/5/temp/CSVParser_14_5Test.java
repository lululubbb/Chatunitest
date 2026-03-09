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
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_14_5Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;
    private Map<String, Integer> headerMap;

    @BeforeEach
    void setUp() {
        lexerMock = mock(Lexer.class);
        headerMap = new LinkedHashMap<>();
        headerMap.put("header1", 0);
        headerMap.put("header2", 1);

        // Create parser instance with mocked lexer and headerMap via reflection
        parser = new CSVParser(mock(Reader.class), mock(CSVFormat.class));
        try {
            // Inject lexer mock
            var lexerField = CSVParser.class.getDeclaredField("lexer");
            lexerField.setAccessible(true);
            lexerField.set(parser, lexerMock);

            // Inject headerMap
            var headerMapField = CSVParser.class.getDeclaredField("headerMap");
            headerMapField.setAccessible(true);
            headerMapField.set(parser, headerMap);

            // Inject recordNumber = 0
            var recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
            recordNumberField.setAccessible(true);
            recordNumberField.setLong(parser, 0L);

            // Inject reusableToken
            var reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
            reusableTokenField.setAccessible(true);
            token = new Token();
            reusableTokenField.set(parser, token);

            // Clear record list
            var recordField = CSVParser.class.getDeclaredField("record");
            recordField.setAccessible(true);
            List<String> recordList = (List<String>) recordField.get(parser);
            recordList.clear();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testNextRecord_TokenThenEORECORD() throws Throwable {
        // Arrange tokens sequence: TOKEN, EORECORD (end record)
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "value1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "value2";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to add values to record list
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);

        // We override addRecordValue to add token content to record list
        // We simulate addRecordValue by adding token.content to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            var recordField = CSVParser.class.getDeclaredField("record");
            recordField.setAccessible(true);
            List<String> recordList = (List<String>) recordField.get(spyParser);
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's fields properly
        var lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        var headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, headerMap);
        var recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);
        var reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(spyParser, token);
        var recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        ((List<String>) recordField.get(spyParser)).clear();

        // Act
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Assert
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals(1L, spyParser.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_CommentToken() throws Throwable {
        // Arrange tokens sequence: COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment line 1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "value1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "value2";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to add values to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            var recordField = CSVParser.class.getDeclaredField("record");
            recordField.setAccessible(true);
            List<String> recordList = (List<String>) recordField.get(spyParser);
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's fields properly
        var lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        var headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, headerMap);
        var recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);
        var reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(spyParser, token);
        var recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        ((List<String>) recordField.get(spyParser)).clear();

        // Act
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Assert
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals(1L, spyParser.getRecordNumber());
        assertEquals("comment line 1", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_CommentMultipleLines() throws Throwable {
        // Arrange tokens sequence: COMMENT, COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment line 1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment line 2";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "value1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "value2";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to add values to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            var recordField = CSVParser.class.getDeclaredField("record");
            recordField.setAccessible(true);
            List<String> recordList = (List<String>) recordField.get(spyParser);
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's fields properly
        var lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        var headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, headerMap);
        var recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);
        var reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(spyParser, token);
        var recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        ((List<String>) recordField.get(spyParser)).clear();

        // Act
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Assert
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals(1L, spyParser.getRecordNumber());
        assertEquals("comment line 1\ncomment line 2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_EOFWithIsReadyTrue() throws Throwable {
        // Arrange tokens sequence: TOKEN, EOF with isReady true
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "value1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content = "value2";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to add values to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            var recordField = CSVParser.class.getDeclaredField("record");
            recordField.setAccessible(true);
            List<String> recordList = (List<String>) recordField.get(spyParser);
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's fields properly
        var lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        var headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, headerMap);
        var recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);
        var reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(spyParser, token);
        var recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        ((List<String>) recordField.get(spyParser)).clear();

        // Act
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Assert
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals(1L, spyParser.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_ThrowsIOExceptionOnInvalid() throws Throwable {
        // Arrange tokens sequence: INVALID token
        doAnswer(invocation -> {
            token.type = Token.Type.INVALID;
            token.content = "invalid";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Inject parser fields properly
        var lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
        var reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(parser, token);

        // Mock getCurrentLineNumber to return a line number
        CSVParser spyParser = spy(parser);
        doReturn(42L).when(spyParser).getCurrentLineNumber();

        // Act & Assert
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        IOException thrown = assertThrows(IOException.class, () -> {
            try {
                nextRecordMethod.invoke(spyParser);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
        assertTrue(thrown.getMessage().contains("line 42"));
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_ReturnsNullWhenRecordEmpty() throws Throwable {
        // Arrange tokens sequence: EORECORD without adding any record value (record list empty)
        doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to do nothing (record list remains empty)
        CSVParser spyParser = spy(parser);
        doNothing().when(spyParser).addRecordValue();

        // Inject spyParser's fields properly
        var lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        var headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, headerMap);
        var recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);
        var reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(spyParser, token);
        var recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        ((List<String>) recordField.get(spyParser)).clear();

        // Act
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Assert
        assertNull(record);
        assertEquals(0L, spyParser.getRecordNumber());
    }
}