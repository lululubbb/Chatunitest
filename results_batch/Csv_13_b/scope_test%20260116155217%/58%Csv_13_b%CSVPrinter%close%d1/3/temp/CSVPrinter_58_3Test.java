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

    private Appendable mockAppendable;
    private CSVFormat mockFormat;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void close_withCloseableAppendable_callsClose() throws IOException, NoSuchFieldException, IllegalAccessException {
        Closeable closeableAppendable = mock(Closeable.class);
        // Create CSVPrinter with a dummy Appendable since constructor requires Appendable
        printer = new CSVPrinter(mock(Appendable.class), mockFormat);

        // Use reflection to set the private final 'out' field to our Closeable mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, closeableAppendable);

        printer.close();

        verify(closeableAppendable, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_withNonCloseableAppendable_doesNotThrow() throws IOException {
        mockAppendable = mock(Appendable.class);
        // Appendable is not Closeable here
        printer = new CSVPrinter(mockAppendable, mockFormat);

        assertDoesNotThrow(() -> printer.close());
    }
}