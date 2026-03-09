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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_19_1Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;

    @BeforeEach
    void setUp() throws Exception {
        // Mock dependencies
        lexerMock = mock(Lexer.class);

        // Create a real Token instance
        reusableToken = new Token();

        // Create CSVFormat and headerMap mocks or real instances as needed
        CSVFormat format = mock(CSVFormat.class);
        Map<String, Integer> headerMap = Map.of();

        // Actually instantiate CSVParser using constructor
        parser = new CSVParser(new java.io.StringReader(""), format, 0L, 0L);

        // Inject mocks and initial state via reflection
        setField(parser, "lexer", lexerMock);
        setField(parser, "headerMap", headerMap);
        setField(parser, "recordNumber", 0L);
        setField(parser, "characterOffset", 0L);
        setField(parser, "reusableToken", reusableToken);
        setField(parser, "recordList", new java.util.ArrayList<String>());
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_TokenThenEORECORD() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.isReady = true;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value1");
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.isReady = true;
            reusableToken.content.setLength(0);
            reusableToken.content.append("value2");
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
        List<String> values = List.of(record.get(0), record.get(1));
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_OnlyEOFWithIsReady() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.isReady = true;
            reusableToken.content.setLength(0);
            reusableToken.content.append("valueEOF");
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
        assertEquals("valueEOF", record.get(0));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_EmptyRecordListReturnsNull() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.isReady = false;
            reusableToken.content.setLength(0);
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_CommentTokenAppendsToComment() throws Exception {
        doAnswer(new org.mockito.stubbing.Answer<Void>() {
            int count = 0;

            @Override
            public Void answer(org.mockito.invocation.InvocationOnMock invocation) {
                if (count == 0) {
                    reusableToken.type = Token.Type.COMMENT;
                    reusableToken.isReady = true;
                    reusableToken.content.setLength(0);
                    reusableToken.content.append("comment1");
                } else if (count == 1) {
                    reusableToken.type = Token.Type.COMMENT;
                    reusableToken.isReady = true;
                    reusableToken.content.setLength(0);
                    reusableToken.content.append("comment2");
                } else {
                    reusableToken.type = Token.Type.EORECORD;
                    reusableToken.isReady = true;
                    reusableToken.content.setLength(0);
                    reusableToken.content.append("value");
                }
                count++;
                return null;
            }
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertEquals(1, record.getRecordNumber());
        assertEquals("value", record.get(0));
        assertEquals("comment1\ncomment2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_InvalidTokenThrowsIOException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.INVALID;
            reusableToken.isReady = true;
            reusableToken.content.setLength(0);
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        IOException exception = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(exception.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_UnexpectedTokenThrowsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF; // will be changed to an unexpected type
            reusableToken.isReady = true;
            reusableToken.content.setLength(0);
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // First call to nextRecord should not throw
        try {
            parser.nextRecord();
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            fail("Unexpected IllegalStateException");
        }

        // forcibly set token type to an unexpected type and call nextRecord again
        reusableToken.type = null;
        reusableToken.isReady = true;
        reusableToken.content.setLength(0);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("Unexpected Token type"));
    }
}