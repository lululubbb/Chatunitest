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

class CSVParser_15_3Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;
    private List<String> recordList;
    private Map<String, Integer> headerMap;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Lexer
        lexerMock = mock(Lexer.class);

        // Prepare headerMap (empty for simplicity)
        headerMap = Map.of();

        // Create instance of CSVParser with mocked Reader and CSVFormat
        parser = new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class));

        // Inject mocks and fields via reflection
        setField(parser, "lexer", lexerMock);
        setField(parser, "headerMap", headerMap);

        // reusableToken is final in CSVParser, get it via reflection instead of creating new
        reusableToken = (Token) getFieldValue(parser, "reusableToken");
        // Initialize reusableToken.content as StringBuilder to match actual type
        setField(reusableToken, "content", new StringBuilder());

        recordList = (List<String>) getFieldValue(parser, "record");
        recordList.clear();

        setField(parser, "recordNumber", 0L);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }

    private void setReusableTokenType(Token.Type type) {
        try {
            Field typeField = getFieldFromClassHierarchy(Token.class, "type");
            typeField.setAccessible(true);
            typeField.set(reusableToken, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setReusableTokenContent(String content) {
        // Clear and append to StringBuilder content
        StringBuilder sb = (StringBuilder) getFieldValue(reusableToken, "content");
        sb.setLength(0);
        sb.append(content);
    }

    private void setReusableTokenIsReady(boolean isReady) {
        try {
            Field isReadyField = getFieldFromClassHierarchy(Token.class, "isReady");
            isReadyField.setAccessible(true);
            isReadyField.setBoolean(reusableToken, isReady);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFieldValue(Object target, String fieldName) {
        try {
            Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeAddRecordValue() throws Exception {
        // invoke private method addRecordValue via reflection
        java.lang.reflect.Method method = CSVParser.class.getDeclaredMethod("addRecordValue");
        method.setAccessible(true);
        method.invoke(parser);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEORECORD() throws Exception {
        doAnswer(invocation -> {
            setReusableTokenType(Token.Type.TOKEN);
            setReusableTokenContent("val1");
            return null;
        }).doAnswer(invocation -> {
            setReusableTokenType(Token.Type.TOKEN);
            setReusableTokenContent("val2");
            return null;
        }).doAnswer(invocation -> {
            setReusableTokenType(Token.Type.EORECORD);
            setReusableTokenContent("val3");
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertArrayEquals(new String[]{"val1", "val2", "val3"}, record.values());
        assertEquals(1L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_andIsReadyTrue() throws Exception {
        doAnswer(invocation -> {
            setReusableTokenType(Token.Type.TOKEN);
            setReusableTokenContent("val1");
            return null;
        }).doAnswer(invocation -> {
            setReusableTokenType(Token.Type.EOF);
            setReusableTokenContent("val2");
            setReusableTokenIsReady(true);
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertArrayEquals(new String[]{"val1", "val2"}, record.values());
        assertEquals(1L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_andIsReadyFalse() throws Exception {
        doAnswer(invocation -> {
            setReusableTokenType(Token.Type.EOF);
            setReusableTokenContent("val1");
            setReusableTokenIsReady(false);
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNull(record);
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentTokens() throws Exception {
        doAnswer(invocation -> {
            setReusableTokenType(Token.Type.COMMENT);
            setReusableTokenContent("comment1");
            return null;
        }).doAnswer(invocation -> {
            setReusableTokenType(Token.Type.COMMENT);
            setReusableTokenContent("comment2");
            return null;
        }).doAnswer(invocation -> {
            setReusableTokenType(Token.Type.EORECORD);
            setReusableTokenContent("val1");
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord record = parser.nextRecord();

        assertNotNull(record);
        assertArrayEquals(new String[]{"val1"}, record.values());
        assertEquals(1L, parser.getRecordNumber());
        assertEquals("comment1\ncomment2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            setReusableTokenType(Token.Type.INVALID);
            setReusableTokenContent("bad");
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IOException ex = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withUnexpectedToken_throwsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            setReusableTokenType(null); // unexpected token type
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("Unexpected Token type"));
    }
}