package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_15_2Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;
    private List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVFormat stub, can be null if not used in nextRecord
        CSVFormat format = mock(CSVFormat.class);

        // Create Lexer mock
        lexerMock = mock(Lexer.class);

        // Create parser instance with dummy Reader and format
        parser = new CSVParser(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) { return -1; }
            @Override
            public void close() {}
        }, format);

        // Inject lexerMock into parser using reflection
        var lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Inject headerMap as empty map to avoid NPE
        var headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());

        // Inject record list as the parser's record
        var recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = new ArrayList<>();
        recordField.set(parser, recordList);

        // Inject reusableToken
        var tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        token = new Token();
        tokenField.set(parser, token);

        // Reset recordNumber to 0
        var recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenAndEORECORD() throws Exception {
        // Setup token sequence: TOKEN, TOKEN, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "value1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "value2";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "value3";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        // Spy parser to verify addRecordValue call
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            // add the current token content to record list
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Use reflection to invoke private nextRecord method
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        // Verify addRecordValue called 3 times (2 TOKEN + 1 EORECORD)
        verify(spyParser, times(3)).addRecordValue();

        // Verify record content
        assertNotNull(record);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_eofWithIsReadyTrue() throws Exception {
        // Setup token sequence: TOKEN, EOF with isReady true
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content = "valEOF";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        verify(spyParser, times(2)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[]{"val", "valEOF"}, record.values());
        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_eofWithIsReadyFalse() throws Exception {
        // Setup token sequence: TOKEN, EOF with isReady false
        doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EOF;
            token.content = "valEOF";
            token.isReady = false;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        verify(spyParser, times(1)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[]{"val"}, record.values());
        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_commentToken() throws Exception {
        // Setup token sequence: COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment line 1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "val2";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        verify(spyParser, times(2)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[]{"val", "val2"}, record.values());
        assertEquals(1L, record.getRecordNumber());
        assertEquals("comment line 1", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_multipleComments() throws Exception {
        // Setup token sequence: COMMENT, COMMENT, TOKEN, EORECORD
        doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment1";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.COMMENT;
            token.content = "comment2";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.TOKEN;
            token.content = "val";
            token.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "val2";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        verify(spyParser, times(2)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[]{"val", "val2"}, record.values());
        assertEquals(1L, record.getRecordNumber());
        assertEquals("comment1\ncomment2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_invalidTokenThrowsIOException() throws Exception {
        doAnswer(invocation -> {
            token.type = Token.Type.INVALID;
            token.content = "bad";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        IOException thrown = assertThrows(IOException.class, () -> nextRecordMethod.invoke(parser));
        assertTrue(thrown.getCause() instanceof IOException || thrown instanceof IOException);
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_unexpectedTokenTypeThrowsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            // Use a token type not handled in switch
            token.type = null;
            token.content = "val";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);

        Exception ex = assertThrows(Exception.class, () -> nextRecordMethod.invoke(parser));
        Throwable cause = ex.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Unexpected Token type"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_emptyRecordReturnsNull() throws Exception {
        // Setup token sequence: EORECORD with no values added (record empty)
        doAnswer(invocation -> {
            token.type = Token.Type.EORECORD;
            token.content = "";
            token.isReady = true;
            return null;
        }).when(lexerMock).nextToken(token);

        CSVParser spyParser = spy(parser);
        doNothing().when(spyParser).addRecordValue();

        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        CSVRecord record = (CSVRecord) nextRecordMethod.invoke(spyParser);

        assertNull(record);
    }
}