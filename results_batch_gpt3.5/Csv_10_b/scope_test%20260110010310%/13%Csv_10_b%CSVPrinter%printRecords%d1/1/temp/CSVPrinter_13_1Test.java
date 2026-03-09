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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinterPrintRecordsTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArrayContainingObjectArray() throws Exception {
        Object[] innerArray = new Object[] {"inner1", "inner2"};
        Object[] values = new Object[] {innerArray};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(innerArray);
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArrayContainingIterable() throws Exception {
        Iterable<String> iterable = java.util.Arrays.asList("one", "two");
        Object[] values = new Object[] {iterable};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArrayContainingOtherObject() throws Exception {
        Object value = "stringValue";
        Object[] values = new Object[] {value};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(value);
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMultipleMixedValues() throws Exception {
        Object[] innerArray = new Object[] {"inner1"};
        Iterable<String> iterable = java.util.Collections.singletonList("one");
        Object value = 123;
        Object[] values = new Object[] {innerArray, iterable, value};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord((Iterable<?>) any());
        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(innerArray);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(value);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyArray() throws IOException {
        Object[] values = new Object[0];

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Object) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object[]) any());
    }
}