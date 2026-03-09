package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {

    private Appendable appendable;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        appendable = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(appendable, format);
    }

    @Test
    @Timeout(8000)
    void close_closesOutWhenCloseable() throws Exception {
        Closeable closeableOut = mock(Closeable.class);
        setPrivateField(printer, "out", closeableOut);

        printer.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_doesNothingWhenOutNotCloseable() throws Exception {
        // Create a mock Appendable that does NOT implement Closeable
        Appendable nonCloseableOut = mock(Appendable.class);
        setPrivateField(printer, "out", nonCloseableOut);

        // Should not throw any exception
        assertDoesNotThrow(() -> printer.close());

        // No close method to verify on nonCloseableOut
        verifyNoMoreInteractions(nonCloseableOut);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}