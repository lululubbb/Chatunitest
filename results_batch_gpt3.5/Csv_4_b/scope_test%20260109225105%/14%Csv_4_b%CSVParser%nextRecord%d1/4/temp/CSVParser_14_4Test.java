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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_14_4Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Create a CSVFormat mock or dummy (can be null if not used in nextRecord)
        CSVFormat format = mock(CSVFormat.class);

        // Instantiate CSVParser with mocked Lexer via constructor and reflection
        parser = new CSVParser(new StringReader(""), format);

        // Use reflection to set private final lexer field to lexerMock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Access reusableToken field
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        reusableToken = (Token) tokenField.get(parser);

        // Reset recordNumber to 0
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Clear record list
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Clear headerMap (can be null or empty)
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEORECORD() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value1");
            reusableToken.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value2");
            reusableToken.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value3");
            reusableToken.isReady = false;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        String[] values = record.values();
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, values);
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReadyTrue() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("val1");
            reusableToken.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.content.setLength(0);
            reusableToken.content.append("val2");
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        String[] values = record.values();
        assertArrayEquals(new String[] { "val1", "val2" }, values);
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReadyFalse_returnsNull() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.content.setLength(0);
            reusableToken.isReady = false;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNull(record);
        assertEquals(0, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentTokens() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content.setLength(0);
            reusableToken.content.append("comment line 1");
            reusableToken.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content.setLength(0);
            reusableToken.content.append("comment line 2");
            reusableToken.isReady = false;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value1");
            reusableToken.isReady = false;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1, parser.getRecordNumber());
        String[] values = record.values();
        assertArrayEquals(new String[] { "value1" }, values);
        assertEquals("comment line 1\ncomment line 2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.INVALID;
            reusableToken.content.setLength(0);
            reusableToken.isReady = false;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IOException thrown = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }
}