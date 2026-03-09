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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_12_3Test {

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
    void testPrintRecords_withIterableOfObjects() throws Exception {
        Iterable<Object> iterable = Arrays.asList("one", "two", "three");
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        Method printRecordObjectMethod = CSVPrinter.class.getMethod("printRecord", Object.class);
        verify(spyPrinter, times(3)).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingObjectArrays() throws Exception {
        Iterable<Object> iterable = Arrays.asList(new Object[] {"a", "b"}, new Object[] {"c", "d"});
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(2)).printRecord(any(Object[].class));
        verify(spyPrinter, never()).printRecord(any(Iterable.class));
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterables() throws IOException {
        Iterable<Object> innerIterable1 = Arrays.asList("x", "y");
        Iterable<Object> innerIterable2 = Collections.singletonList("z");
        Iterable<Object> iterable = Arrays.asList(innerIterable1, innerIterable2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(2)).printRecord(any(Iterable.class));
        verify(spyPrinter, never()).printRecord(any(Object[].class));
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingMixedTypes() throws IOException {
        Object[] objArray = new Object[] {"arr1", "arr2"};
        Iterable<Object> iterable = Arrays.asList(objArray, Arrays.asList("inner1"), "singleValue");

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(objArray);
        verify(spyPrinter).printRecord(any(Iterable.class));
        verify(spyPrinter).printRecord((Object) "singleValue");
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> iterable = Collections.emptyList();
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, never()).printRecord((Object) any());
    }
}