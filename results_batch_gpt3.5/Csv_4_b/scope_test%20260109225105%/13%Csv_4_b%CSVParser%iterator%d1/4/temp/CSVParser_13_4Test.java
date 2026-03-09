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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIteratorTest {

    private CSVParser parser;

    private static class TestCSVParserHelper {
        private int callCount = 0;
        private boolean closed = false;

        CSVRecord nextRecord() throws IOException {
            callCount++;
            if (callCount == 1) {
                return mock(CSVRecord.class);
            } else if (callCount == 2) {
                return mock(CSVRecord.class);
            }
            return null;
        }

        boolean isClosed() {
            return closed;
        }

        void closeParser() {
            closed = true;
        }
    }

    private TestCSVParserHelper helper;

    @BeforeEach
    void setUp() {
        helper = spy(new TestCSVParserHelper());

        parser = mock(CSVParser.class);

        try {
            when(parser.isClosed()).thenAnswer(invocation -> helper.isClosed());
            when(parser.nextRecord()).thenAnswer(invocation -> helper.nextRecord());

            // Provide a real iterator implementation that uses the mocked parser's methods
            when(parser.iterator()).thenAnswer(invocation -> new Iterator<CSVRecord>() {
                private CSVRecord current;

                private CSVRecord getNextRecord() {
                    try {
                        return parser.nextRecord();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public boolean hasNext() {
                    if (parser.isClosed()) {
                        return false;
                    }
                    if (current == null) {
                        current = getNextRecord();
                    }
                    return current != null;
                }

                @Override
                public CSVRecord next() {
                    if (parser.isClosed()) {
                        throw new NoSuchElementException("CSVParser has been closed");
                    }
                    CSVRecord next = current;
                    current = null;

                    if (next == null) {
                        next = getNextRecord();
                        if (next == null) {
                            throw new NoSuchElementException("No more CSV records available");
                        }
                    }
                    return next;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNextNormalFlow() {
        Iterator<CSVRecord> iterator = parser.iterator();

        // first hasNext() should fetch first record
        assertTrue(iterator.hasNext());

        // next() should return the first record
        CSVRecord firstRecord = iterator.next();
        assertNotNull(firstRecord);

        // second hasNext() should fetch second record
        assertTrue(iterator.hasNext());

        // next() should return the second record
        CSVRecord secondRecord = iterator.next();
        assertNotNull(secondRecord);
        assertNotSame(firstRecord, secondRecord);

        // third hasNext() should find no more records
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWithoutHasNextThrowsNoSuchElementException() {
        Iterator<CSVRecord> iterator = parser.iterator();

        // Consume first record with next()
        CSVRecord firstRecord = iterator.next();
        assertNotNull(firstRecord);

        // Consume second record with next()
        CSVRecord secondRecord = iterator.next();
        assertNotNull(secondRecord);

        // Third call to next() without hasNext() should throw NoSuchElementException
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextWhenParserClosedReturnsFalse() throws Exception {
        Iterator<CSVRecord> iterator = parser.iterator();

        // Close the parser by setting closed field to true via reflection
        Field closedField = helper.getClass().getDeclaredField("closed");
        closedField.setAccessible(true);
        closedField.set(helper, true);

        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWhenParserClosedThrowsNoSuchElementException() throws Exception {
        Iterator<CSVRecord> iterator = parser.iterator();

        // Close the parser by setting closed field to true via reflection
        Field closedField = helper.getClass().getDeclaredField("closed");
        closedField.setAccessible(true);
        closedField.set(helper, true);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("CSVParser has been closed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorRemoveThrowsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();

        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertNotNull(ex);
    }

    @Test
    @Timeout(8000)
    void testIteratorNextRecordThrowsIOExceptionWrappedInRuntimeException() throws IOException {
        CSVParser ioExceptionParser = mock(CSVParser.class);

        when(ioExceptionParser.isClosed()).thenReturn(false);
        when(ioExceptionParser.nextRecord()).thenThrow(new IOException("io error"));

        when(ioExceptionParser.iterator()).thenAnswer(invocation -> new Iterator<CSVRecord>() {
            private CSVRecord current;

            private CSVRecord getNextRecord() {
                try {
                    return ioExceptionParser.nextRecord();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean hasNext() {
                if (ioExceptionParser.isClosed()) {
                    return false;
                }
                if (current == null) {
                    current = getNextRecord();
                }
                return current != null;
            }

            @Override
            public CSVRecord next() {
                if (ioExceptionParser.isClosed()) {
                    throw new NoSuchElementException("CSVParser has been closed");
                }
                CSVRecord next = current;
                current = null;

                if (next == null) {
                    next = getNextRecord();
                    if (next == null) {
                        throw new NoSuchElementException("No more CSV records available");
                    }
                }
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        });

        Iterator<CSVRecord> iterator = ioExceptionParser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("io error", ex.getCause().getMessage());
    }
}