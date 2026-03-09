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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.withSettings;

class CSVParser_iterator_Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() {
        parser = Mockito.mock(CSVParser.class, withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_and_next_normalFlow() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        when(parser.nextRecord())
            .thenReturn(record1)
            .thenReturn(record2)
            .thenReturn(null);

        when(parser.isClosed()).thenReturn(false);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertTrue(iterator.hasNext());
        assertSame(record1, iterator.next());

        assertTrue(iterator.hasNext());
        assertSame(record2, iterator.next());

        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_whenClosed_returnsFalse() {
        when(parser.isClosed()).thenReturn(true);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_whenClosed_throwsNoSuchElementException() {
        when(parser.isClosed()).thenReturn(true);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_next_withoutHasNext_calls_getNextRecord() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        when(parser.isClosed()).thenReturn(false);
        when(parser.nextRecord()).thenReturn(record);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertSame(record, iterator.next());

        when(parser.nextRecord()).thenReturn(null);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void testIterator_nextRecord_throwsIOException_wrappedInRuntimeException() throws IOException {
        when(parser.isClosed()).thenReturn(false);
        when(parser.nextRecord()).thenThrow(new IOException("IO error"));

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(ex.getCause() instanceof IOException);

        RuntimeException ex2 = assertThrows(RuntimeException.class, iterator::next);
        assertTrue(ex2.getCause() instanceof IOException);
    }
}