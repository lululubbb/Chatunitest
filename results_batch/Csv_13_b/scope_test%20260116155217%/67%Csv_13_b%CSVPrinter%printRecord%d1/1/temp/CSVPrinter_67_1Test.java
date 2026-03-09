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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_67_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> empty = Collections.emptyList();
        printer.printRecord(empty);
        // Should print just a newline
        assertEquals(System.lineSeparator(), out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws IOException {
        Iterable<Object> values = Collections.singletonList("value1");
        CSVPrinter spyPrinter = Mockito.spy(printer);

        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        // Fix: specify the method signature to match print(Object) exactly
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(1)).print("value1");
        assertTrue(out.toString().endsWith(System.lineSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("val1", 123, null, true);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter).print("val1");
        verify(spyPrinter).print(123);
        verify(spyPrinter).print(null);
        verify(spyPrinter).print(true);
        assertTrue(out.toString().endsWith(System.lineSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndPrintln() throws Exception {
        Iterable<Object> values = Arrays.asList("a", "b");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doCallRealMethod().when(spyPrinter).printRecord(any(Iterable.class));
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(2)).print(any());
        verify(spyPrinter, times(1)).println();
    }
}