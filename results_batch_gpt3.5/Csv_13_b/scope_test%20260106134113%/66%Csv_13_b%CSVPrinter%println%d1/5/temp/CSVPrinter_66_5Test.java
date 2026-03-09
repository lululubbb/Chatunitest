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
    void println_shouldAppendRecordSeparator_whenRecordSeparatorIsNotNull() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        printer.println();

        verify(out).append(recordSeparator);
    }

    @Test
    @Timeout(8000)
    void println_shouldNotAppend_whenRecordSeparatorIsNull() throws IOException {
        when(format.getRecordSeparator()).thenReturn(null);

        printer.println();

        verify(out, never()).append(anyString());
    }

    @Test
    @Timeout(8000)
    void println_shouldSetNewRecordTrue() throws Exception {
        // Use reflection to access private field newRecord
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);

        // Set newRecord to false initially
        newRecordField.set(printer, false);

        printer.println();

        // Assert newRecord is true after println
        boolean newRecordValue = (boolean) newRecordField.get(printer);
        assert(newRecordValue);
    }
}