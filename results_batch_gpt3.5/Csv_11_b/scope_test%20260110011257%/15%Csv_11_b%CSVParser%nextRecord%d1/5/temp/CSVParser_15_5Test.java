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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CSVParser_15_5Test {

    private CSVParser csvParser;
    private Lexer lexerMock;
    private Token reusableToken;
    private List<String> recordList;
    private Map<String, Integer> headerMapMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mocks and test setup
        lexerMock = mock(Lexer.class);
        headerMapMock = mock(Map.class);

        // Create instance of CSVParser with dummy CSVFormat and Reader using reflection
        csvParser = (CSVParser) java.lang.reflect.Constructor.class
                .getDeclaredConstructor(Class.class, Class.class)
                .newInstance(CSVParser.class, CSVFormat.class);

        // Using reflection to create instance since constructor is public CSVParser(Reader, CSVFormat)
        // We will instantiate with dummy Reader and CSVFormat for testing
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Inject mocks and reset fields
        // Use reflection to set private final fields
        setField(csvParser, "lexer", lexerMock);
        setField(csvParser, "headerMap", headerMapMock);

        // record list
        recordList = new ArrayList<>();
        setField(csvParser, "record", recordList);

        // reusableToken
        reusableToken = new Token();
        setField(csvParser, "reusableToken", reusableToken);

        // recordNumber reset to 0
        setField(csvParser, "recordNumber", 0L);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withTokensAndEORECORD() throws Exception {
        // Setup token sequence: TOKEN, TOKEN, EORECORD
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

        // Spy on addRecordValue to add current reusableToken content to record list
        CSVParser spyParser = spy(csvParser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        CSVRecord record = callNextRecord(spyParser);

        assertNotNull(record);
        assertEquals(3, record.size());
        assertEquals("value1", record.get(0));
        assertEquals("value2", record.get(1));
        assertEquals("value3", record.get(2));
        assertEquals(1L, spyParser.getRecordNumber());
        assertNull(record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_isReadyTrue() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.content = "lastValue";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(csvParser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        CSVRecord record = callNextRecord(spyParser);

        assertNotNull(record);
        assertEquals(1, record.size());
        assertEquals("lastValue", record.get(0));
        assertEquals(1L, spyParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withEOF_isReadyFalse_returnsNull() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.EOF;
            reusableToken.content = "";
            reusableToken.isReady = false;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(csvParser);
        CSVRecord record = callNextRecord(spyParser);

        assertNull(record);
        assertEquals(0L, spyParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withCommentToken() throws Exception {
        // TOKEN, COMMENT, TOKEN, EORECORD sequence
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "val1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content = "comment line";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "val2";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.content = "val3";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(csvParser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        CSVRecord record = callNextRecord(spyParser);

        assertNotNull(record);
        assertEquals(3, record.size());
        assertEquals("val1", record.get(0));
        assertEquals("val2", record.get(1));
        assertEquals("val3", record.get(2));
        assertEquals(1L, spyParser.getRecordNumber());
        assertEquals("comment line", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withMultipleComments() throws Exception {
        // TOKEN, COMMENT, COMMENT, EORECORD sequence
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.TOKEN;
            reusableToken.content = "val1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content = "comment1";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.COMMENT;
            reusableToken.content = "comment2";
            reusableToken.isReady = true;
            return null;
        }).doAnswer(invocation -> {
            reusableToken.type = Token.Type.EORECORD;
            reusableToken.content = "val2";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        CSVParser spyParser = spy(csvParser);
        doAnswer(invocation -> {
            recordList.add(reusableToken.content);
            return null;
        }).when(spyParser).addRecordValue();

        CSVRecord record = callNextRecord(spyParser);

        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("val1", record.get(0));
        assertEquals("val2", record.get(1));
        assertEquals(1L, spyParser.getRecordNumber());
        assertEquals("comment1\ncomment2", record.getComment());
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withInvalidToken_throwsIOException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = Token.Type.INVALID;
            reusableToken.content = "bad";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IOException thrown = assertThrows(IOException.class, () -> {
            callNextRecord(csvParser);
        });
        assertTrue(thrown.getMessage().contains("invalid parse sequence"));
    }

    @Test
    @Timeout(8000)
    void testNextRecord_withUnexpectedToken_throwsIllegalStateException() throws Exception {
        doAnswer(invocation -> {
            reusableToken.type = null; // unexpected null type
            reusableToken.content = "bad";
            reusableToken.isReady = true;
            return null;
        }).when(lexerMock).nextToken(reusableToken);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            callNextRecord(csvParser);
        });
        assertTrue(thrown.getMessage().contains("Unexpected Token type"));
    }

    private CSVRecord callNextRecord(CSVParser parser) throws Exception {
        Method nextRecordMethod = CSVParser.class.getDeclaredMethod("nextRecord");
        nextRecordMethod.setAccessible(true);
        return (CSVRecord) nextRecordMethod.invoke(parser);
    }
}