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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_12_5Test {

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
    void testPrintRecords_withIterableOfObjects_callsPrintRecordObject() throws IOException {
        Iterable<String> iterable = Arrays.asList("a", "b", "c");
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        for (String s : iterable) {
            verify(spyPrinter).printRecord(s);
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArray_callsPrintRecordObjectArray() throws IOException {
        Object[] objArray1 = new Object[] {1, 2};
        Object[] objArray2 = new Object[] {"x", "y"};
        Iterable<Object> iterable = Arrays.asList(objArray1, objArray2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(objArray1);
        verify(spyPrinter).printRecord(objArray2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterable_callsPrintRecordIterable() throws IOException {
        Iterable<String> innerIterable1 = Arrays.asList("1", "2");
        Iterable<String> innerIterable2 = Collections.singletonList("x");
        Iterable<Iterable<String>> iterable = Arrays.asList(innerIterable1, innerIterable2);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(innerIterable1);
        verify(spyPrinter).printRecord(innerIterable2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyIterable_noPrintRecordCalls() throws IOException {
        Iterable<Object> emptyIterable = Collections.emptyList();
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(emptyIterable);

        verify(spyPrinter, never()).printRecord(isA(Iterable.class));
        verify(spyPrinter, never()).printRecord((Object[]) any());

        // verify printRecord(Object) is never called
        verify(spyPrinter, never()).printRecord(any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedIterable_callsAppropriatePrintRecord() throws IOException {
        Object[] objArray = new Object[] {"a"};
        Iterable<String> iterable = Arrays.asList("x", "y");
        Object simpleObject = 123;
        Iterable<Object> mixed = Arrays.asList(objArray, iterable, simpleObject);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(mixed);

        verify(spyPrinter).printRecord(objArray);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(simpleObject);
    }
}