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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_68_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        printer.printRecord("value1", 123, null, true);

        String result = out.toString();
        assertFalse(result.isEmpty());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("true"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        printer.printRecord();

        String result = out.toString();
        assertTrue(result.equals(System.lineSeparator()) || result.equals("\n") || result.equals("\r\n"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndPrintln() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(out, format));

        spyPrinter.printRecord("a", "b");

        verify(spyPrinter, times(2)).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintMethodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Pass correct parameters: object, CharSequence value, offset, length
        printMethod.invoke(printer, "obj", "value", 0, "value".length());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintAndEscapeInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        printAndEscapeMethod.invoke(printer, "escapeValue", 0, "escapeValue".length());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintAndQuoteInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        printAndQuoteMethod.invoke(printer, "obj", "quoteValue", 0, "quoteValue".length());
    }
}