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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_15_1Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token token;
    private List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Lexer
        lexerMock = mock(Lexer.class);

        // Create real Token instance
        token = new Token();

        // Create parser instance with dummy CSVFormat (can be null for this test)
        parser = new CSVParser(new java.io.StringReader(""), null);

        // Inject mocked Lexer into parser via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Inject reusableToken field with our token instance
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        // Access record field
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Inject headerMap (empty map)
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, Map.of());

        // Reset recordNumber to 0
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Stub getCurrentLineNumber to return 1
        Method getCurrentLineNumberMethod = CSVParser.class.getDeclaredMethod("getCurrentLineNumber");
        getCurrentLineNumberMethod.setAccessible(true);
        // We'll mock getCurrentLineNumber by spying if needed in tests below
    }

    private void setTokenType(Token.Type type, String content, boolean isReady) {
        token.type = type;
        token.content = content;
        token.isReady = isReady;
    }

    private void addRecordValueSpy() throws Exception {
        // Spy addRecordValue private method to add content of token to record list
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);
        addRecordValueMethod.invoke(parser);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensThenEORECORD() throws Exception {
        // Setup token sequence: TOKEN, TOKEN, EORECORD
        // We will simulate lexer.nextToken(token) calls with these token types in order
        doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            if (t.type == null || t.type == Token.Type.EOF) {
                setTokenType(Token.Type.TOKEN, "value1", true);
            } else if ("value1".equals(t.content)) {
                setTokenType(Token.Type.TOKEN, "value2", true);
            } else {
                setTokenType(Token.Type.EORECORD, null, true);
            }
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // We also need to spy addRecordValue to actually add token.content to record list
        // But since addRecordValue is private and not implemented here, we simulate it manually
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Because addRecordValue is private and no code provided, we simulate it by calling addRecordValue manually after each token
        // So we override the parser's addRecordValue to add token.content to record list
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);

        // We simulate the loop manually by calling nextToken and addRecordValue accordingly
        // But since nextRecord calls lexer.nextToken and addRecordValue internally, we need to mock lexer.nextToken to set token and addRecordValue to add token content

        // Instead, we prepare a sequence of token types and content for lexer.nextToken to set on the reusableToken
        Token.Type[] tokenTypes = new Token.Type[] { Token.Type.TOKEN, Token.Type.TOKEN, Token.Type.EORECORD };
        String[] contents = new String[] { "value1", "value2", "value3" };
        int[] callCount = {0};

        doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            int idx = callCount[0]++;
            if (idx < tokenTypes.length) {
                t.type = tokenTypes[idx];
                t.content = contents[idx];
                t.isReady = true;
            } else {
                t.type = Token.Type.EOF;
                t.content = null;
                t.isReady = true;
            }
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Spy addRecordValue to add token.content to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's lexer and reusableToken fields
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, token);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);

        // Call nextRecord
        CSVRecord record = spyParser.nextRecord();

        // Verify record is not null and contains expected values
        assertNotNull(record);
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, record.values().toArray());
        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentToken() throws Exception {
        // Setup token sequence: COMMENT, TOKEN, EORECORD
        Token.Type[] tokenTypes = new Token.Type[] { Token.Type.COMMENT, Token.Type.TOKEN, Token.Type.EORECORD };
        String[] contents = new String[] { "comment line", "value1", "value2" };
        int[] callCount = {0};

        doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            int idx = callCount[0]++;
            if (idx < tokenTypes.length) {
                t.type = tokenTypes[idx];
                t.content = contents[idx];
                t.isReady = true;
            } else {
                t.type = Token.Type.EOF;
                t.content = null;
                t.isReady = true;
            }
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Spy addRecordValue to add token.content to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spyParser's lexer and reusableToken fields
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, token);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);

        // Call nextRecord
        CSVRecord record = spyParser.nextRecord();

        // Verify record is not null and contains expected values
        assertNotNull(record);
        assertArrayEquals(new String[] { "value1", "value2" }, record.values().toArray());
        assertEquals(1L, record.getRecordNumber());
        assertEquals("comment line", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOFAndIsReadyTrue() throws Exception {
        // Setup token sequence: TOKEN, EOF (isReady true)
        Token.Type[] tokenTypes = new Token.Type[] { Token.Type.TOKEN, Token.Type.EOF };
        String[] contents = new String[] { "value1", "value2" };
        boolean[] isReadyFlags = new boolean[] { true, true };
        int[] callCount = {0};

        doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            int idx = callCount[0]++;
            if (idx < tokenTypes.length) {
                t.type = tokenTypes[idx];
                t.content = contents[idx];
                t.isReady = isReadyFlags[idx];
            } else {
                t.type = Token.Type.EOF;
                t.content = null;
                t.isReady = false;
            }
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, token);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertArrayEquals(new String[] { "value1", "value2" }, record.values().toArray());
        assertEquals(1L, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            t.type = Token.Type.INVALID;
            t.content = null;
            t.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        IOException ex = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withUnexpectedTokenType_throwsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            t.type = Token.Type.EORECORD; // valid token to start with
            t.content = "value1";
            t.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            t.type = null; // unexpected null token type to trigger default case
            t.content = null;
            t.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        // Spy addRecordValue to avoid NPE during addRecordValue call
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            recordList.add(token.content);
            return null;
        }).when(spyParser).addRecordValue();

        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, Map.of());
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);

        IllegalStateException ex = assertThrows(IllegalStateException.class, spyParser::nextRecord);
        assertTrue(ex.getMessage().contains("Unexpected Token type"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_returnsNullWhenRecordEmpty() throws Exception {
        // Setup lexer.nextToken to return EOF immediately
        doAnswer(invocation -> {
            Token t = invocation.getArgument(0);
            t.type = Token.Type.EOF;
            t.content = null;
            t.isReady = true;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        // record list is empty
        recordList.clear();

        CSVRecord record = parser.nextRecord();

        assertNull(record);
    }
}