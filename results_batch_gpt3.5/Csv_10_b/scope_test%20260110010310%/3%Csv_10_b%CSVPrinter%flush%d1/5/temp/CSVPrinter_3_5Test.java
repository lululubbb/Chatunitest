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

    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_shouldInvokeFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable mockFlushable = mock(Flushable.class);

        // Create CSVPrinter with a dummy Appendable because constructor requires Appendable
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), mockFormat);

        // Use reflection to set the private final 'out' field to our Flushable mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, mockFlushable);

        printer.flush();

        verify(mockFlushable, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_shouldNotThrow() throws IOException {
        Appendable nonFlushable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
            @Override
            public Appendable append(char c) { return this; }
        };
        CSVPrinter printer = new CSVPrinter(nonFlushable, mockFormat);

        assertDoesNotThrow(() -> printer.flush());
    }
}