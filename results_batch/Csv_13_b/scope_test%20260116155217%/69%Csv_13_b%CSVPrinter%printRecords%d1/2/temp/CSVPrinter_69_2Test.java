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

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentMatcher;

class CSVPrinter_69_2Test {

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
    void testPrintRecords_withObjectArray() throws Exception {
        Object[] record1 = new Object[] {"a", "b"};
        Object[] record2 = new Object[] {"c", "d"};

        Iterable<Object[]> iterable = Arrays.asList(record1, record2);

        // Spy on printer to verify printRecord(Object[]) called
        CSVPrinter spyPrinter = spy(printer);

        // Use doCallRealMethod to avoid calling the real printRecord(Object[]) immediately
        doCallRealMethod().when(spyPrinter).printRecords(iterable);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord(argThat(new ArgumentMatcher<Object[]>() {
            @Override
            public boolean matches(Object[] argument) {
                return argument != null && argument.length == record1.length &&
                       argument[0].equals(record1[0]) && argument[1].equals(record1[1]);
            }
        }));
        verify(spyPrinter, times(1)).printRecord(argThat(new ArgumentMatcher<Object[]>() {
            @Override
            public boolean matches(Object[] argument) {
                return argument != null && argument.length == record2.length &&
                       argument[0].equals(record2[0]) && argument[1].equals(record2[1]);
            }
        }));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws Exception {
        Iterable<String> innerIterable1 = Arrays.asList("x", "y");
        Iterable<String> innerIterable2 = Collections.singletonList("z");

        Iterable<Iterable<String>> iterable = Arrays.asList(innerIterable1, innerIterable2);

        CSVPrinter spyPrinter = spy(printer);

        doCallRealMethod().when(spyPrinter).printRecords(iterable);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord(innerIterable1);
        verify(spyPrinter, times(1)).printRecord(innerIterable2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withOtherObjects() throws Exception {
        Iterable<Object> iterable = Arrays.asList("string", 123, 45.6);

        CSVPrinter spyPrinter = spy(printer);

        doCallRealMethod().when(spyPrinter).printRecords(iterable);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord("string");
        verify(spyPrinter, times(1)).printRecord(123);
        verify(spyPrinter, times(1)).printRecord(45.6);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyIterable() throws IOException {
        Iterable<Object> empty = Collections.emptyList();

        // Should not throw and not print anything
        printer.printRecords(empty);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_nullElement() throws Exception {
        Iterable<Object> iterable = Arrays.asList((Object) null);

        CSVPrinter spyPrinter = spy(printer);

        doCallRealMethod().when(spyPrinter).printRecords(iterable);

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord((Object) null);
    }
}