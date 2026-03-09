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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinterCloseTest {

    private static class CloseableAppendable implements Appendable, Closeable {
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
        @Override
        public void close() throws IOException {
            // no-op
        }
    }

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_callsClose() throws Exception {
        CloseableAppendable closeableOut = mock(CloseableAppendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(closeableOut, format);

        setFinalField(printer, "out", closeableOut);

        printer.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_doesNotThrow() throws IOException {
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

        assertDoesNotThrow((Executable) printer::close);
    }

    @Test
    @Timeout(8000)
    void close_whenCloseThrowsIOException_propagatesException() throws Exception {
        CloseableAppendable closeableOut = mock(CloseableAppendable.class);
        doThrow(new IOException("close failed")).when(closeableOut).close();
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(closeableOut, format);

        setFinalField(printer, "out", closeableOut);

        IOException thrown = assertThrows(IOException.class, printer::close);
        assertEquals("close failed", thrown.getMessage());
    }
}