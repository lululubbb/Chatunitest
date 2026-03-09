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
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_67_4Test {

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
        printer.printRecord(Collections.emptyList());
        // Should call println() after no print calls
        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        // Stub print(Object) to do nothing to avoid actual printing side effects
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(3)).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintAndEscape() throws Throwable {
        // Using reflection to invoke private print(Object, CharSequence, int, int)
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Call with normal string
        privatePrint.invoke(printer, "obj", "value", 0, 5);

        // Call with empty CharSequence
        privatePrint.invoke(printer, "obj", "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeAndPrintAndQuote() throws Throwable {
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);
        printAndEscape.invoke(printer, "escapeMe", 0, 8);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);
        printAndQuote.invoke(printer, "obj", "quoteMe", 0, 7);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_nullValues() throws IOException {
        Iterable<Object> values = Collections.singletonList(null);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        // Stub print(Object) to do nothing to avoid actual printing side effects
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(values);

        verify(spyPrinter).print((Object) null);
        verify(spyPrinter).println();
    }

}