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
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_14_4Test {

    private CSVParser parser;

    // We need to add fields closed and callCount to this test class
    private boolean closed;
    private int callCount;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a real CSVParser instance with null params (assuming it allows)
        parser = spy(new CSVParser(null, null));

        // Initialize closed and callCount fields in this test instance
        closed = false;
        callCount = 0;

        // Mock nextRecord() method
        doAnswer(invocation -> {
            callCount++;
            if (callCount == 1 || callCount == 2) {
                return mock(CSVRecord.class);
            } else {
                return null;
            }
        }).when(parser).nextRecord();

        // Mock isClosed() method to return the value of closed field
        doAnswer(invocation -> closed).when(parser).isClosed();
    }

    private void closeParser() throws Exception {
        this.closed = true;

        // Use reflection to set CSVParser's closed field to true if exists
        try {
            Field closedField = CSVParser.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.set(parser, true);
        } catch (NoSuchFieldException ignored) {
            // If no such field, ignore
        }
    }

    @Test
    @Timeout(8000)
    public void testIterator_hasNextAndNext_NormalIteration() {
        Iterator<CSVRecord> iterator = parser.iterator();

        // First call: hasNext() triggers nextRecord() and returns true
        assertTrue(iterator.hasNext());

        // next() returns the first record
        CSVRecord firstRecord = iterator.next();
        assertNotNull(firstRecord);

        // Second call: hasNext() triggers nextRecord() and returns true
        assertTrue(iterator.hasNext());

        // next() returns the second record
        CSVRecord secondRecord = iterator.next();
        assertNotNull(secondRecord);
        assertNotSame(firstRecord, secondRecord);

        // Third call: hasNext() triggers nextRecord() and returns false (null)
        assertFalse(iterator.hasNext());

        // next() after no more records throws NoSuchElementException
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> iterator.next());
        assertEquals("No more CSV records available", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testIterator_hasNext_WhenParserClosed() throws Exception {
        closeParser();
        Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testIterator_next_WhenParserClosed_Throws() throws Exception {
        closeParser();
        Iterator<CSVRecord> iterator = parser.iterator();
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () -> iterator.next());
        assertEquals("CSVParser has been closed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testIterator_next_WithoutHasNext_CallsNextRecord() {
        Iterator<CSVRecord> iterator = parser.iterator();
        // Do not call hasNext before next, so current is null and next() calls getNextRecord().
        CSVRecord firstRecord = iterator.next();
        assertNotNull(firstRecord);

        // The second call to next() should return the next record (cached by hasNext)
        CSVRecord secondRecord = iterator.next();
        assertNotNull(secondRecord);
    }

    @Test
    @Timeout(8000)
    public void testIterator_remove_ThrowsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    @Timeout(8000)
    public void testIterator_nextRecordThrowsIOException_WrappedInRuntimeException() throws Exception {
        CSVParser parserWithException = spy(new CSVParser(null, null));
        doThrow(new IOException("Simulated IO exception")).when(parserWithException).nextRecord();
        doAnswer(invocation -> false).when(parserWithException).isClosed();

        Iterator<CSVRecord> iterator = parserWithException.iterator();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            iterator.hasNext();
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("Simulated IO exception", thrown.getCause().getMessage());
    }
}