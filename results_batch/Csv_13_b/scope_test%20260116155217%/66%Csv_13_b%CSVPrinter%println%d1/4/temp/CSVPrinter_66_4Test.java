package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_66_4Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        mocks = MockitoAnnotations.initMocks(this);
        printer = new CSVPrinter(out, format);
        // Set newRecord to false before each test to verify it becomes true after println
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.setBoolean(printer, false);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void println_AppendsRecordSeparator_WhenRecordSeparatorIsNotNull() throws IOException, NoSuchFieldException, IllegalAccessException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        printer.println();

        verify(out).append(recordSeparator);
        // Use reflection to check private field newRecord is true
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        assertTrue(field.getBoolean(printer));
    }

    @Test
    @Timeout(8000)
    void println_DoesNotAppend_WhenRecordSeparatorIsNull() throws IOException, NoSuchFieldException, IllegalAccessException {
        when(format.getRecordSeparator()).thenReturn(null);

        printer.println();

        verify(out, never()).append(anyString());
        // Use reflection to check private field newRecord is true
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        assertTrue(field.getBoolean(printer));
    }

    @Test
    @Timeout(8000)
    void println_ThrowsIOException_WhenAppendThrows() throws IOException, NoSuchFieldException, IllegalAccessException {
        String recordSeparator = "\r\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);
        doThrow(new IOException("append failed")).when(out).append(recordSeparator);

        IOException thrown = assertThrows(IOException.class, () -> printer.println());
        assertEquals("append failed", thrown.getMessage());
        // newRecord should be true even if append throws, verify it
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        assertTrue(field.getBoolean(printer));
    }
}