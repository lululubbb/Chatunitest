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

public class CSVPrinter_67_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withNonEmptyIterable() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);

        CSVPrinter spyPrinter = spy(printer);

        // Use doNothing() on print(Object) to avoid actual printing side effects
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(3)).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);

        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

}