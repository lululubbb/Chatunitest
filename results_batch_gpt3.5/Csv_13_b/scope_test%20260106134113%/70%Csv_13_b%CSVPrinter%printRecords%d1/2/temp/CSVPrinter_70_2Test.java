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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_70_2Test {

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
    void testPrintRecords_withSingleObjects() throws Exception {
        // Single objects, not arrays or Iterable
        Object o1 = "value1";
        Object o2 = 123;

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(o1, o2);

        verify(spyPrinter, times(1)).printRecord(o1);
        verify(spyPrinter, times(1)).printRecord(o2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws Exception {
        Object[] array = new Object[]{"a", "b", "c"};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords((Object) array);

        verify(spyPrinter, times(1)).printRecord(array);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws Exception {
        List<String> list = Arrays.asList("x", "y");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords((Object) list);

        verify(spyPrinter, times(1)).printRecord(list);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_mixedArguments() throws Exception {
        Object[] array = new Object[]{"a", "b"};
        List<String> list = Arrays.asList("x", "y");
        String single = "single";

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(array, list, single);

        verify(spyPrinter, times(1)).printRecord(array);
        verify(spyPrinter, times(1)).printRecord(list);
        verify(spyPrinter, times(1)).printRecord(single);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_noArguments() throws Exception {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords();

        // fix ambiguity by using verify(spyPrinter, never()).printRecord(Object[])
        verify(spyPrinter, never()).printRecord(any(Object[].class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_privateMethodInvocation() throws Exception {
        // Just to demonstrate invoking private method printRecords(Iterable<?>)
        Method printRecordsIterable = CSVPrinter.class.getDeclaredMethod("printRecords", Iterable.class);
        printRecordsIterable.setAccessible(true);

        List<String> list = Arrays.asList("a", "b");

        printRecordsIterable.invoke(printer, list);
        // No exception means success, no output to verify here
    }
}