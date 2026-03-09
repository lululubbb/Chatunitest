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

class CSVPrinterFlushTest {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_flushCalled() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable flushableOut = mock(Flushable.class);

        // Create CSVPrinter with a dummy Appendable (use real Appendable implementation)
        Appendable dummyAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
            @Override
            public Appendable append(char c) { return this; }
        };
        CSVPrinter printer = new CSVPrinter(dummyAppendable, format);

        // Use reflection to set the private final 'out' field to flushableOut
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        outField.set(printer, flushableOut);

        printer.flush();

        verify(flushableOut, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noException() throws IOException {
        Appendable appendableOut = mock(Appendable.class);
        CSVPrinter printer = new CSVPrinter(appendableOut, format);

        // flush should not throw even if out is not Flushable
        assertDoesNotThrow(() -> printer.flush());
    }
}