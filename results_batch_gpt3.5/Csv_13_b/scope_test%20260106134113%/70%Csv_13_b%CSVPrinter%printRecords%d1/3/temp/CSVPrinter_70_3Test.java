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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_70_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        // Use a real CSVFormat instance instead of mock to avoid issues
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSingleObjects_callsPrintRecordForEach() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object obj1 = "abc";
        Object obj2 = 123;

        spyPrinter.printRecords(obj1, obj2);

        verify(spyPrinter).printRecord(obj1);
        verify(spyPrinter).printRecord(obj2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray_callsPrintRecordWithArray() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object[] array = new Object[] { "a", "b" };

        spyPrinter.printRecords((Object) array);

        verify(spyPrinter).printRecord(array);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable_callsPrintRecordWithIterable() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Iterable<String> iterable = Arrays.asList("x", "y");

        spyPrinter.printRecords((Object) iterable);

        verify(spyPrinter).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedTypes_callsCorrectPrintRecordMethods() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object[] array = new Object[] { "a", "b" };
        Iterable<String> iterable = Arrays.asList("x", "y");
        String single = "single";

        spyPrinter.printRecords((Object) array, (Object) iterable, single);

        verify(spyPrinter).printRecord(array);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(single);
    }

}