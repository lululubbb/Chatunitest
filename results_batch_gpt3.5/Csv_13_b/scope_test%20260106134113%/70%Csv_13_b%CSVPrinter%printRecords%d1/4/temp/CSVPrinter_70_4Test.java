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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_70_4Test {

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
    void testPrintRecords_withSingleObject_callsPrintRecordObject() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object value = "singleValue";

        spyPrinter.printRecords(value);

        verify(spyPrinter).printRecord(value);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray_callsPrintRecordObjectArray() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object[] array = new Object[] { "a", "b" };

        spyPrinter.printRecords((Object) array);

        verify(spyPrinter).printRecord(array);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable_callsPrintRecordIterable() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Iterable<String> iterable = Arrays.asList("a", "b");

        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMultipleMixedValues() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object[] array = new Object[] { "a", "b" };
        Iterable<String> iterable = Arrays.asList("c", "d");
        String single = "e";

        spyPrinter.printRecords(array, iterable, single);

        verify(spyPrinter).printRecord(array);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(single);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyValues() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords();

        // Disambiguate the overloaded method by using Mockito's any() with cast
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }
}