package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_59_4Test {

    private Appendable outFlushable;
    private Appendable outNonFlushable;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_WithFlushableOut_CallsFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        outFlushable = mock(Appendable.class, withSettings().extraInterfaces(Flushable.class));
        CSVPrinter printer = new CSVPrinter(outFlushable, format);

        // Use reflection to set the private final field 'out' to our mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, outFlushable);

        printer.flush();

        verify((Flushable) outFlushable, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_WithNonFlushableOut_DoesNotThrow() throws IOException, NoSuchFieldException, IllegalAccessException {
        outNonFlushable = mock(Appendable.class);
        CSVPrinter printer = new CSVPrinter(outNonFlushable, format);

        // Use reflection to set the private final field 'out' to our mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, outNonFlushable);

        assertDoesNotThrow(() -> printer.flush());
    }
}