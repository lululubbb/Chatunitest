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
import org.mockito.ArgumentMatchers;

class CSVPrinter_69_5Test {

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
    void testPrintRecords_withNullIterable() throws IOException {
        Iterable<?> values = Collections.emptyList();
        printer.printRecords(values);
        // no output expected, no exception thrown
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjects() throws IOException {
        Iterable<?> values = Arrays.asList("value1", "value2");
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(2)).printRecord(ArgumentMatchers.any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArray() throws IOException {
        Object[] arr1 = new Object[] { "a", "b" };
        Object[] arr2 = new Object[] { "c", "d" };
        Iterable<?> values = Arrays.asList(arr1, arr2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(2)).printRecord(ArgumentMatchers.<Object[]>any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterable() throws IOException {
        Iterable<String> inner1 = Arrays.asList("x", "y");
        Iterable<String> inner2 = Arrays.asList("z");
        Iterable<?> values = Arrays.asList(inner1, inner2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(2)).printRecord(ArgumentMatchers.<Iterable<?>>any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedIterable() throws IOException {
        Object[] arr = new Object[] { "a" };
        Iterable<String> inner = Arrays.asList("x");
        String str = "string";
        Iterable<?> values = Arrays.asList(arr, inner, str);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord(ArgumentMatchers.<Object[]>any());
        verify(spyPrinter, times(1)).printRecord(ArgumentMatchers.<Iterable<?>>any());
        verify(spyPrinter, times(1)).printRecord(ArgumentMatchers.eq(str));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIOExceptionFromPrintRecord() throws IOException {
        Iterable<?> values = Collections.singletonList("value");
        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("fail")).when(spyPrinter).printRecord(ArgumentMatchers.any());

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecords(values));
        assertEquals("fail", thrown.getMessage());
    }
}