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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserInitializeHeaderTest {

    private CSVParser parser;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
    }

    private Map<String, Integer> invokeInitializeHeader(CSVParser parser) throws Throwable {
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        try {
            return (Map<String, Integer>) method.invoke(parser);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Helper class to simulate CSVRecord since CSVRecord constructor is not accessible
    private static class CSVRecordStub {
        private final String[] values;

        CSVRecordStub(String[] values) {
            this.values = values;
        }

        public String[] values() {
            return values;
        }
    }

    // Helper class implementing CSVParser interface by delegation, since CSVParser is final
    private static class CSVParserDelegate extends CSVParser {
        private final CSVFormat format;
        private final CSVRecordStub nextRecordStub;
        private boolean nextRecordCalled = false;

        CSVParserDelegate(Reader reader, CSVFormat format, CSVRecordStub nextRecordStub) {
            super(reader, format);
            this.format = format;
            this.nextRecordStub = nextRecordStub;
        }

        @Override
        public CSVRecord nextRecord() {
            nextRecordCalled = true;
            if (nextRecordStub == null) {
                return null;
            }
            return new CSVRecordAdapter(nextRecordStub);
        }

        public boolean wasNextRecordCalled() {
            return nextRecordCalled;
        }

        @Override
        public CSVFormat getFormat() {
            return format;
        }
    }

    // Adapter class to convert CSVRecordStub to CSVRecord via delegation
    private static class CSVRecordAdapter extends CSVRecord {
        private final CSVRecordStub stub;

        CSVRecordAdapter(CSVRecordStub stub) {
            super(null, null, null, 0L);
            this.stub = stub;
        }

        @Override
        public String[] values() {
            return stub.values();
        }
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(null);
        parser = new CSVParserDelegate(mock(Reader.class), formatMock, null);
        Map<String, Integer> result = invokeInitializeHeader(parser);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordNonNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        CSVRecordStub recordStub = new CSVRecordStub(new String[]{"a", "b", "c"});
        parser = new CSVParserDelegate(mock(Reader.class), formatMock, recordStub);
        Map<String, Integer> result = invokeInitializeHeader(parser);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(0), result.get("a"));
        assertEquals(Integer.valueOf(1), result.get("b"));
        assertEquals(Integer.valueOf(2), result.get("c"));
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderEmpty_nextRecordNull() throws Throwable {
        when(formatMock.getHeader()).thenReturn(new String[0]);
        parser = new CSVParserDelegate(mock(Reader.class), formatMock, null);
        Map<String, Integer> result = invokeInitializeHeader(parser);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordTrue() throws Throwable {
        String[] header = new String[]{"x", "y"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);
        CSVRecordStub recordStub = new CSVRecordStub(new String[]{"ignore"});
        CSVParserDelegate stubParser = new CSVParserDelegate(mock(Reader.class), formatMock, recordStub);
        parser = stubParser;
        Map<String, Integer> result = invokeInitializeHeader(parser);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("x"));
        assertEquals(Integer.valueOf(1), result.get("y"));
        // nextRecord should be called once to skip header record
        assertTrue(stubParser.wasNextRecordCalled());
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_formatHeaderNonEmpty_skipHeaderRecordFalse() throws Throwable {
        String[] header = new String[]{"col1", "col2"};
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        parser = new CSVParserDelegate(mock(Reader.class), formatMock, null) {
            @Override
            public CSVRecord nextRecord() {
                fail("nextRecord should not be called when skipHeaderRecord is false and header is non-empty");
                return null;
            }
        };
        Map<String, Integer> result = invokeInitializeHeader(parser);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(0), result.get("col1"));
        assertEquals(Integer.valueOf(1), result.get("col2"));
    }
}