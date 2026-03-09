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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.Token.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CSVParser_15_5Test {

    private CSVParser parser;
    private Lexer lexerMock;
    private Token reusableToken;
    private List<String> recordList;
    private Map<String, Integer> headerMapMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a dummy CSVFormat for constructor (can be null since unused here)
        CSVFormat format = mock(CSVFormat.class);

        // Instantiate parser with dummy Reader and format
        parser = new CSVParser(mock(Reader.class), format);

        // Use reflection to inject mocks and manipulate fields
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerMock = mock(Lexer.class);
        lexerField.set(parser, lexerMock);

        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordList = new ArrayList<>();
        recordField.set(parser, recordList);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapMock = mock(Map.class);
        headerMapField.set(parser, headerMapMock);

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableToken = new Token();
        reusableTokenField.set(parser, reusableToken);
    }

    @Test
    @Timeout(8000)
    void nextRecord_shouldReturnNull_whenEOFImmediately() throws Exception {
        // Setup reusableToken to EOF and not ready
        reusableToken.type = Type.EOF;
        reusableToken.isReady = false;

        doAnswer(invocation -> {
            // reusableToken is already set to EOF, so no changes
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVRecord result = parser.nextRecord();

        assertNull(result);
        assertTrue(recordList.isEmpty());
    }

    @Test
    @Timeout(8000)
    void nextRecord_shouldReturnRecord_whenSingleToken() throws Exception {
        // Setup sequence: TOKEN then EORECORD
        // We simulate lexer.nextToken() setting reusableToken.type accordingly

        // Setup reusableToken content for addRecordValue to add to record list
        reusableToken.type = Type.TOKEN;
        reusableToken.content = "value1";

        // Mock lexer.nextToken to change reusableToken.type on calls
        doAnswer(invocation -> {
            reusableToken.type = Type.TOKEN;
            reusableToken.content = "value1";
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Type.EORECORD;
            reusableToken.content = "value2";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        // Spy on parser to verify addRecordValue is called and to simulate it adding values
        CSVParser spyParser = spy(parser);

        doAnswer(invocation -> {
            // addRecordValue adds reusableToken.content to record list
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spy's fields same as original parser
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, headerMapMock);
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(spyParser, reusableToken);

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals(1L, spyParser.getRecordNumber());

        // The comment should be null because no COMMENT token was handled
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void nextRecord_shouldAccumulateCommentAndReturnRecord() throws Exception {
        // Setup tokens sequence: COMMENT, COMMENT, TOKEN, EORECORD
        // The reusableToken.type should be set accordingly on each lexer.nextToken call

        // Setup reusableToken content for each token call
        final String comment1 = "#comment1";
        final String comment2 = "#comment2";
        final String val1 = "val1";
        final String val2 = "val2";

        // We need to simulate the loop in nextRecord with multiple lexer.nextToken calls
        // The sequence of token.type and content:
        // 1. COMMENT with content comment1
        // 2. COMMENT with content comment2
        // 3. TOKEN with content val1
        // 4. EORECORD with content val2

        // We will use an array to track calls and set reusableToken accordingly
        final Token.Type[] types = new Token.Type[] {Type.COMMENT, Type.COMMENT, Type.TOKEN, Type.EORECORD};
        final String[] contents = new String[] {comment1, comment2, val1, val2};
        final boolean[] isReadyFlags = new boolean[] {false, false, false, false};

        final int[] callCount = {0};

        doAnswer(invocation -> {
            int i = callCount[0]++;
            reusableToken.type = types[i];
            reusableToken.content = contents[i];
            reusableToken.isReady = isReadyFlags[i];
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        // Spy parser to mock addRecordValue adding reusableToken.content to record list
        CSVParser spyParser = spy(parser);

        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Inject spy's fields same as original parser
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, recordList);
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(spyParser, headerMapMock);
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);
        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);
        reusableTokenField.set(spyParser, reusableToken);

        CSVRecord record = spyParser.nextRecord();

        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals(val1, record.get(0));
        assertEquals(val2, record.get(1));
        assertEquals(1L, spyParser.getRecordNumber());

        // The comment should be the two comments joined by LF
        String expectedComment = comment1 + Constants.LF + comment2;
        assertEquals(expectedComment, record.getComment());
    }

    @Test
    @Timeout(8000)
    void nextRecord_shouldThrowIOException_whenInvalidToken() throws Exception {
        reusableToken.type = Type.INVALID;
        reusableToken.content = "bad";

        doAnswer(invocation -> {
            reusableToken.type = Type.INVALID;
            reusableToken.content = "bad";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IOException thrown = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void nextRecord_shouldThrowIllegalStateException_whenUnexpectedToken() throws Exception {
        // Create an unexpected token type not handled by switch
        // We use reflection to create a new Token.Type enum constant is impossible,
        // so simulate by setting type to null and expect NPE wrapped in IllegalStateException
        reusableToken.type = null;

        doAnswer(invocation -> {
            reusableToken.type = null;
            reusableToken.content = "unexpected";
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("Unexpected Token type"));
    }
}