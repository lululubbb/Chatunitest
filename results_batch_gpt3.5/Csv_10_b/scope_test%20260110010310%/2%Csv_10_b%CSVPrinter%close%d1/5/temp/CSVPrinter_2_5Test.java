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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {

    static class NonCloseableAppendable implements Appendable {
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
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_shouldCallClose() throws IOException {
        Closeable closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);

        CSVPrinter printer = new CSVPrinter(new NonCloseableAppendable(), format);
        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            outField.set(printer, closeableOut);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        printer.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() throws IOException {
        Appendable nonCloseableOut = new NonCloseableAppendable();
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(nonCloseableOut, format);

        assertDoesNotThrow(printer::close);
    }
}