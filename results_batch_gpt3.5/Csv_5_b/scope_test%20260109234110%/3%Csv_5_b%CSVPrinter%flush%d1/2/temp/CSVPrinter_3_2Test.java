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
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterFlushTest {

    private CSVFormat format;
    private Flushable flushableOut;
    private Appendable nonFlushableOut;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
        flushableOut = mock(Flushable.class);
        nonFlushableOut = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void flush_WithFlushableOut_CallsFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVPrinter printer = new CSVPrinter(nonFlushableOut, format);

        // Use reflection to replace private final 'out' field with flushableOut
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, flushableOut);

        printer.flush();

        verify(flushableOut, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_WithNonFlushableOut_DoesNotThrow() throws IOException {
        CSVPrinter printer = new CSVPrinter(nonFlushableOut, format);

        // Should not throw even if out is not Flushable
        assertDoesNotThrow(() -> printer.flush());
    }
}