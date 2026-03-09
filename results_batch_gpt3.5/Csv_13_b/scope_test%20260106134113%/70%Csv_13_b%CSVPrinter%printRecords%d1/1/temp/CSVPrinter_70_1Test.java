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
import org.mockito.Mockito;

class CSVPrinter_70_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;
    private Method printRecordIterableMethod;
    private Method printRecordObjectArrayMethod;

    @BeforeEach
    void setUp() throws Exception {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);

        // Get methods to disambiguate overloaded printRecord calls
        printRecordIterableMethod = CSVPrinter.class.getMethod("printRecord", Iterable.class);
        printRecordObjectArrayMethod = CSVPrinter.class.getMethod("printRecord", Object[].class);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSingleObjects() throws IOException {
        printer = spy(printer);
        Object v1 = "value1";
        Object v2 = 123;
        printer.printRecords(v1, v2);
        verify(printer, times(1)).printRecord(v1);
        verify(printer, times(1)).printRecord(v2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws IOException {
        printer = spy(printer);
        Object[] arr = new Object[] {"a", "b"};
        printer.printRecords((Object)arr);
        verify(printer, times(1)).printRecord(arr);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        printer = spy(printer);
        Iterable<String> iterable = Arrays.asList("x", "y");
        printer.printRecords((Object)iterable);
        verify(printer, times(1)).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedValues() throws IOException {
        printer = spy(printer);
        Object[] arr = new Object[] {"arr"};
        Iterable<String> iterable = Collections.singletonList("iter");
        Object obj = "obj";

        printer.printRecords(arr, iterable, obj);

        verify(printer, times(1)).printRecord(arr);
        verify(printer, times(1)).printRecord(iterable);
        verify(printer, times(1)).printRecord(obj);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNoValues() throws Exception {
        printer = spy(printer);

        // Call printRecords with no arguments
        printer.printRecords();

        // Verify that printRecord(Object) was never called
        // Disambiguate overloaded methods using reflection and Mockito's verify with method references

        // Verify printRecord(Iterable<?>) never called
        verify(printer, never()).printRecord((Iterable<?>) any());

        // Verify printRecord(Object...) never called
        verify(printer, never()).printRecord((Object[]) any());
        
        // For printRecord(Object) - since no such method exists (only Iterable<?> and Object...),
        // we do not verify it here. The calls go to either Iterable<?> or Object...
    }
}