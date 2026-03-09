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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_15_6Test {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Use a real CSVFormat or mock if needed
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a Lexer mock
        lexerMock = mock(Lexer.class);

        // Create parser instance with a real Reader and format (use dummy reader)
        parser = new CSVParser(new java.io.StringReader(""), format);

        // Inject lexerMock into parser via reflection
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Reset recordNumber to 0 via reflection
        java.lang.reflect.Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 0L);

        // Clear record list
        java.lang.reflect.Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        List<String> recordList = (List<String>) recordField.get(parser);
        recordList.clear();

        // Reset reusableToken
        java.lang.reflect.Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        Token reusableToken = (Token) tokenField.get(parser);
        reusableToken.reset();
    }

    @Test
    @Timeout(8000)
    void testNextRecord_tokenAndEORECORD_addRecordValueCalledAndRecordCreated() throws Exception {
        // Setup tokens sequence: TOKEN -> TOKEN -> EORECORD
        Token token1 = new Token();
        token1.type = Token.Type.TOKEN;
        token1.content = "value1";
        token1.isReady = true;

        Token token2 = new Token();
        token2.type = Token.Type.TOKEN;
        token2.content = "value2";
        token2.isReady = true;

        Token token3 = new Token();
        token3.type = Token.Type.EORECORD;
        token3.content = "value3";
        token3.isReady = true;

        // Prepare reusableToken to be set by lexer.nextToken()
        // We'll simulate lexer.nextToken(Token) by setting reusableToken fields
        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = token1.type;
            argToken.content = token1.content;
            argToken.isReady = token1.isReady;
            return null;
        }).doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = token2.type;
            argToken.content = token2.content;
            argToken.isReady = token2.isReady;
            return null;
        }).doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = token3.type;
            argToken.content = token3.content;
            argToken.isReady = token3.isReady;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        // Spy parser to verify addRecordValue calls
        CSVParser spyParser = spy(parser);

        // Call nextRecord
        CSVRecord record = spyParser.nextRecord();

        // Verify addRecordValue called 3 times (for TOKEN, TOKEN, EORECORD)
        verify(spyParser, times(3)).addRecordValue();

        // Validate CSVRecord content
        assertNotNull(record);
        String[] values = record.values();
        assertArrayEquals(new String[] {"value1", "value2", "value3"}, values);
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_commentToken_appendsCommentAndContinues() throws Exception {
        // Tokens: COMMENT -> TOKEN -> EORECORD
        Token commentToken = new Token();
        commentToken.type = Token.Type.COMMENT;
        commentToken.content = "comment line 1";
        commentToken.isReady = true;

        Token token = new Token();
        token.type = Token.Type.TOKEN;
        token.content = "val1";
        token.isReady = true;

        Token eorecordToken = new Token();
        eorecordToken.type = Token.Type.EORECORD;
        eorecordToken.content = "val2";
        eorecordToken.isReady = true;

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = commentToken.type;
            argToken.content = commentToken.content;
            argToken.isReady = commentToken.isReady;
            return null;
        }).doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = token.type;
            argToken.content = token.content;
            argToken.isReady = token.isReady;
            return null;
        }).doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = eorecordToken.type;
            argToken.content = eorecordToken.content;
            argToken.isReady = eorecordToken.isReady;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVParser spyParser = spy(parser);

        CSVRecord record = spyParser.nextRecord();

        verify(spyParser, times(2)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[] {"val1", "val2"}, record.values());
        assertEquals(1, record.getRecordNumber());
        assertEquals("comment line 1", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_commentMultipleLines_appendsLfBetweenComments() throws Exception {
        // Tokens: COMMENT -> COMMENT -> EORECORD
        Token comment1 = new Token();
        comment1.type = Token.Type.COMMENT;
        comment1.content = "comment line 1";
        comment1.isReady = true;

        Token comment2 = new Token();
        comment2.type = Token.Type.COMMENT;
        comment2.content = "comment line 2";
        comment2.isReady = true;

        Token eorecord = new Token();
        eorecord.type = Token.Type.EORECORD;
        eorecord.content = "val1";
        eorecord.isReady = true;

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = comment1.type;
            argToken.content = comment1.content;
            argToken.isReady = comment1.isReady;
            return null;
        }).doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = comment2.type;
            argToken.content = comment2.content;
            argToken.isReady = comment2.isReady;
            return null;
        }).doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = eorecord.type;
            argToken.content = eorecord.content;
            argToken.isReady = eorecord.isReady;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVParser spyParser = spy(parser);

        CSVRecord record = spyParser.nextRecord();

        verify(spyParser, times(1)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[] {"val1"}, record.values());
        assertEquals(1, record.getRecordNumber());
        assertEquals("comment line 1\ncomment line 2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_eofWithIsReady_addRecordValueCalled() throws Exception {
        // Tokens: TOKEN -> EOF(isReady=true)
        Token token = new Token();
        token.type = Token.Type.TOKEN;
        token.content = "val1";
        token.isReady = true;

        Token eof = new Token();
        eof.type = Token.Type.EOF;
        eof.content = "";
        eof.isReady = true;

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = token.type;
            argToken.content = token.content;
            argToken.isReady = token.isReady;
            return null;
        }).doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = eof.type;
            argToken.content = eof.content;
            argToken.isReady = eof.isReady;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVParser spyParser = spy(parser);

        CSVRecord record = spyParser.nextRecord();

        verify(spyParser, times(2)).addRecordValue();

        assertNotNull(record);
        assertArrayEquals(new String[] {"val1"}, record.values());
        assertEquals(1, record.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_invalidToken_throwsIOException() throws Exception {
        Token invalid = new Token();
        invalid.type = Token.Type.INVALID;
        invalid.content = "";
        invalid.isReady = false;

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = invalid.type;
            argToken.content = invalid.content;
            argToken.isReady = invalid.isReady;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        IOException thrown = assertThrows(IOException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_unexpectedToken_throwsIllegalStateException() throws Exception {
        Token unknown = new Token();
        unknown.type = null; // simulate unknown token type
        unknown.content = "";
        unknown.isReady = false;

        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = unknown.type;
            argToken.content = unknown.content;
            argToken.isReady = unknown.isReady;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> parser.nextRecord());
        assertTrue(thrown.getMessage().contains("Unexpected Token type"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_emptyRecord_returnsNull() throws Exception {
        Token eorecord = new Token();
        eorecord.type = Token.Type.EORECORD;
        eorecord.content = "";
        eorecord.isReady = true;

        // lexer returns EORECORD immediately, no values added
        doAnswer(invocation -> {
            Token argToken = invocation.getArgument(0);
            argToken.type = eorecord.type;
            argToken.content = eorecord.content;
            argToken.isReady = eorecord.isReady;
            return null;
        }).when(lexerMock).nextToken(any(Token.class));

        CSVRecord record = parser.nextRecord();

        assertNull(record);
    }
}