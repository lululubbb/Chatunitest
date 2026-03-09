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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_19_2Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Lexer
        lexerMock = mock(Lexer.class);

        // Create a real Token instance to be used as reusableToken
        token = new Token();

        // Create parser instance with reflection to inject mocks and fields
        parser = (CSVParser) java.lang.reflect.Proxy.newProxyInstance(
                CSVParser.class.getClassLoader(),
                new Class[] { CSVParser.class },
                (proxy, method, args) -> null);

        // Use reflection to create a real CSVParser instance using constructor
        var constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        parser = constructor.newInstance(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Inject lexer mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Inject reusableToken
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        // Clear recordList
        Field recordListField = CSVParser.class.getDeclaredField("recordList");
        recordListField.setAccessible(true);
        List<String> recordList = (List<String>) recordListField.get(parser);
        recordList.clear();

        // Set headerMap to empty map
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());

        // Set recordNumber to 0
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Set characterOffset to 0
        Field characterOffsetField = CSVParser.class.getDeclaredField("characterOffset");
        characterOffsetField.setAccessible(true);
        characterOffsetField.setLong(parser, 0L);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_TokenThenEORECORD() throws Exception {
        // Setup tokens: TOKEN -> EORECORD
        when(lexerMock.getCharacterPosition()).thenReturn(5L);

        doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            if (arg.type == null || arg.type == Token.Type.TOKEN) {
                arg.type = Token.Type.TOKEN;
                arg.content = "value1";
            } else if (arg.type == Token.Type.TOKEN) {
                arg.type = Token.Type.EORECORD;
                arg.content = "value2";
            }
            return null;
        }).doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            arg.type = Token.Type.EORECORD;
            arg.content = "value2";
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Because addRecordValue is private, we spy parser and mock addRecordValue to add to recordList
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            boolean lastRecord = invocation.getArgument(0);
            Field recordListField = CSVParser.class.getDeclaredField("recordList");
            recordListField.setAccessible(true);
            List<String> recordList = (List<String>) recordListField.get(spyParser);
            recordList.add("value");
            return null;
        }).when(spyParser).addRecordValue(anyBoolean());

        // Use reflection to invoke nextRecord
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        assertNotNull(record);
        assertEquals(1L, spyParser.getRecordNumber());
        assertEquals(1, record.size());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_CommentToken() throws Exception {
        when(lexerMock.getCharacterPosition()).thenReturn(0L);

        // Setup token sequence: COMMENT -> TOKEN -> EORECORD
        doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            if (arg.type == null) {
                arg.type = Token.Type.COMMENT;
                arg.content = "comment1";
            } else if (arg.type == Token.Type.COMMENT) {
                arg.type = Token.Type.TOKEN;
                arg.content = "value1";
            } else if (arg.type == Token.Type.TOKEN) {
                arg.type = Token.Type.EORECORD;
                arg.content = "value2";
            }
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            Field recordListField = CSVParser.class.getDeclaredField("recordList");
            recordListField.setAccessible(true);
            List<String> recordList = (List<String>) recordListField.get(spyParser);
            recordList.add("value");
            return null;
        }).when(spyParser).addRecordValue(anyBoolean());

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        assertNotNull(record);
        assertEquals(1L, spyParser.getRecordNumber());
        assertEquals(1, record.size());
        assertEquals("comment1", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_EOFWithIsReady() throws Exception {
        when(lexerMock.getCharacterPosition()).thenReturn(0L);

        // Setup token sequence: TOKEN -> EOF (isReady true)
        doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            if (arg.type == null) {
                arg.type = Token.Type.TOKEN;
                arg.content = "value1";
            } else if (arg.type == Token.Type.TOKEN) {
                arg.type = Token.Type.EOF;
                arg.isReady = true;
                arg.content = "value2";
            }
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            Field recordListField = CSVParser.class.getDeclaredField("recordList");
            recordListField.setAccessible(true);
            List<String> recordList = (List<String>) recordListField.get(spyParser);
            recordList.add("value");
            return null;
        }).when(spyParser).addRecordValue(anyBoolean());

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        assertNotNull(record);
        assertEquals(1L, spyParser.getRecordNumber());
        assertEquals(1, record.size());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_InvalidTokenThrowsIOException() throws Exception {
        when(lexerMock.getCharacterPosition()).thenReturn(0L);

        doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            arg.type = Token.Type.INVALID;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        IOException thrown = assertThrows(IOException.class, () -> nextRecordMethod.invoke(parser));
        // InvocationTargetException wraps IOException, so unwrap:
        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IOException);
        assertTrue(cause.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_UnexpectedTokenTypeThrowsIllegalStateException() throws Exception {
        when(lexerMock.getCharacterPosition()).thenReturn(0L);

        doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            arg.type = Token.Type.EOF; // EOF but isReady false, so default case triggers IllegalStateException
            arg.isReady = false;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Modify token type to an unexpected enum value by reflection to simulate unknown token type
        // Since enum can't be modified, we simulate by setting type to null after first call
        // But safer is to spy and forcibly set token.type to an unexpected value after one iteration

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        // We simulate by spying and forcing reusableToken.type after first call
        CSVParser spyParser = spy(parser);

        doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            arg.type = Token.Type.EOF;
            arg.isReady = false;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // After first call, forcibly set reusableToken.type to null to cause default case
        doAnswer(invocation -> {
            Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
            tokenField.setAccessible(true);
            Token t = (Token) tokenField.get(spyParser);
            t.type = null;
            return null;
        }).when(spyParser).addRecordValue(anyBoolean());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> nextRecordMethod.invoke(spyParser));
        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Unexpected Token type"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_EmptyRecordListReturnsNull() throws Exception {
        when(lexerMock.getCharacterPosition()).thenReturn(0L);

        doAnswer(invocation -> {
            Token arg = invocation.getArgument(0);
            arg.type = Token.Type.EOF;
            arg.isReady = false;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Spy parser and mock addRecordValue to not add anything to recordList (empty)
        CSVParser spyParser = spy(parser);
        doNothing().when(spyParser).addRecordValue(anyBoolean());

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        assertNull(record);
    }
}