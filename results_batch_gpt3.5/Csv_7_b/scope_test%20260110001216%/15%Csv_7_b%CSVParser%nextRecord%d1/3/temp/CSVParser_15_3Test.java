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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.Token.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_15_3Test {

    CSVParser parser;
    Lexer lexerMock;
    Token token;
    List<String> recordList;

    @BeforeEach
    void setUp() throws Exception {
        lexerMock = mock(Lexer.class);
        // Create CSVFormat mock or dummy as needed
        CSVFormat format = mock(CSVFormat.class);

        // Use Reader and CSVFormat constructor via reflection since constructor is public
        parser = new CSVParser(mock(java.io.Reader.class), format);

        // Inject mocked lexer
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Inject reusableToken
        token = new Token();
        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        // Inject record list
        recordList = new ArrayList<>();
        java.lang.reflect.Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(parser, recordList);

        // Inject headerMap (empty map)
        java.lang.reflect.Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, null);

        // Inject recordNumber
        java.lang.reflect.Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenAndEORECORD_andEOF_withComment() throws Exception {
        // Setup tokens for TOKEN, EORECORD, EOF with isReady true and COMMENT
        // Use an array of tokens to simulate lexer.nextToken calls
        Token[] tokens = new Token[6];

        tokens[0] = new Token();
        tokens[0].type = Type.TOKEN;
        tokens[0].content = "value1";

        tokens[1] = new Token();
        tokens[1].type = Type.COMMENT;
        tokens[1].content = "comment1";

        tokens[2] = new Token();
        tokens[2].type = Type.TOKEN;
        tokens[2].content = "value2";

        tokens[3] = new Token();
        tokens[3].type = Type.EORECORD;
        tokens[3].content = "value3";

        tokens[4] = new Token();
        tokens[4].type = Type.EOF;
        tokens[4].content = "value4";
        tokens[4].isReady = true;

        tokens[5] = new Token();
        tokens[5].type = Type.TOKEN;
        tokens[5].content = "value5";

        // Use an index to track calls
        final int[] callIndex = {0};

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            Token current = tokens[callIndex[0]];
            argToken.type = current.type;
            argToken.content = current.content;
            argToken.isReady = current.isReady;
            callIndex[0]++;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Spy on addRecordValue to add token content to record list
        Method addRecordValueMethod = CSVParser.class.getDeclaredMethod("addRecordValue");
        addRecordValueMethod.setAccessible(true);

        // Override addRecordValue to add token content to record list
        CSVParser spyParser = spy(parser);
        doAnswer(invocation -> {
            Token reusableToken = (Token) CSVParser.class.getDeclaredField("reusableToken").get(spyParser);
            List<String> record = (List<String>) CSVParser.class.getDeclaredField("record").get(spyParser);
            record.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        // Replace lexer and token in spyParser
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(spyParser, lexerMock);

        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(spyParser, token);

        java.lang.reflect.Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        recordField.set(spyParser, new ArrayList<>());

        java.lang.reflect.Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(spyParser, 0L);

        // Invoke nextRecord
        CSVRecord record = spyParser.nextRecord();

        // Verify record is not null
        assertNotNull(record);

        // Verify record contents
        String[] values = record.values();
        assertArrayEquals(new String[] {"value1", "value2", "value3", "value4"}, values);

        // Verify comment contains comment1
        assertEquals("comment1", record.getComment());

        // Verify record number incremented to 1
        assertEquals(1L, spyParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_invalidToken_throwsIOException() throws Exception {
        token.type = Type.INVALID;
        token.content = "invalid";

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = Type.INVALID;
            argToken.content = "invalid";
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Inject lexer and token
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        IOException ex = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_unexpectedToken_throwsIllegalStateException() throws Exception {
        token.type = Type.EOF; // Use unexpected type to trigger default
        token.content = "unexpected";

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = Type.valueOf("UNKNOWN") != null ? Type.valueOf("UNKNOWN") : null;
            argToken.content = "unexpected";
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Because UNKNOWN is not in enum, simulate by setting type to null via reflection
        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = null;
            argToken.content = "unexpected";
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(ex.getMessage().contains("Unexpected Token type"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_emptyRecord_returnsNull() throws Exception {
        // Setup token to EOF immediately with isReady false and no record values
        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = Type.EOF;
            argToken.isReady = false;
            argToken.content = "";
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        tokenField.set(parser, token);

        CSVRecord record = parser.nextRecord();
        assertNull(record);
    }
}