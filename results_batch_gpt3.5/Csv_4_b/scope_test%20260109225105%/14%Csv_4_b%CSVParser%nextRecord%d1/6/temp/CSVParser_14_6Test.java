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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CSVParser_14_6Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Lexer
        lexerMock = mock(Lexer.class);
        // Create parser with mocked lexer and dummy format/headerMap via reflection
        parser = (CSVParser) java.lang.reflect.Proxy.newProxyInstance(
                CSVParser.class.getClassLoader(),
                new Class<?>[] { CSVParser.class },
                (proxy, method, args) -> null);

        // Use reflection to create a real instance of CSVParser with Reader and CSVFormat
        // Since constructor is public CSVParser(Reader, CSVFormat)
        // We create dummy Reader and CSVFormat
        java.io.Reader dummyReader = new java.io.StringReader("");
        CSVFormat dummyFormat = mock(CSVFormat.class);
        parser = new CSVParser(dummyReader, dummyFormat);

        // Set lexer field to mocked lexer
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Set headerMap field to empty map
        java.lang.reflect.Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());

        // Clear record list
        java.lang.reflect.Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = new ArrayList<>();
        recordField.set(parser, recordList);

        // Set recordNumber to 0
        java.lang.reflect.Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Set reusableToken field to new Token instance
        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        token = new Token();
        tokenField.set(parser, token);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeTokenAndEORECORD() throws Exception {
        // Setup token sequence: TOKEN -> TOKEN -> EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val1";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val2";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "val3";
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy addRecordValue to add token content to record list
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);

        // We override addRecordValue to add token.content to record list
        // Use reflection to replace addRecordValue with a lambda is not possible,
        // so we invoke it normally after setting token content.
        // So we call nextRecord and rely on real addRecordValue implementation.

        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        assertEquals(3, record.size());
        assertArrayEquals(new String[] {"val1", "val2", "val3"}, record.values().toArray(new String[0]));
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeEOFWithIsReadyTrue() throws Exception {
        // Setup token sequence: TOKEN -> EOF(isReady true)
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val1";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content = "val2";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        assertEquals(2, record.size());
        assertArrayEquals(new String[] {"val1", "val2"}, record.values().toArray(new String[0]));
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeEOFWithIsReadyFalse() throws Exception {
        // Setup token sequence: TOKEN -> EOF(isReady false)
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val1";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content = "val2";
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        assertEquals(1, record.size());
        assertArrayEquals(new String[] {"val1"}, record.values().toArray(new String[0]));
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeComment() throws Exception {
        // Setup token sequence: COMMENT -> TOKEN -> EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment line 1";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val1";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "val2";
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        assertEquals(2, record.size());
        assertEquals("comment line 1", record.getComment());
        assertArrayEquals(new String[] {"val1", "val2"}, record.values().toArray(new String[0]));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeCommentMultipleLines() throws Exception {
        // Setup token sequence: COMMENT -> COMMENT -> TOKEN -> EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment line 1";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment line 2";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val1";
            token.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "val2";
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        assertEquals(2, record.size());
        assertEquals("comment line 1\ncomment line 2", record.getComment());
        assertArrayEquals(new String[] {"val1", "val2"}, record.values().toArray(new String[0]));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeInvalidThrowsIOException() throws Exception {
        doAnswer(invocation -> {
            token.type = Token.Type.INVALID;
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        IOException ex = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_emptyRecordReturnsNull() throws Exception {
        // Setup token sequence: EOF with isReady false and empty record
        doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord record = parser.nextRecord();
        assertNull(record);
    }
}