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
import java.lang.reflect.Constructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {

    private Closeable closeableOut;
    private CSVPrinter printerWithCloseable;
    private CSVPrinter printerWithNonCloseable;
    private Appendable nonCloseableOut;

    @BeforeEach
    void setUp() throws Exception {
        closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);
        // Create a wrapper Appendable around Closeable to satisfy constructor parameter
        Appendable closeableAppendable = new AppendableCloseableWrapper(closeableOut);
        printerWithCloseable = createCSVPrinterWithOut(closeableAppendable, format);

        nonCloseableOut = new StringBuilder();
        printerWithNonCloseable = createCSVPrinterWithOut(nonCloseableOut, format);
    }

    @Test
    @Timeout(8000)
    void close_closesOutIfCloseable() throws IOException {
        printerWithCloseable.close();
        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_doesNotThrowIfOutNotCloseable() {
        assertDoesNotThrow(() -> printerWithNonCloseable.close());
    }

    // Helper method to create CSVPrinter instance with specified out and format using reflection
    private CSVPrinter createCSVPrinterWithOut(Appendable out, CSVFormat format) throws Exception {
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        return constructor.newInstance(out, format);
    }

    // Wrapper class to adapt Closeable to Appendable
    private static class AppendableCloseableWrapper implements Appendable, Closeable {
        private final Closeable closeable;

        AppendableCloseableWrapper(Closeable closeable) {
            this.closeable = closeable;
        }

        @Override
        public Appendable append(CharSequence csq) throws IOException {
            return this;
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) throws IOException {
            return this;
        }

        @Override
        public Appendable append(char c) throws IOException {
            return this;
        }

        @Override
        public void close() throws IOException {
            closeable.close();
        }
    }
}