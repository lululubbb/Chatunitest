package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

class CSVPrinter_2_6Test {

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_shouldCloseOut() throws IOException, ReflectiveOperationException {
        // Mock Appendable that also implements Closeable
        Appendable closeableOut = mock(Appendable.class, withSettings().extraInterfaces(Closeable.class));
        Closeable closeable = (Closeable) closeableOut;
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        // Use reflection to set the private final field 'out' to our mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, closeableOut);

        printer.close();

        verify(closeable, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() throws Exception {
        Appendable nonCloseableOut = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) {
                return this;
            }
            @Override
            public Appendable append(CharSequence csq, int start, int end) {
                return this;
            }
            @Override
            public Appendable append(char c) {
                return this;
            }
        };
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(nonCloseableOut, format);

        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                printer.close();
            }
        });
    }

    @Test
    @Timeout(8000)
    void close_whenCloseThrowsIOException_shouldThrow() throws IOException, ReflectiveOperationException {
        // Mock Appendable that also implements Closeable
        Appendable closeableOut = mock(Appendable.class, withSettings().extraInterfaces(Closeable.class));
        Closeable closeable = (Closeable) closeableOut;
        doThrow(new IOException("close failed")).when(closeable).close();
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        // Use reflection to set the private final field 'out' to our mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, closeableOut);

        IOException thrown = assertThrows(IOException.class, printer::close);
        assertEquals("close failed", thrown.getMessage());
    }
}