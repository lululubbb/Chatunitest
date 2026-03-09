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

class CSVPrinter_2_3Test {

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_shouldCloseOut() throws IOException, ReflectiveOperationException {
        Closeable closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);
        // Use a dummy Appendable that implements Closeable by proxy
        Appendable appendableOut = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) { return this; }
            @Override
            public Appendable append(CharSequence csq, int start, int end) { return this; }
            @Override
            public Appendable append(char c) { return this; }
        };
        CSVPrinter printer = new CSVPrinter(appendableOut, format);

        // Use reflection to set the private 'out' field to our Closeable mock
        java.lang.reflect.Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, closeableOut);

        printer.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() throws IOException {
        Appendable nonCloseableOut = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(nonCloseableOut, format);

        assertDoesNotThrow((Executable) printer::close);
    }
}