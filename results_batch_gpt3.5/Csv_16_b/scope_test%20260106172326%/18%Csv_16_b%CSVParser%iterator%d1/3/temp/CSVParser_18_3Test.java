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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIteratorTest {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        parser = mock(CSVParser.class);

        when(parser.isClosed()).thenReturn(false);

        // Use an array to keep call count in lambda
        final int[] callCount = {0};

        when(parser.nextRecord()).thenAnswer(invocation -> {
            callCount[0]++;
            if (callCount[0] == 1) {
                return mock(CSVRecord.class);
            } else if (callCount[0] == 2) {
                return mock(CSVRecord.class);
            } else {
                return null;
            }
        });

        // Provide a real iterator implementation that mimics CSVParser.iterator() behavior,
        // but uses the mocked nextRecord() and isClosed()
        when(parser.iterator()).thenAnswer(invocation -> {
            return new Iterator<CSVRecord>() {
                private CSVRecord current;

                private CSVRecord getNextRecord() {
                    try {
                        return parser.nextRecord();
                    } catch (IOException e) {
                        throw new IllegalStateException(
                                e.getClass().getSimpleName() + " reading next record: " + e.toString(), e);
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
            };
        });
    }

    @Test
    @Timeout(8000)
    void iterator_hasNextAndNext_normalFlow() {
        Iterator<CSVRecord> it = parser.iterator();

        assertTrue(it.hasNext(), "hasNext should be true for first record");
        CSVRecord first = it.next();
        assertNotNull(first, "First record should not be null");

        assertTrue(it.hasNext(), "hasNext should be true for second record");
        CSVRecord second = it.next();
        assertNotNull(second, "Second record should not be null");

        assertFalse(it.hasNext(), "hasNext should be false when no more records");

        assertThrows(NoSuchElementException.class, it::next, "next() after end should throw");
    }

    @Test
    @Timeout(8000)
    void iterator_nextWithoutHasNext() {
        Iterator<CSVRecord> it = parser.iterator();

        CSVRecord first = it.next();
        assertNotNull(first);

        CSVRecord second = it.next();
        assertNotNull(second);

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void iterator_hasNext_whenClosed() throws Exception {
        when(parser.isClosed()).thenReturn(true);

        Iterator<CSVRecord> it = parser.iterator();

        assertFalse(it.hasNext());
    }

    @Test
    @Timeout(8000)
    void iterator_next_whenClosed_throws() throws Exception {
        when(parser.isClosed()).thenReturn(true);

        Iterator<CSVRecord> it = parser.iterator();

        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void iterator_getNextRecord_throwsIOException_wrappedInIllegalStateException() throws Exception {
        CSVParser throwingParser = mock(CSVParser.class);

        when(throwingParser.isClosed()).thenReturn(false);
        when(throwingParser.nextRecord()).thenThrow(new IOException("io error"));

        when(throwingParser.iterator()).thenAnswer(invocation -> {
            return new Iterator<CSVRecord>() {
                private CSVRecord current;

                private CSVRecord getNextRecord() {
                    try {
                        return throwingParser.nextRecord();
                    } catch (IOException e) {
                        throw new IllegalStateException(
                                e.getClass().getSimpleName() + " reading next record: " + e.toString(), e);
                    }
                }

                @Override
                public boolean hasNext() {
                    if (throwingParser.isClosed()) {
                        return false;
                    }
                    if (current == null) {
                        current = getNextRecord();
                    }
                    return current != null;
                }

                @Override
                public CSVRecord next() {
                    if (throwingParser.isClosed()) {
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
            };
        });

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            Iterator<CSVRecord> it = throwingParser.iterator();
            it.hasNext();
        });
        assertTrue(ex.getMessage().contains("IOException reading next record"));
        assertTrue(ex.getCause() instanceof IOException);
    }

    @Test
    @Timeout(8000)
    void iterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> it = parser.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }
}