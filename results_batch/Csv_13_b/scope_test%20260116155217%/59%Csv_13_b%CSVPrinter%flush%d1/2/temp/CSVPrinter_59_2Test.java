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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVPrinterFlushTest {

    private CSVPrinter printer;
    private Appendable appendableMock;
    private CSVFormat formatMock;

    @BeforeEach
    public void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(appendableMock, formatMock);
    }

    @Test
    @Timeout(8000)
    public void testFlush_withFlushableOut_flushCalled() throws Exception {
        Flushable flushableMock = mock(Flushable.class);
        AppendableFlushableAdapter adapter = new AppendableFlushableAdapter(flushableMock);
        setOutField(printer, adapter);

        printer.flush();

        verify(flushableMock, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    public void testFlush_withNonFlushableOut_noException() throws IOException {
        // out is Appendable but not Flushable
        printer.flush();
        // no exception thrown and no flush call possible
    }

    private void setOutField(CSVPrinter printer, Appendable newOut) throws Exception {
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, newOut);
    }

    /**
     * Adapter class to wrap a Flushable as an Appendable.
     */
    private static class AppendableFlushableAdapter implements Appendable, Flushable {
        private final Flushable flushable;

        AppendableFlushableAdapter(Flushable flushable) {
            this.flushable = flushable;
        }

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
        public void flush() throws IOException {
            flushable.flush();
        }
    }
}