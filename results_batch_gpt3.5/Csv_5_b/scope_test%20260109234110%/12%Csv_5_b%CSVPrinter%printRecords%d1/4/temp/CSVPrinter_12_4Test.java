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

class CSVPrinter_12_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingObjectArray() throws Exception {
        Object[] record1 = new Object[]{"a", "b"};
        Object[] record2 = new Object[]{"c", "d"};
        Iterable<Object[]> records = Arrays.asList(record1, record2);

        // Spy on printer to verify printRecord calls
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(records);

        verify(spyPrinter, times(1)).printRecord(record1);
        verify(spyPrinter, times(1)).printRecord(record2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterable() throws Exception {
        Iterable<String> innerIterable1 = Arrays.asList("x", "y");
        Iterable<String> innerIterable2 = Arrays.asList("z");

        Iterable<Iterable<String>> records = Arrays.asList(innerIterable1, innerIterable2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(records);

        verify(spyPrinter, times(1)).printRecord(innerIterable1);
        verify(spyPrinter, times(1)).printRecord(innerIterable2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingSingleObjects() throws Exception {
        Iterable<Object> records = Arrays.asList("val1", 123, 45.6);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(records);

        verify(spyPrinter, times(1)).printRecord("val1");
        verify(spyPrinter, times(1)).printRecord(123);
        verify(spyPrinter, times(1)).printRecord(45.6);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> empty = Collections.emptyList();

        // Should not throw or call printRecord
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(empty);

        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_invokingPrivatePrintRecordsObjectArray() throws Exception {
        Object[] records = new Object[]{"a", "b"};

        Method printRecordsObjectArray = CSVPrinter.class.getDeclaredMethod("printRecords", Object[].class);
        printRecordsObjectArray.setAccessible(true);

        // Spy on printer to verify printRecord(Object...) is called
        CSVPrinter spyPrinter = spy(printer);

        // When invoking varargs method with reflection, wrap array in Object to avoid varargs expansion
        printRecordsObjectArray.invoke(spyPrinter, (Object) records);

        verify(spyPrinter, times(1)).printRecord(records);
    }

}