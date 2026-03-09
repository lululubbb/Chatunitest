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
import org.junit.jupiter.api.function.Executable;

class CSVParserIteratorTest {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws IOException {
        parser = spy(new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class)));
        // Reset internal 'closed' state to false using reflection
        try {
            Field closedField = CSVParser.class.getDeclaredField("closed");
            closedField.setAccessible(true);
            closedField.setBoolean(parser, false);
        } catch (NoSuchFieldException ignored) {
            // If field 'closed' does not exist, ignore
        } catch (IllegalAccessException ignored) {
        }
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNextAndNext_withRecords() throws IOException {
        CSVRecord record1 = mock(CSVRecord.class);
        CSVRecord record2 = mock(CSVRecord.class);

        doReturn(record1).doReturn(record2).doReturn(null).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // first hasNext() should fetch record1
        assertTrue(iterator.hasNext());
        // next() should return record1
        assertSame(record1, iterator.next());

        // second hasNext() should fetch record2
        assertTrue(iterator.hasNext());
        // next() should return record2
        assertSame(record2, iterator.next());

        // third hasNext() should fetch null and return false
        assertFalse(iterator.hasNext());

        // next() after no more records should throw NoSuchElementException
        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() {
                iterator.next();
            }
        });
    }

    @Test
    @Timeout(8000)
    void testIteratorHasNext_whenParserClosed() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() {
                iterator.next();
            }
        });
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_withoutCallingHasNext_firstCall() throws IOException {
        CSVRecord record = mock(CSVRecord.class);

        doReturn(record).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        // Call next() without hasNext() first, should still return record
        assertSame(record, iterator.next());

        // Next call to next() with no more records should throw
        doReturn(null).when(parser).nextRecord();
        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() {
                iterator.next();
            }
        });
    }

    @Test
    @Timeout(8000)
    void testIteratorNext_whenParserClosed_throws() {
        doReturn(true).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() {
                iterator.next();
            }
        });
    }

    @Test
    @Timeout(8000)
    void testIteratorRemove_throwsUnsupportedOperationException() {
        Iterator<CSVRecord> iterator = parser.iterator();

        assertThrows(UnsupportedOperationException.class, new Executable() {
            @Override
            public void execute() {
                iterator.remove();
            }
        });
    }

    @Test
    @Timeout(8000)
    void testIterator_getNextRecord_throwsRuntimeExceptionOnIOException() throws IOException {
        doThrow(new IOException("io error")).when(parser).nextRecord();
        doReturn(false).when(parser).isClosed();

        Iterator<CSVRecord> iterator = parser.iterator();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            iterator.hasNext();
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("io error", thrown.getCause().getMessage());
    }
}