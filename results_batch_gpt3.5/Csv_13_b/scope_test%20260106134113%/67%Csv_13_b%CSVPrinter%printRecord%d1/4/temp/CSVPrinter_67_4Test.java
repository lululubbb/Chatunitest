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
        Iterable<?> empty = Collections.emptyList();
        printer.printRecord(empty);
        // verify no print calls, only println called (println writes line separator)
        verify(out, never()).append(anyChar());
        verify(out, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws Exception {
        Iterable<Object> singleValue = Collections.singletonList("test");
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(singleValue);

        verify(spyPrinter).print("test");
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws Exception {
        Iterable<Object> multipleValues = Arrays.asList("a", 123, null);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(multipleValues);

        verify(spyPrinter).print("a");
        verify(spyPrinter).print(123);
        verify(spyPrinter).print((Object) null);
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectPrivatePrintAndEscape() throws Exception {
        // Use reflection to invoke private method printAndEscape(CharSequence, int, int)
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        // Test with normal string
        printAndEscapeMethod.invoke(printer, "escapeTest", 0, 10);

        // Test with empty string
        printAndEscapeMethod.invoke(printer, "", 0, 0);

        // Test with subrange
        printAndEscapeMethod.invoke(printer, "escapeTest", 2, 4);

        // Verify append called at least once (cannot verify exact calls because Appendable is mocked)
        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectPrivatePrintAndQuote() throws Exception {
        // Use reflection to invoke private method printAndQuote(Object, CharSequence, int, int)
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        // Test with string object
        printAndQuoteMethod.invoke(printer, "obj", "quoteTest", 0, 9);

        // Test with null object
        printAndQuoteMethod.invoke(printer, null, "", 0, 0);

        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectPrivatePrintObjectValue() throws Exception {
        // Use reflection to invoke private method print(Object, CharSequence, int, int)
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Normal string
        printMethod.invoke(printer, "obj", "value", 0, 5);

        // Null value
        printMethod.invoke(printer, null, "", 0, 0);

        verify(out, atLeastOnce()).append(anyChar());
    }
}