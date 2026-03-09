package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.Token.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserNextRecordTest {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;

    @BeforeEach
    void setUp() throws Exception {
        // Create real instance of CSVParser with null Reader and CSVFormat.DEFAULT
        parser = new CSVParser(null, CSVFormat.DEFAULT);

        // Mock Lexer
        lexerMock = mock(Lexer.class);
        reusableToken = new Token();

        // Inject lexerMock and reusableToken into parser via reflection
        setField(parser, "lexer", lexerMock);
        setField(parser, "reusableToken", reusableToken);
        setField(parser, "recordList", new ArrayList<String>());
        setField(parser, "headerMap", Map.of());
        setField(parser, "recordNumber", 0L);
        setField(parser, "characterOffset", 0L);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEORECORD() throws Exception {
        // Simulate tokens: TOKEN, TOKEN, EORECORD
        doAnswer(invocation -> {
            reusableToken.type = Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value1");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value2");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.EORECORD;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value3");
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1L, parser.getRecordNumber());
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, record.values().toArray(new String[0]));
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentAndToken() throws Exception {
        // Simulate tokens: COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            reusableToken.type = Type.COMMENT;
            reusableToken.content.setLength(0);
            reusableToken.content.append("comment line");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value1");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.EORECORD;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value2");
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1L, parser.getRecordNumber());
        assertArrayEquals(new String[] { "value1", "value2" }, record.values().toArray(new String[0]));
        assertEquals("comment line", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withMultipleComments() throws Exception {
        // Simulate tokens: COMMENT, COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            reusableToken.type = Type.COMMENT;
            reusableToken.content.setLength(0);
            reusableToken.content.append("comment1");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.COMMENT;
            reusableToken.content.setLength(0);
            reusableToken.content.append("comment2");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value1");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.EORECORD;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value2");
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1L, parser.getRecordNumber());
        assertArrayEquals(new String[] { "value1", "value2" }, record.values().toArray(new String[0]));
        assertEquals("comment1\ncomment2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReadyTrue() throws Exception {
        // Simulate tokens: TOKEN, EOF (isReady true)
        doAnswer(invocation -> {
            reusableToken.type = Type.TOKEN;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value1");
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.EOF;
            reusableToken.content.setLength(0);
            reusableToken.content.append("");
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1L, parser.getRecordNumber());
        assertArrayEquals(new String[] { "value1" }, record.values().toArray(new String[0]));
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReadyFalse() throws Exception {
        // Simulate tokens: EOF (isReady false)
        doAnswer(invocation -> {
            reusableToken.type = Type.EOF;
            reusableToken.content.setLength(0);
            reusableToken.content.append("");
            reusableToken.isReady = false;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNull(record);
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        // Simulate tokens: INVALID
        doAnswer(invocation -> {
            reusableToken.type = Type.INVALID;
            reusableToken.content.setLength(0);
            reusableToken.content.append("");
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        IOException ex = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withUnexpectedToken_throwsIllegalStateException() throws Exception {
        // Simulate tokens: a token with type not handled (e.g. null)
        doAnswer(invocation -> {
            reusableToken.type = null;
            reusableToken.content.setLength(0);
            reusableToken.content.append("");
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().startsWith("Unexpected Token type"));
    }

    // Helper method to set private fields via reflection
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}