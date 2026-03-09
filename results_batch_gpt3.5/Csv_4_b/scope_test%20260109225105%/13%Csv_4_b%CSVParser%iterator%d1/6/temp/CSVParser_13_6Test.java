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

    @BeforeEach
    void setUp() throws Exception {
        // Use a real CSVFormat instance instead of mock to avoid constructor issues
        CSVFormat format = CSVFormat.DEFAULT;
        // Use a dummy Reader instance to avoid null pointer in constructor
        parser = spy(new CSVParser(new java.io.StringReader(""), format));

        // Use reflection to set the private 'closed' flag to false if exists (to avoid isClosed issues)
        try {
            Field closedField = CSVParser.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.set(parser, false);
        } catch (NoSuchFieldException ignored) {
            // If no such field, ignore
        }
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNext_NormalFlow() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertTrue(iterator.hasNext());
        assertSame(record1, iterator.next());

        assertTrue(iterator.hasNext());
        assertSame(record2, iterator.next());

        assertFalse(iterator.hasNext());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWithoutHasNext() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        doReturn(record).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertSame(record, iterator.next());

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("No more CSV records available", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextWhenClosed() throws IOException {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWhenClosed() throws IOException {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, iterator::next);
        assertEquals("CSVParser has been closed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorRemoveUnsupportedOperation() {
        Iterator<CSVRecord> iterator = parser.iterator();
        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertNull(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIteratorGetNextRecordThrowsIOException() throws IOException {
        doThrow(new IOException("IO failure")).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException thrown = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO failure", thrown.getCause().getMessage());
    }
}