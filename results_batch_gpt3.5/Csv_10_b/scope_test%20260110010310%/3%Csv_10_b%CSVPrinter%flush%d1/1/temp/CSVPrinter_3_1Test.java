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
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_flushIsCalled() throws IOException, NoSuchFieldException, IllegalAccessException {
        flushableOut = mock(Flushable.class);
        // Create CSVPrinter with a dummy Appendable
        Appendable dummyOut = mock(Appendable.class);
        printer = new CSVPrinter(dummyOut, format);

        // Use reflection to set the private final 'out' field to flushableOut
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier from 'out' field to allow reassignment
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, flushableOut);

        printer.flush();

        verify(flushableOut, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noExceptionThrown() throws IOException {
        Appendable nonFlushableOut = mock(Appendable.class);
        printer = new CSVPrinter(nonFlushableOut, format);

        // Should not throw any exception
        assertDoesNotThrow(() -> printer.flush());
    }
}