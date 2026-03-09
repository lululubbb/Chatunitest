package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.io.Flushable;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;

class CSVPrinterFlushTest {

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_flushCalled() throws IOException, ReflectiveOperationException {
        Flushable flushable = mock(Flushable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);
        java.lang.reflect.Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, flushable);

        printer.flush();

        verify(flushable, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noException() throws IOException {
        Appendable appendable = new Appendable() {
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
        CSVPrinter printer = new CSVPrinter(appendable, format);

        assertDoesNotThrow(() -> printer.flush());
    }

    @Test
    @Timeout(8000)
    void flush_whenFlushThrowsIOException_exceptionPropagated() throws IOException, ReflectiveOperationException {
        Flushable flushable = mock(Flushable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);
        java.lang.reflect.Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, flushable);
        doThrow(new IOException("flush failed")).when(flushable).flush();

        IOException thrown = null;
        try {
            printer.flush();
        } catch (IOException e) {
            thrown = e;
        }

        assertNotNull(thrown);
        assertEquals("flush failed", thrown.getMessage());
    }
}