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
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_14_3Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;
    private Method nextRecordMethod;
    private Method addRecordValueMethod;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat formatMock = mock(CSVFormat.class);
        lexerMock = mock(Lexer.class);

        parser = new CSVParser(new java.io.StringReader(""), formatMock);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableToken = (Token) reusableTokenField.get(parser);

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);

        nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
    }

    private void setReusableToken(Token.Type type, String content, boolean isReady) {
        reusableToken.type = type;
        reusableToken.content.setLength(0);
        reusableToken.content.append(content);
        reusableToken.isReady = isReady;
    }

    @Test
    @Timeout(8000)
    void testNextRecord_singleToken() throws Throwable {
        doAnswer(invocation -> {
            setReusableToken(Token.Type.TOKEN, "value1", false);
            return null;
        }).doAnswer(invocation -> {
            setReusableToken(Token.Type.EORECORD, "value2", false);
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(parser);

        assertNotNull(record);
        assertArrayEquals(new String[] { "value1", "value2" }, record.values());
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_emptyRecordReturnsNull() throws Throwable {
        doAnswer(invocation -> {
            setReusableToken(Token.Type.EOF, "", false);
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(parser);

        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withComment() throws Throwable {
        doAnswer(invocation -> {
            setReusableToken(Token.Type.COMMENT, "comment1", false);
            return null;
        }).doAnswer(invocation -> {
            setReusableToken(Token.Type.COMMENT, "comment2", false);
            return null;
        }).doAnswer(invocation -> {
            setReusableToken(Token.Type.EORECORD, "value1", false);
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(parser);

        assertNotNull(record);
        assertArrayEquals(new String[] { "value1" }, record.values());
        assertEquals("comment1\ncomment2", record.getComment());
        assertEquals(1, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_invalidThrowsIOException() {
        try {
            doAnswer(invocation -> {
                setReusableToken(Token.Type.INVALID, "", false);
                return null;
            }).when(lexerMock).nextToken(reusableToken);

            nextRecordMethod.invoke(parser);
            fail("Expected InvocationTargetException with IOException cause");
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IOException);
            assertTrue(cause.getMessage().contains("invalid parse sequence"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    @Timeout(8000)
    void testNextRecord_eofWithIsReadyTrueAddsValue() throws Throwable {
        doAnswer(invocation -> {
            setReusableToken(Token.Type.EOF, "valueEOF", true);
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(parser);

        assertNotNull(record);
        assertArrayEquals(new String[] { "valueEOF" }, record.values());
        assertEquals(1, record.getRecordNumber());
    }
}