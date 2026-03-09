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
import org.mockito.Mockito;

class CSVPrinter_70_6Test {

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
    void testPrintRecords_withSingleObjects() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object obj1 = "value1";
        Integer obj2 = 42;

        spyPrinter.printRecords(obj1, obj2);

        verify(spyPrinter).printRecord(obj1);
        verify(spyPrinter).printRecord(obj2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object[] array = new Object[] { "a", "b" };

        // call printRecords with the array as a single Object (no unpacking)
        spyPrinter.printRecords((Object) array);

        verify(spyPrinter).printRecord(array);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Iterable<String> iterable = Arrays.asList("x", "y");

        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmpty() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords();

        verify(spyPrinter, never()).printRecord((Object) any());
    }

}