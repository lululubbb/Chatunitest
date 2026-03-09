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
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CSVParserNextRecordTest {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;
    private Map<String, Integer> headerMapMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mock dependencies
        lexerMock = mock(Lexer.class);
        headerMapMock = mock(Map.class);

        // Construct CSVParser with a Reader and CSVFormat (use null and mock for simplicity)
        parser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Inject mocks and fields via reflection
        setField(parser, "lexer", lexerMock);
        setField(parser, "headerMap", headerMapMock);
        setField(parser, "recordNumber", 0L);
        setField(parser, "characterOffset", 0L);

        // Create a real reusableToken instance and inject
        reusableToken = new Token();
        setField(parser, "reusableToken", reusableToken);

        // Clear recordList (already empty, but just in case)
        List<String> recordList = (List<String>) getField(parser, "recordList");
        recordList.clear();
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokenAndEORECORD() throws Exception {
        // Setup tokens: TOKEN, TOKEN, EORECORD
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

        // Spy on addRecordValue to verify calls
        CSVParser spyParser = spy(parser);
        doNothing().when(spyParser).addRecordValue(anyBoolean());

        // Use reflection to invoke private nextRecord
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Verify addRecordValue called three times with correct lastRecord flags
        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(spyParser, times(3)).addRecordValue(captor.capture());
        List<Boolean> calls = captor.getAllValues();
        assertEquals(List.of(false, false, true), calls);

        // Since addRecordValue is stubbed, recordList is empty, so record should be null
        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFReady() throws Exception {
        // Setup tokens: TOKEN, EOF (isReady true)
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "value1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.isReady = true;
            reusableToken.content = null;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        // Spy on addRecordValue to verify calls
        CSVParser spyParser = spy(parser);
        doNothing().when(spyParser).addRecordValue(anyBoolean());

        // Use reflection to invoke private nextRecord
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Verify addRecordValue called twice: false then true
        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(spyParser, times(2)).addRecordValue(captor.capture());
        List<Boolean> calls = captor.getAllValues();
        assertEquals(List.of(false, true), calls);

        // record should be null since addRecordValue stubbed and recordList empty
        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentToken() throws Exception {
        // Setup tokens: COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content = "comment line 1";
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

        // Spy on addRecordValue to verify calls
        CSVParser spyParser = spy(parser);
        doNothing().when(spyParser).addRecordValue(anyBoolean());

        // Use reflection to invoke private nextRecord
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Verify addRecordValue called twice: false then true (COMMENT does not call addRecordValue)
        ArgumentCaptor<Boolean> captor = ArgumentCaptor.forClass(Boolean.class);
        verify(spyParser, times(2)).addRecordValue(captor.capture());
        List<Boolean> calls = captor.getAllValues();
        assertEquals(List.of(false, true), calls);

        // record should be null since addRecordValue stubbed and recordList empty
        assertNull(record);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        // Setup token: INVALID
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.INVALID;
            reusableToken.content = "bad";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        // Spy on getCurrentLineNumber to return 5
        CSVParser spyParser = spy(parser);
        doReturn(5L).when(spyParser).getCurrentLineNumber();

        // Use reflection to invoke private nextRecord and expect IOException
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        IOException thrown = assertThrows(IOException.class, () -> nextRecordMethod.invoke(spyParser));
        assertTrue(thrown.getCause().getMessage().contains("(line 5) invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withUnexpectedToken_throwsIllegalStateException() throws Exception {
        // Setup token: a token type not handled (e.g. null or a fake type)
        doAnswer(invocation -> {
            reusableToken.type = null;
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        // Use reflection to invoke private nextRecord and expect IllegalStateException
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> nextRecordMethod.invoke(parser));
        assertTrue(thrown.getCause().getMessage().contains("Unexpected Token type"));
    }

    // Helper methods to access private fields
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static Object getField(Object target, String fieldName) throws Exception {
        java.lang.reflect.Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}