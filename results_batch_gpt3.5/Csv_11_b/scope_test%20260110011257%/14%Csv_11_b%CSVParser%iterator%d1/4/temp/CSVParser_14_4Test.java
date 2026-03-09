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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVParserIteratorTest {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        // Create a spy of CSVParser with a dummy Reader and CSVFormat to allow stubbing nextRecord and isClosed
        parser = Mockito.spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));

        // Use reflection to set private fields that may cause issues during iteration
        // For example, headerMap or lexer might be null and cause NullPointerException
        // Setting headerMap to empty map to avoid NPE
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        headerMapField.set(parser, java.util.Collections.emptyMap());

        // Setting lexer field to a non-null mock to avoid NullPointerException during iteration
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, mock(Lexer.class));
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNext_NormalFlow() throws IOException {
        // Arrange: stub isClosed() to false initially, then true after all records returned
        when(parser.isClosed()).thenReturn(false, false, true);

        // Stub nextRecord() to return two records then null
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);
        when(parser.nextRecord()).thenReturn(record1, record2, (CSVRecord) null);

        Iterator<CSVRecord> iterator = parser.iterator();

        // Act & Assert
        assertTrue(iterator.hasNext());
        assertSame(record1, iterator.next());

        assertTrue(iterator.hasNext());
        assertSame(record2, iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNext_WhenParserClosed() {
        when(parser.isClosed()).thenReturn(true);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_WithoutCallingHasNext() throws IOException {
        when(parser.isClosed()).thenReturn(false, false, true);
        CSVRecord record = mock(CSVRecord.class);
        when(parser.nextRecord()).thenReturn(record, (CSVRecord) null);

        Iterator<CSVRecord> iterator = parser.iterator();

        // Directly call next() without hasNext()
        assertSame(record, iterator.next());
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_ThrowsNoSuchElementExceptionWhenNoMoreRecords() throws IOException {
        when(parser.isClosed()).thenReturn(false, false, true);
        when(parser.nextRecord()).thenReturn((CSVRecord) null);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_ThrowsNoSuchElementExceptionWhenClosed() {
        when(parser.isClosed()).thenReturn(true);

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void testIteratorRemoveUnsupported() {
        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_ThrowsRuntimeExceptionOnIOException() throws IOException {
        when(parser.isClosed()).thenReturn(false, true);
        when(parser.nextRecord()).thenThrow(new IOException("IO failure"));

        Iterator<CSVRecord> iterator = parser.iterator();

        // The IOException in getNextRecord() should be wrapped into RuntimeException when next() or hasNext() calls it
        RuntimeException thrown = assertThrows(RuntimeException.class, iterator::hasNext);
        assertTrue(thrown.getCause() instanceof IOException);

        // Also test next() triggers the same RuntimeException if current is null
        when(parser.isClosed()).thenReturn(false, true);
        when(parser.nextRecord()).thenThrow(new IOException("IO failure"));

        Iterator<CSVRecord> iterator2 = parser.iterator();
        RuntimeException thrown2 = assertThrows(RuntimeException.class, iterator2::next);
        assertTrue(thrown2.getCause() instanceof IOException);
    }
}