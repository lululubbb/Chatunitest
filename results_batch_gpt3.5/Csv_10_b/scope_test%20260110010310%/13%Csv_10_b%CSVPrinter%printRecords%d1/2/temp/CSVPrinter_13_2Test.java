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

class CSVPrinter_13_2Test {

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
    void testPrintRecords_withArrayOfObjects() throws IOException {
        Object[] values = new Object[] { "one", "two", "three" };

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        // Disambiguate printRecord(Object...) by casting argument to Object[]
        verify(spyPrinter, times(3)).printRecord(any());
        verify(spyPrinter).printRecord((Object) "one");
        verify(spyPrinter).printRecord((Object) "two");
        verify(spyPrinter).printRecord((Object) "three");
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withArrayContainingObjectArray() throws IOException {
        Object[] nestedArray = new Object[] { "nested1", "nested2" };
        Object[] values = new Object[] { nestedArray };

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord((Object[]) nestedArray);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withArrayContainingIterable() throws IOException {
        Iterable<String> iterable = Arrays.asList("a", "b");
        Object[] values = new Object[] { iterable };

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyArray() throws IOException {
        Object[] values = new Object[0];

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord(any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNullValue() throws IOException {
        Object[] values = new Object[] { (Object) null };

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord((Object) null);
    }
}