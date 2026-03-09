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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_69_1Test {

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
    void testPrintRecords_withIterableOfObjectArrays() throws Exception {
        Object[] record1 = new Object[] { "a", "b" };
        Object[] record2 = new Object[] { "c", "d" };
        Iterable<Object[]> iterable = Arrays.asList(record1, record2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        // Use reflection to get the printRecord(Object[]) method for verification
        Method printRecordArrayMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Object[].class);
        printRecordArrayMethod.setAccessible(true);

        // Verify that printRecord(Object[]) was called twice
        verify(spyPrinter, times(2)).printRecord((Object[]) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables() throws Exception {
        Iterable<String> innerIterable1 = Arrays.asList("x", "y");
        Iterable<String> innerIterable2 = Arrays.asList("z");
        Iterable<Iterable<String>> iterable = Arrays.asList(innerIterable1, innerIterable2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        // Use reflection to get the printRecord(Iterable<?>) method for verification
        Method printRecordIterableMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Iterable.class);
        printRecordIterableMethod.setAccessible(true);

        verify(spyPrinter, times(2)).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfSingleObjects() throws Exception {
        Iterable<String> iterable = Arrays.asList("single1", "single2");

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        // Use reflection to get the printRecord(Object) method for verification
        Method printRecordObjectMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Object.class);
        printRecordObjectMethod.setAccessible(true);

        verify(spyPrinter, times(2)).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<?> emptyIterable = Collections.emptyList();

        // Should not throw or print anything
        printer.printRecords(emptyIterable);

        // out should remain empty
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNullValueInIterable() throws Exception {
        Iterable<Object> iterable = Arrays.asList((Object) null);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        // Use reflection to get the printRecord(Object) method for verification
        Method printRecordObjectMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Object.class);
        printRecordObjectMethod.setAccessible(true);

        verify(spyPrinter, times(1)).printRecord((Object) isNull());
    }
}