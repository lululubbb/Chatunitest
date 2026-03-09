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

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_flushCalled() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable flushableOut = mock(Flushable.class);
        CSVPrinter printerFlushable = new CSVPrinter(mock(Appendable.class), format);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        outField.set(printerFlushable, flushableOut);

        printerFlushable.flush();

        verify(flushableOut).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noException() {
        assertDoesNotThrow(() -> printer.flush());
    }
}