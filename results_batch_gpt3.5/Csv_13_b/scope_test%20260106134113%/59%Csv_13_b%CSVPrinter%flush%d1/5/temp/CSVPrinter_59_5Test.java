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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_59_5Test {

    private Appendable outMock;
    private CSVFormat formatMock;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_flushCalled() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable flushableOut = mock(Flushable.class);
        // Create CSVPrinter with Appendable mock first
        CSVPrinter printerWithFlushable = new CSVPrinter(mock(Appendable.class), formatMock);

        // Use reflection to replace the private final 'out' field with flushableOut
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printerWithFlushable, flushableOut);

        printerWithFlushable.flush();

        verify(flushableOut).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noFlushCalled() throws IOException {
        // outMock is Appendable but not Flushable
        printer.flush();
        // no exception and no flush call on outMock since it is not Flushable
        verifyNoMoreInteractions(outMock);
    }
}