package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_67_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        // Use real CSVFormat instance instead of mock to avoid constructor issues
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNonEmptyIterable_callsPrintAndPrintln() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(3)).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable_onlyCallsPrintln() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withIterable_printThrowsIOException() throws IOException {
        Iterable<Object> values = Arrays.asList("val");

        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("print failed")).when(spyPrinter).print(any());

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecord(values));
        assertEquals("print failed", thrown.getMessage());
    }
}