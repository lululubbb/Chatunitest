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

public class CSVParser_14_1Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws Exception {
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        parser = spy(constructor.newInstance(mock(java.io.Reader.class), mock(CSVFormat.class)));

        doReturn(false).when(parser).isClosed();
        doReturn(null).when(parser).nextRecord();
    }

    @Test
    @Timeout(8000)
    public void testIterator_hasNextAndNext_normalFlow() throws IOException {
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

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    public void testIterator_hasNext_whenClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    public void testIterator_next_withoutHasNext_callsGetNextRecord() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        doReturn(false).when(parser).isClosed();
        doReturn(record).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertSame(record, iterator.next());
    }

    @Test
    @Timeout(8000)
    public void testIterator_next_throwsWhenClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    public void testIterator_next_throwsWhenNoMoreRecords() throws IOException {
        doReturn(false).when(parser).isClosed();
        doReturn(null).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    public void testIterator_remove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    public void testIterator_getNextRecord_wrapsIOException() throws IOException {
        doReturn(false).when(parser).isClosed();
        doThrow(new IOException("io error")).when(parser).nextRecord();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            iterator.hasNext();
        });
        assertTrue(ex.getCause() instanceof IOException);
    }
}