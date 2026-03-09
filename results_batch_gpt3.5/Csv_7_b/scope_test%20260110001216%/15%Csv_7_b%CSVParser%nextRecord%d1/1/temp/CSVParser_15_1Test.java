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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_15_1Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;
    private List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Lexer
        lexerMock = mock(Lexer.class);

        // Create CSVFormat mock or dummy
        CSVFormat format = mock(CSVFormat.class);

        // Create CSVParser instance using constructor with Reader and CSVFormat
        parser = new CSVParser(new java.io.StringReader(""), format);

        // Inject the mocked Lexer into parser using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Access reusableToken field
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        reusableToken = (Token) tokenField.get(parser);

        // Access record field
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = (List<String>) recordField.get(parser);

        // Reset recordNumber field to 0
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Mock headerMap field with empty map
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokenAndEoRecord() throws Exception {
        // Setup reusableToken.type sequence: TOKEN, EORECORD
        // TOKEN triggers addRecordValue()
        // EORECORD triggers addRecordValue() and ends loop
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.isReady = false;
            reusableToken.content = "value1";
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.isReady = false;
            reusableToken.content = "value2";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        // Spy on parser to verify addRecordValue calls
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Replace parser with spyParser in test
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, reusableToken);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());

        // Call nextRecord
        CSVRecord record = spyParser.nextRecord();

        // Verify addRecordValue called twice
        verify(spyParser, times(2)).addRecordValue();

        // Assert record is not null
        assertNotNull(record);

        // Assert record content contains added values
        String[] values = record.values();
        assertArrayEquals(new String[] {"value1", "value2"}, values);

        // Assert record number incremented to 1
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        long recordNumber = recordNumberField.getLong(spyParser);
        assertEquals(1L, recordNumber);

        // Assert comment is null (no comments)
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFReady() throws Exception {
        // Setup reusableToken.type sequence: TOKEN, EOF (isReady=true)
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.isReady = false;
            reusableToken.content = "val1";
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.isReady = true;
            reusableToken.content = "val2";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, reusableToken);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());

        CSVRecord record = spyParser.nextRecord();

        verify(spyParser, times(2)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[] {"val1", "val2"}, record.values());

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        long recordNumber = recordNumberField.getLong(spyParser);
        assertEquals(1L, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFNotReady_returnsNull() throws Exception {
        // Setup reusableToken.type sequence: EOF (isReady=false)
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.isReady = false;
            reusableToken.content = "";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(parser);
        doNothing().when(spyParser).addRecordValue();

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, reusableToken);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());

        CSVRecord record = spyParser.nextRecord();

        verify(spyParser, never()).addRecordValue();

        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withComment() throws Exception {
        // Setup reusableToken.type sequence: COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.isReady = false;
            reusableToken.content = "comment line 1";
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.isReady = false;
            reusableToken.content = "val1";
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.isReady = false;
            reusableToken.content = "val2";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, reusableToken);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());

        CSVRecord record = spyParser.nextRecord();

        verify(spyParser, times(2)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[] {"val1", "val2"}, record.values());

        // Comment should be "comment line 1"
        assertEquals("comment line 1", record.getComment());

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        long recordNumber = recordNumberField.getLong(spyParser);
        assertEquals(1L, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.INVALID;
            reusableToken.isReady = false;
            reusableToken.content = "";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, reusableToken);

        IOException thrown = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withUnexpectedToken_throwsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            // Set a token type not handled by switch (e.g. null or a new enum)
            // Since Token.Type is enum, pick a value not in switch cases
            reusableToken.type = null;
            reusableToken.isReady = false;
            reusableToken.content = "";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, reusableToken);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("Unexpected Token type"));
    }
}