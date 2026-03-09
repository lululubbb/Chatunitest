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

class CSVPrinter_66_5Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void println_appendsRecordSeparator_whenRecordSeparatorNotNull() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        printer.println();

        verify(out).append(recordSeparator);
        // Use reflection to check private field newRecord is true
        boolean newRecord = getNewRecord(printer);
        assertTrue(newRecord);
    }

    @Test
    @Timeout(8000)
    void println_doesNotAppend_whenRecordSeparatorIsNull() throws IOException {
        when(format.getRecordSeparator()).thenReturn(null);

        printer.println();

        verify(out, never()).append(anyString());
        boolean newRecord = getNewRecord(printer);
        assertTrue(newRecord);
    }

    private boolean getNewRecord(CSVPrinter printer) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            return field.getBoolean(printer);
        } catch (Exception e) {
            fail("Reflection failed to get newRecord field");
            return false;
        }
    }
}