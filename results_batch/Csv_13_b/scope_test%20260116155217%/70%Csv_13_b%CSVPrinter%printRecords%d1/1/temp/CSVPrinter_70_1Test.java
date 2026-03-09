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
import org.mockito.Mockito;

class CSVPrinter_70_1Test {

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
    void testPrintRecords_withSingleObjects() throws IOException {
        // Single values that are not arrays or Iterable
        Object val1 = "value1";
        Object val2 = 123;

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(val1, val2);

        verify(spyPrinter).printRecord(val1);
        verify(spyPrinter).printRecord(val2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArrays() throws IOException {
        Object[] arr1 = new Object[] {"a", "b"};
        Object[] arr2 = new Object[] {1, 2};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords((Object) arr1, (Object) arr2);

        verify(spyPrinter).printRecord(arr1);
        verify(spyPrinter).printRecord(arr2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        Iterable<String> iterable1 = Arrays.asList("x", "y");
        Iterable<Integer> iterable2 = Collections.singletonList(5);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(iterable1, iterable2);

        verify(spyPrinter).printRecord(iterable1);
        verify(spyPrinter).printRecord(iterable2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_mixedTypes() throws IOException {
        Object[] arr = new Object[] {"arr"};
        Iterable<String> iterable = Collections.singletonList("iter");
        String val = "val";

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(arr, iterable, val);

        verify(spyPrinter).printRecord(arr);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(val);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_empty() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords();

        // Fix ambiguity by specifying the exact method printRecord(Object...) with empty varargs
        verify(spyPrinter, never()).printRecord((Object[]) any());
    }
}