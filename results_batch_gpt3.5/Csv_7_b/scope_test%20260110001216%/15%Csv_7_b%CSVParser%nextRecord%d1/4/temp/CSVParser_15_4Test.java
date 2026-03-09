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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_15_4Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;
    private List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Create a real Token instance
        token = new Token();

        // Create parser instance with dummy CSVFormat and Reader
        parser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Use reflection to inject the lexer mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Inject the reusableToken field with our token instance
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        // Inject the record list
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = new ArrayList<>();
        recordField.set(parser, recordList);

        // Inject headerMap as empty map for constructor
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());

        // Reset recordNumber to 0
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Spy on parser to allow addRecordValue calls (private method)
        parser = spy(parser);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeTOKEN_callsAddRecordValue() throws Exception {
        // Setup token type sequence: TOKEN then EORECORD to end
        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            contentField.set(token, new StringBuilder("value1"));
            Field typeField = Token.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(token, Token.Type.TOKEN);
            Field isReadyField = Token.class.getDeclaredField("isReady");
            isReadyField.setAccessible(true);
            isReadyField.setBoolean(token, true);
            return null;
        }).doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            contentField.set(token, new StringBuilder("value2"));
            Field typeField = Token.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(token, Token.Type.EORECORD);
            Field isReadyField = Token.class.getDeclaredField("isReady");
            isReadyField.setAccessible(true);
            isReadyField.setBoolean(token, true);
            return null;
        }).when(lexerMock).nextToken(token);

        // Mock addRecordValue to add token content to record list
        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            StringBuilder sb = (StringBuilder) contentField.get(token);
            recordList.add(sb.toString());
            return null;
        }).when(parser).addRecordValue();

        CSVRecord result = parser.nextRecord();

        assertNotNull(result);
        assertArrayEquals(new String[] {"value1", "value2"}, result.values());
        assertEquals(1L, result.getRecordNumber());
        assertNull(result.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeEORECORD_callsAddRecordValue() throws Exception {
        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            contentField.set(token, new StringBuilder("val"));
            Field typeField = Token.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(token, Token.Type.EORECORD);
            Field isReadyField = Token.class.getDeclaredField("isReady");
            isReadyField.setAccessible(true);
            isReadyField.setBoolean(token, true);
            return null;
        }).when(lexerMock).nextToken(token);

        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            StringBuilder sb = (StringBuilder) contentField.get(token);
            recordList.add(sb.toString());
            return null;
        }).when(parser).addRecordValue();

        CSVRecord result = parser.nextRecord();

        assertNotNull(result);
        assertArrayEquals(new String[] {"val"}, result.values());
        assertEquals(1L, result.getRecordNumber());
        assertNull(result.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeEOF_isReadyTrue_callsAddRecordValue() throws Exception {
        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            contentField.set(token, new StringBuilder("end"));
            Field typeField = Token.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(token, Token.Type.EOF);
            Field isReadyField = Token.class.getDeclaredField("isReady");
            isReadyField.setAccessible(true);
            isReadyField.setBoolean(token, true);
            return null;
        }).when(lexerMock).nextToken(token);

        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            StringBuilder sb = (StringBuilder) contentField.get(token);
            recordList.add(sb.toString());
            return null;
        }).when(parser).addRecordValue();

        CSVRecord result = parser.nextRecord();

        assertNotNull(result);
        assertArrayEquals(new String[] {"end"}, result.values());
        assertEquals(1L, result.getRecordNumber());
        assertNull(result.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeEOF_isReadyFalse_noAddRecordValue() throws Exception {
        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            contentField.set(token, new StringBuilder("end"));
            Field typeField = Token.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(token, Token.Type.EOF);
            Field isReadyField = Token.class.getDeclaredField("isReady");
            isReadyField.setAccessible(true);
            isReadyField.setBoolean(token, false);
            return null;
        }).when(lexerMock).nextToken(token);

        CSVRecord result = parser.nextRecord();

        assertNull(result);
        verify(parser, never()).addRecordValue();
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeINVALID_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            Field typeField = Token.class.getDeclaredField("type");
            typeField.setAccessible(true);
            typeField.set(token, Token.Type.INVALID);
            return null;
        }).when(lexerMock).nextToken(token);

        IOException thrown = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenTypeCOMMENT_accumulatesComment() throws Exception {
        // Setup token sequence: COMMENT, COMMENT, EORECORD
        doAnswer(new org.mockito.stubbing.Answer<Void>() {
            private int count = 0;
            @Override
            public Void answer(org.mockito.invocation.InvocationOnMock invocation) throws Exception {
                Field contentField = Token.class.getDeclaredField("content");
                contentField.setAccessible(true);
                Field typeField = Token.class.getDeclaredField("type");
                typeField.setAccessible(true);
                Field isReadyField = Token.class.getDeclaredField("isReady");
                isReadyField.setAccessible(true);
                if (count == 0) {
                    contentField.set(token, new StringBuilder("comment1"));
                    typeField.set(token, Token.Type.COMMENT);
                    isReadyField.setBoolean(token, true);
                } else if (count == 1) {
                    contentField.set(token, new StringBuilder("comment2"));
                    typeField.set(token, Token.Type.COMMENT);
                    isReadyField.setBoolean(token, true);
                } else {
                    contentField.set(token, new StringBuilder("value"));
                    typeField.set(token, Token.Type.EORECORD);
                    isReadyField.setBoolean(token, true);
                }
                count++;
                return null;
            }
        }).when(lexerMock).nextToken(token);

        doAnswer(invocation -> {
            Field contentField = Token.class.getDeclaredField("content");
            contentField.setAccessible(true);
            StringBuilder sb = (StringBuilder) contentField.get(token);
            recordList.add(sb.toString());
            return null;
        }).when(parser).addRecordValue();

        CSVRecord result = parser.nextRecord();

        assertNotNull(result);
        assertArrayEquals(new String[] {"value"}, result.values());
        assertEquals(1L, result.getRecordNumber());
        assertEquals("comment1\ncomment2", result.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_unexpectedToken_throwsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            Field typeField = Token.class.getDeclaredField("type");
            typeField.setAccessible(true);
            // Set to null to simulate unexpected token type
            typeField.set(token, null);
            return null;
        }).when(lexerMock).nextToken(token);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().startsWith("Unexpected Token type"));
    }
}