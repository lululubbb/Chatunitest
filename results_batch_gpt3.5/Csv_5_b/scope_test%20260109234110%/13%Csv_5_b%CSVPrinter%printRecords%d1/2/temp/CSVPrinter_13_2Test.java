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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_13_2Test {

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
    void testPrintRecords_withArrayContainingObjectArray_Iterable_andOther() throws IOException {
        Object[] nestedArray = new Object[] {"nested1", "nested2"};
        Iterable<String> iterable = Arrays.asList("iter1", "iter2");
        String other = "other";

        Object[] values = new Object[] {nestedArray, iterable, other};

        // Spy on printer to verify internal printRecord calls
        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        // Verify printRecord(Object[]) called with nestedArray
        verify(spyPrinter).printRecord((Object[]) nestedArray);

        // Verify printRecord(Iterable<?>) called with iterable
        verify(spyPrinter).printRecord((Iterable<?>) iterable);

        // Verify printRecord(Object) called with other
        verify(spyPrinter).printRecord((Object) other);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyArray() throws IOException {
        Object[] values = new Object[0];

        // Should not throw and no printRecord calls
        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord(any(Object[].class));
        verify(spyPrinter, never()).printRecord(any(Iterable.class));

        // To avoid ambiguity, use ArgumentMatchers and cast properly
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNullValueInsideArray() throws IOException {
        Object[] values = new Object[] {null};

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object) isNull());
    }
}