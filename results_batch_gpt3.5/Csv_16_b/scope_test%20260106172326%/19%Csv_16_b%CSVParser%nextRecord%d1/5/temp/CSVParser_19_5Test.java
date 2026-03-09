package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_19_5Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token tokenMock;
    private Map<String, Integer> headerMapMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mocks
        lexerMock = mock(Lexer.class);
        tokenMock = new Token();
        headerMapMock = mock(Map.class);

        // Create a CSVParser instance with a dummy Reader and CSVFormat
        Reader dummyReader = mock(Reader.class);
        CSVFormat dummyFormat = mock(CSVFormat.class);

        // Use constructor with reflection to inject mocks
        parser = new CSVParser(dummyReader, dummyFormat, 0L, 0L);
        // Inject mocks into private fields
        setField(parser, "lexer", lexerMock);
        setField(parser, "headerMap", headerMapMock);
        setField(parser, "recordList", new java.util.ArrayList<String>());
        setField(parser, "recordNumber", 0L);
        setField(parser, "characterOffset", 0L);
        setField(parser, "reusableToken", tokenMock);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenToEOF() throws Exception {
        // Setup token sequence: TOKEN, TOKEN, EORECORD
        doAnswer(invocation -> {
            setTokenContent(tokenMock, "value1");
            setField(tokenMock, "type", Token.Type.TOKEN);
            setField(tokenMock, "isReady", true);
            return null;
        }).doAnswer(invocation -> {
            setTokenContent(tokenMock, "value2");
            setField(tokenMock, "type", Token.Type.TOKEN);
            setField(tokenMock, "isReady", true);
            return null;
        }).doAnswer(invocation -> {
            setTokenContent(tokenMock, "value3");
            setField(tokenMock, "type", Token.Type.EORECORD);
            setField(tokenMock, "isReady", true);
            return null;
        }).when(lexerMock).nextToken(tokenMock);

        // Call nextRecord() directly, no spying on private method
        CSVRecord record = parser.nextRecord();

        // Verify record is not null and contains expected values
        assertNotNull(record);
        String[] values = record.values();
        assertEquals(3, values.length);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, values);

        // Verify recordNumber incremented
        assertEquals(1L, parser.getRecordNumber());

        // Verify headerMap passed to CSVRecord constructor
        assertSame(headerMapMock, getField(record, "headerMap"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_commentToken() throws Exception {
        // Setup token sequence: COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            setTokenContent(tokenMock, "comment line");
            setField(tokenMock, "type", Token.Type.COMMENT);
            setField(tokenMock, "isReady", true);
            return null;
        }).doAnswer(invocation -> {
            setTokenContent(tokenMock, "value1");
            setField(tokenMock, "type", Token.Type.TOKEN);
            setField(tokenMock, "isReady", true);
            return null;
        }).doAnswer(invocation -> {
            setTokenContent(tokenMock, "value2");
            setField(tokenMock, "type", Token.Type.EORECORD);
            setField(tokenMock, "isReady", true);
            return null;
        }).when(lexerMock).nextToken(tokenMock);

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("comment line", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_invalidToken_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            setField(tokenMock, "type", Token.Type.INVALID);
            setField(tokenMock, "isReady", true);
            return null;
        }).when(lexerMock).nextToken(tokenMock);

        IOException thrown = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_unexpectedToken_throwsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            setField(tokenMock, "type", null);
            setField(tokenMock, "isReady", true);
            return null;
        }).when(lexerMock).nextToken(tokenMock);

        // Use the existing reusableToken with null type to trigger IllegalStateException
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("Unexpected Token type"));
    }

    // Helper reflection methods
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = null;
        Class<?> clazz = (target instanceof Class<?>) ? (Class<?>) target : target.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        field.setAccessible(true);
        field.set(target, value);
    }

    private static Object getField(Object target, String fieldName) throws Exception {
        Field field = null;
        Class<?> clazz = (target instanceof Class<?>) ? (Class<?>) target : target.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }
        field.setAccessible(true);
        return field.get(target);
    }

    private static void setTokenContent(Token token, String value) throws Exception {
        Field contentField = Token.class.getDeclaredField("content");
        contentField.setAccessible(true);
        // Token.content is StringBuilder, so set new StringBuilder with value
        contentField.set(token, new StringBuilder(value));
    }
}