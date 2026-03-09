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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_13_3Test {

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
    void testPrintRecords_withObjectArrayContainingObjectArray() throws Exception {
        Object[] nested = new Object[] { "nested1", "nested2" };
        Object[] values = new Object[] { nested };
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        // verify printRecord(Object...) called with nested elements
        verify(spyPrinter).printRecord((Object[]) nested);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArrayContainingIterable() throws Exception {
        Iterable<String> iterable = Arrays.asList("a", "b");
        Object[] values = new Object[] { iterable };
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArrayContainingOtherObject() throws Exception {
        Object value = "stringValue";
        Object[] values = new Object[] { value };
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(value);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyArray() throws Exception {
        Object[] values = new Object[0];
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withNullValueInArray() throws Exception {
        Object[] values = new Object[] { null };
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object) null);
    }
}