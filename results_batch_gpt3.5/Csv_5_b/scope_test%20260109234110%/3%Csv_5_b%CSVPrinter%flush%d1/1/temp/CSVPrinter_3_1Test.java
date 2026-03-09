package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_3_1Test {

    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_invokesFlush() throws IOException, ReflectiveOperationException {
        Flushable flushableOut = mock(Flushable.class);
        // Construct CSVPrinter with a dummy Appendable, then replace 'out' with flushableOut via reflection
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), mockFormat);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, flushableOut);

        printer.flush();

        verify(flushableOut, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_doesNotThrow() throws IOException {
        Appendable nonFlushableOut = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(nonFlushableOut, mockFormat);

        // should not throw any exception
        assertDoesNotThrow(() -> printer.flush());
    }
}