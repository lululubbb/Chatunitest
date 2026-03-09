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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

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

class CSVParser_15_6Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;
    private List<String> recordList;
    private Map<String, Integer> headerMapMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mock dependencies
        lexerMock = mock(Lexer.class);
        headerMapMock = mock(Map.class);

        // Create instance with dummy Reader and CSVFormat (using parse with String for simplicity)
        parser = new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class));

        // Use reflection to set private final fields lexer, headerMap, record, reusableToken, recordNumber
        setField(parser, "lexer", lexerMock);
        setField(parser, "headerMap", headerMapMock);

        recordList = new ArrayList<>();
        setField(parser, "record", recordList);

        reusableToken = new Token();
        setField(parser, "reusableToken", reusableToken);

        setField(parser, "recordNumber", 0L);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEORECORD() throws Exception {
        // Setup token sequence: TOKEN, TOKEN, EORECORD
        // We'll simulate lexer.nextToken filling reusableToken accordingly

        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "value1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "value2";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.content = "value3";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        // Spy on parser to verify addRecordValue() calls
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            // add reusableToken.content to record list
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Replace the parser instance with spy in private fields to keep state
        setField(spyParser, "lexer", lexerMock);
        setField(spyParser, "headerMap", headerMapMock);
        setField(spyParser, "record", recordList);
        setField(spyParser, "reusableToken", reusableToken);
        setField(spyParser, "recordNumber", 0L);

        CSVRecord result = spyParser.nextRecord();

        // Verify addRecordValue called 3 times (2 TOKEN + 1 EORECORD)
        verify(spyParser, times(3)).addRecordValue();

        // record list should contain 3 values
        assertEquals(3, recordList.size());
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, recordList.toArray());

        // recordNumber incremented to 1
        long recordNumber = (long) getField(spyParser, "recordNumber");
        assertEquals(1L, recordNumber);

        // Result should not be null and contain proper values
        assertNotNull(result);
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, result.values());
        assertEquals(recordNumber, result.getRecordNumber());
        assertNull(result.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReadyTrue() throws Exception {
        // Setup token sequence: TOKEN, EOF (isReady true)
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "value1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.content = "value2";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        setField(spyParser, "lexer", lexerMock);
        setField(spyParser, "headerMap", headerMapMock);
        setField(spyParser, "record", recordList);
        setField(spyParser, "reusableToken", reusableToken);
        setField(spyParser, "recordNumber", 0L);

        CSVRecord result = spyParser.nextRecord();

        verify(spyParser, times(2)).addRecordValue();

        assertEquals(2, recordList.size());
        assertArrayEquals(new String[] { "value1", "value2" }, recordList.toArray());

        long recordNumber = (long) getField(spyParser, "recordNumber");
        assertEquals(1L, recordNumber);

        assertNotNull(result);
        assertArrayEquals(new String[] { "value1", "value2" }, result.values());
        assertEquals(recordNumber, result.getRecordNumber());
        assertNull(result.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReadyFalse_returnsNull() throws Exception {
        // Setup token sequence: EOF with isReady false
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.content = "value";
            reusableToken.isReady = false;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(parser);
        // addRecordValue should not be called
        doNothing().when(spyParser).addRecordValue();

        setField(spyParser, "lexer", lexerMock);
        setField(spyParser, "headerMap", headerMapMock);
        setField(spyParser, "record", recordList);
        setField(spyParser, "reusableToken", reusableToken);
        setField(spyParser, "recordNumber", 0L);

        CSVRecord result = spyParser.nextRecord();

        verify(spyParser, never()).addRecordValue();

        assertTrue(recordList.isEmpty());

        assertNull(result);

        long recordNumber = (long) getField(spyParser, "recordNumber");
        assertEquals(0L, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.INVALID;
            reusableToken.content = "invalid";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IOException thrown = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentToken_setsCommentAndContinues() throws Exception {
        // Setup token sequence: COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content = "comment line 1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content = "comment line 2";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "value1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.content = "value2";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        setField(spyParser, "lexer", lexerMock);
        setField(spyParser, "headerMap", headerMapMock);
        setField(spyParser, "record", recordList);
        setField(spyParser, "reusableToken", reusableToken);
        setField(spyParser, "recordNumber", 0L);

        CSVRecord result = spyParser.nextRecord();

        verify(spyParser, times(2)).addRecordValue();

        assertEquals(2, recordList.size());
        assertArrayEquals(new String[] { "value1", "value2" }, recordList.toArray());

        assertNotNull(result);

        assertEquals("comment line 1\ncomment line 2", result.getComment());

        long recordNumber = (long) getField(spyParser, "recordNumber");
        assertEquals(1L, recordNumber);
        assertEquals(recordNumber, result.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_unexpectedTokenType_throwsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.HEADER; // Not handled in switch
            reusableToken.content = "header";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("Unexpected Token type"));
    }

    private Object getField(Object target, String fieldName) throws Exception {
        var field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}