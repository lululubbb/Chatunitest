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

class CSVParserIteratorTest {

    CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Create real CSVParser instance with dummy Reader and CSVFormat
        parser = new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class));
        // Spy on the real parser instance
        parser = spy(parser);

        // Use reflection to set private field 'closed' to false if exists (avoid isClosed issues)
        // If there is no such field, skip this step
        try {
            Field closedField = CSVParser.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.set(parser, false);
        } catch (NoSuchFieldException ignored) {
            // Field 'closed' does not exist, ignore
        }
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_and_next_normalFlow() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(false).when(parser).isClosed();
        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();

        Iterator<CSVRecord> it = parser.iterator();

        // First hasNext call, fetches record1
        assertTrue(it.hasNext());
        // next returns record1
        assertSame(record1, it.next());

        // Second hasNext call, fetches record2
        assertTrue(it.hasNext());
        // next returns record2
        assertSame(record2, it.next());

        // Third hasNext call, fetches null, no more records
        assertFalse(it.hasNext());
        // next should throw NoSuchElementException
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("No more CSV records available", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_closedParser() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> it = parser.iterator();

        assertFalse(it.hasNext());
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("CSVParser has been closed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_without_hasNext() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        doReturn(false).when(parser).isClosed();
        doReturn(record).when(parser).nextRecord();

        Iterator<CSVRecord> it = parser.iterator();

        // Call next without hasNext, should fetch record
        assertSame(record, it.next());

        // Next call to next should try to fetch next record and get null, throw exception
        doReturn(null).when(parser).nextRecord();
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, it::next);
        assertEquals("No more CSV records available", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testIterator_remove_unsupportedOperation() {
        Iterator<CSVRecord> it = parser.iterator();

        UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, it::remove);
        assertNotNull(thrown);
    }

    @Test
    @Timeout(8000)
    void testIterator_nextRecord_throwsIOException_wrappedInRuntimeException() throws IOException {
        doReturn(false).when(parser).isClosed();
        doThrow(new IOException("io-exception")).when(parser).nextRecord();

        Iterator<CSVRecord> it = parser.iterator();

        RuntimeException thrown = assertThrows(RuntimeException.class, it::hasNext);
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("io-exception", thrown.getCause().getMessage());
    }
}