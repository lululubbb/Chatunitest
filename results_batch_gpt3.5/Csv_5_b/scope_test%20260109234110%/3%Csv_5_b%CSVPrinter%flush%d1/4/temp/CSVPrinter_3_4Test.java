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

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_withFlushableOut_invokesFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Create a mock Appendable that also implements Flushable
        Appendable flushableAppendable = mock(Appendable.class, withSettings().extraInterfaces(Flushable.class));

        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), format);

        // Use reflection to set the private final field 'out' to the flushableAppendable mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier using reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, flushableAppendable);

        // Cast to Flushable to verify flush() method
        Flushable flushable = (Flushable) flushableAppendable;

        printer.flush();

        verify(flushable).flush();
    }

    @Test
    @Timeout(8000)
    void flush_withNonFlushableOut_doesNotThrow() throws IOException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = new CSVPrinter(appendable, format);

        assertDoesNotThrow(() -> printer.flush());

        verifyNoMoreInteractions(appendable);
    }
}