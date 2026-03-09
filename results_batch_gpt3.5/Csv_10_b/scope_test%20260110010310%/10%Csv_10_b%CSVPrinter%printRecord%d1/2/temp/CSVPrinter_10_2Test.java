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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.invocation.InvocationOnMock;

class CSVPrinter_10_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = spy(new CSVPrinter(out, format));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);

        // Mock print(Object) to append string representation to out
        doAnswer(invocation -> {
            Object arg = invocation.getArgument(0);
            String str = arg == null ? "null" : arg.toString();
            out.append(str);
            return null;
        }).when(printer).print(any());

        printer.printRecord(values);

        // Verify print called for each value
        verify(printer, times(3)).print(any());
        // Verify println called once
        verify(printer).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        printer.printRecord(values);

        // No print calls expected
        verify(printer, never()).print(any());
        // println called once
        verify(printer).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrint_Object_CharSequence_int_int() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private print method with parameters (Object, CharSequence, int, int)
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Prepare parameters
        String value = "hello world";
        // Call with offset 0, length value.length()
        privatePrint.invoke(printer, value, value, 0, value.length());

        // Verify Appendable out append called at least once
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintAndEscape() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        String val = "escape,test";
        printAndEscape.invoke(printer, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintAndQuote() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        String val = "quote,test";
        printAndQuote.invoke(printer, val, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}