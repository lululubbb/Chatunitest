package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_15_2Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;
    private List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        // Create a dummy CSVFormat for constructor (can be null if not used in nextRecord)
        CSVFormat format = mock(CSVFormat.class);

        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Instantiate CSVParser with a dummy Reader and format
        parser = new CSVParser(new java.io.StringReader(""), format);

        // Use reflection to set private final lexer field to our mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Access and set the reusableToken field
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        token = (Token) tokenField.get(parser);

        // Access the record list field
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = (List<String>) recordField.get(parser);

        // Clear record list before each test
        recordList.clear();

        // Set headerMap field to empty map to avoid NPE
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());

        // Reset recordNumber to 0
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEoRecord_returnsRecord() throws Exception {
        // Setup token type sequence: TOKEN, TOKEN, EORECORD
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

        // Call nextRecord
        CSVRecord record = parser.nextRecord();

        // Assert record is not null and contains expected values
        assertNotNull(record);
        String[] values = record.values();
        assertArrayEquals(new String[] {"value1", "value2", "value3"}, values);
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReady_addsValueAndReturnsRecord() throws Exception {
        // Setup token type sequence: TOKEN, EOF with isReady true
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content.setLength(0);
            token.content.append("val1");
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content.setLength(0);
            token.content.append("val2");
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Call nextRecord
        CSVRecord record = parser.nextRecord();

        // Assert record values include both tokens
        assertNotNull(record);
        String[] values = record.values();
        assertArrayEquals(new String[] {"val1", "val2"}, values);
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentTokens_accumulatesCommentAndReturnsRecord() throws Exception {
        // Setup token type sequence: COMMENT, COMMENT, EORECORD
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

        // Call nextRecord
        CSVRecord record = parser.nextRecord();

        // Assert record values and comment string with line feed separator
        assertNotNull(record);
        String[] values = record.values();
        assertArrayEquals(new String[] {"value1"}, values);
        assertEquals(1, record.getRecordNumber());
        assertEquals("comment1\ncomment2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        // Setup token type: INVALID
        doAnswer(invocation -> {
            token.type = Token.Type.INVALID;
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        IOException ex = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withUnexpectedTokenType_throwsIllegalStateException() throws Exception {
        // Setup token type: null (simulate unexpected)
        doAnswer(invocation -> {
            token.type = null;
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("Unexpected Token type"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_returnsNullWhenRecordEmpty() throws Exception {
        // Setup token type: EORECORD but no values added
        doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content.setLength(0);
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // record list is empty, so nextRecord should return null
        CSVRecord record = parser.nextRecord();
        assertNull(record);
    }
}