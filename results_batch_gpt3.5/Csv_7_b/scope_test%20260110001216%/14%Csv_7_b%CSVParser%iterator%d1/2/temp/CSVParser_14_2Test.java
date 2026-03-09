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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParser_14_2Test {

    private CSVParser csvParser;
    private int count;

    @BeforeEach
    void setUp() throws Exception {
        csvParser = Mockito.spy(new CSVParser(null, null));

        // Use reflection to set headerMap field to an empty map to avoid NullPointerException
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(csvParser, Map.of());

        count = 0;

        doAnswer(invocation -> {
            if (count == 0) {
                count++;
                return createCSVRecord(0);
            } else if (count == 1) {
                count++;
                return createCSVRecord(1);
            } else {
                return null;
            }
        }).when(csvParser).nextRecord();
    }

    private CSVRecord createCSVRecord(int recordNumber) throws Exception {
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(csvParser);

        String[] values = new String[0];

        // CSVRecord constructor: CSVRecord(String[] values, Map<String, Integer> headerMap, String comment, long recordNumber)
        return new CSVRecord(values, headerMap, null, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNext() {
        Iterator<CSVRecord> iterator = csvParser.iterator();

        assertTrue(iterator.hasNext());
        CSVRecord record1 = iterator.next();
        assertNotNull(record1);

        assertTrue(iterator.hasNext());
        CSVRecord record2 = iterator.next();
        assertNotNull(record2);
        assertNotSame(record1, record2);

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorNextWithoutHasNext() {
        Iterator<CSVRecord> iterator = csvParser.iterator();

        CSVRecord record1 = iterator.next();
        assertNotNull(record1);

        CSVRecord record2 = iterator.next();
        assertNotNull(record2);

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorRemoveThrows() {
        Iterator<CSVRecord> iterator = csvParser.iterator();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextWhenClosed() {
        CSVParser closedParser = Mockito.spy(csvParser);
        doReturn(true).when(closedParser).isClosed();
        Iterator<CSVRecord> iterator = closedParser.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorNextThrowsRuntimeExceptionOnIOException() throws IOException {
        CSVParser errorParser = Mockito.spy(csvParser);
        doThrow(new IOException("IO error")).when(errorParser).nextRecord();
        Iterator<CSVRecord> iterator = errorParser.iterator();

        RuntimeException ex = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(ex.getCause() instanceof IOException);

        RuntimeException exNext = assertThrows(RuntimeException.class, iterator::next);
        assertTrue(exNext.getCause() instanceof IOException);
    }
}