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
import org.mockito.MockSettings;

class CSVPrinterFlushTest {

    private CSVFormat format;
    private Appendable appendable;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_WithFlushableOut_ShouldInvokeFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Create a mock Appendable that also implements Flushable
        MockSettings settings = withSettings().extraInterfaces(Flushable.class);
        Appendable appendableFlushable = mock(Appendable.class, settings);

        // Create CSVPrinter with a dummy Appendable and format (will replace 'out' via reflection)
        printer = new CSVPrinter(mock(Appendable.class), format);

        // Use reflection to set the private final field 'out' to our mock to ensure the flush call is intercepted
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier if present (necessary to modify final field)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, appendableFlushable);

        // Cast to Flushable for verification
        Flushable flushable = (Flushable) appendableFlushable;

        // Setup the flush method on the Flushable interface
        doNothing().when(flushable).flush();

        // Call flush
        printer.flush();

        // Verify flush was called exactly once
        verify(flushable, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_WithNonFlushableOut_ShouldNotThrow() throws IOException {
        appendable = mock(Appendable.class);
        printer = new CSVPrinter(appendable, format);
        // flush should do nothing and not throw
        printer.flush();
    }
}