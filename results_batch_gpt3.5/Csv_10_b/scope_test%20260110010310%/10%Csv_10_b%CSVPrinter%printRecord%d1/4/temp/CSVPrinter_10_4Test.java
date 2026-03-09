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

class CSVPrinter_10_4Test {

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
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<?> emptyIterable = Collections.emptyList();

        printer.printRecord(emptyIterable);

        // Should just print a newline (println)
        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws IOException {
        Iterable<?> iterable = Collections.singletonList("value");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecord(iterable);

        verify(spyPrinter).print("value");
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<?> iterable = Arrays.asList("val1", 123, null);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecord(iterable);

        verify(spyPrinter).print("val1");
        verify(spyPrinter).print(123);
        verify(spyPrinter).print((Object) null);
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_printThrowsIOException() throws IOException {
        Iterable<?> iterable = Collections.singletonList("fail");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        doThrow(new IOException("print failed")).when(spyPrinter).print("fail");

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecord(iterable));
        assertEquals("print failed", thrown.getMessage());
    }
}