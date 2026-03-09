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
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_12_5Test {

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
    void testPrintRecords_withIterableOfObjects() throws IOException {
        Iterable<Object> values = Arrays.asList("one", "two", "three");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        // disambiguate printRecord(Object...) by casting argument to Object[]
        verify(spyPrinter, times(3)).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingObjectArray() throws IOException {
        Object[] record = new Object[] { "a", "b" };
        Iterable<Object> values = Collections.singletonList(record);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord(record);
        verify(spyPrinter, never()).printRecord(any(Iterable.class));
        // disambiguate printRecord(Object...) by casting argument to Object[]
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterable() throws IOException {
        Iterable<String> innerIterable = Arrays.asList("x", "y");
        Iterable<Object> values = Collections.singletonList(innerIterable);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord(innerIterable);
        verify(spyPrinter, never()).printRecord((Object[]) any());
        // disambiguate printRecord(Object...) by casting argument to Object[]
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingOtherObject() throws IOException {
        Iterable<Object> values = Collections.singletonList(123);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        // disambiguate printRecord(Object...) by casting argument to Object[]
        verify(spyPrinter, times(1)).printRecord((Object) 123);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        // disambiguate printRecord(Object...) by casting argument to Object[]
        verify(spyPrinter, never()).printRecord((Object) any());
    }
}