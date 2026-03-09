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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_iterator_Test {

    CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        parser = Mockito.spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));
        // Use reflection to set the private 'closed' field if exists (to avoid side effects)
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
    void iterator_hasNext_and_next_normalFlow() throws IOException {
        // Arrange: prepare nextRecord to return a CSVRecord then null
        CSVRecord rec1 = mock(CSVRecord.class);
        doReturn(rec1).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // Act & Assert
        assertTrue(iterator.hasNext());
        assertSame(rec1, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void iterator_hasNext_returnsFalse_whenParserClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void iterator_next_throwsNoSuchElementException_whenClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void iterator_next_calls_getNextRecord_whenCurrentNull() throws IOException {
        CSVRecord rec1 = mock(CSVRecord.class);
        doReturn(false).when(parser).isClosed();
        doReturn(rec1).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        // current is null initially so next() calls getNextRecord internally
        assertSame(rec1, iterator.next());
    }

    @Test
    @Timeout(8000)
    void iterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void iterator_next_throwsRuntimeException_whenNextRecordThrowsIOException() throws IOException {
        doReturn(false).when(parser).isClosed();
        doThrow(new IOException("io")).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::next);
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("io", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    void iterator_hasNext_throwsRuntimeException_whenNextRecordThrowsIOException() throws IOException {
        doReturn(false).when(parser).isClosed();
        // To simulate IOException thrown on nextRecord(), we need to ensure current is null to trigger getNextRecord call
        doThrow(new IOException("io")).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("io", ex.getCause().getMessage());
    }
}