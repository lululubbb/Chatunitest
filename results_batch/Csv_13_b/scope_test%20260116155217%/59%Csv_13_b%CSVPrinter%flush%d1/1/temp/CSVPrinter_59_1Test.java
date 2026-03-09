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
    void flush_whenOutIsFlushable_flushCalled() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable flushable = mock(Flushable.class);
        CSVPrinter printerWithFlushable = new CSVPrinter(mock(Appendable.class), format);

        // Use reflection to set the private final field 'out' to our flushable mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier via reflection (necessary for final fields)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printerWithFlushable, flushable);

        printerWithFlushable.flush();

        verify(flushable, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noException() throws IOException {
        // appendable is not Flushable
        printer.flush();
        // no exception thrown, no flush call possible
    }
}