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
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_14_3Test {

    CSVParser csvParser;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVParser realParser = constructor.newInstance(mock(java.io.Reader.class), mock(CSVFormat.class));
        csvParser = spy(realParser);
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_and_next_withRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(csvParser).nextRecord();
        doReturn(false).when(csvParser).isClosed();

        Iterator<CSVRecord> iterator = csvParser.iterator();

        // first hasNext() loads first record
        assertTrue(iterator.hasNext());

        // next() returns first record
        assertSame(record1, iterator.next());

        // second hasNext() loads second record
        assertTrue(iterator.hasNext());

        // next() returns second record
        assertSame(record2, iterator.next());

        // third hasNext() loads null record (end)
        assertFalse(iterator.hasNext());

        // next() after end throws NoSuchElementException
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_next_without_hasNext_called() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);

        doReturn(record1).doReturn(null).when(csvParser).nextRecord();
        doReturn(false).when(csvParser).isClosed();

        Iterator<CSVRecord> iterator = csvParser.iterator();

        // Call next() without hasNext()
        assertSame(record1, iterator.next());

        // next() again throws NoSuchElementException
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_hasNext_whenClosed() {
        doReturn(true).when(csvParser).isClosed();

        Iterator<CSVRecord> iterator = csvParser.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testIterator_next_whenClosed_throws() {
        doReturn(true).when(csvParser).isClosed();

        Iterator<CSVRecord> iterator = csvParser.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = csvParser.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void testIterator_getNextRecord_throwsRuntimeException() throws IOException {
        doThrow(new IOException("io error")).when(csvParser).nextRecord();
        doReturn(false).when(csvParser).isClosed();

        Iterator<CSVRecord> iterator = csvParser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(ex.getCause() instanceof IOException);

        RuntimeException ex2 = assertThrows(RuntimeException.class, iterator::next);
        assertTrue(ex2.getCause() instanceof IOException);
    }
}