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
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {
    private CSVFormat format;
    private Appendable appendable;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_shouldCallClose() throws IOException, ReflectiveOperationException {
        Appendable closeableOut = mock(Appendable.class, withSettings().extraInterfaces(Closeable.class));
        printer = new CSVPrinter(closeableOut, format);

        // Use reflection to set the private final field 'out' to the mock with Closeable interface
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, closeableOut);

        printer.close();

        verify((Closeable) closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() throws IOException {
        appendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
            @Override
            public Appendable append(char c) { return this; }
        };
        printer = new CSVPrinter(appendable, format);

        assertDoesNotThrow(() -> printer.close());
    }
}