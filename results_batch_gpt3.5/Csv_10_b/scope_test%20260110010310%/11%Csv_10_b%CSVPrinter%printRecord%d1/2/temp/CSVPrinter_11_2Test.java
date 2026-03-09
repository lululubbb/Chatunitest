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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_11_2Test {

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
        printer.printRecord("value1", 123, null, "value4");

        String printed = out.toString();
        assertFalse(printed.isEmpty());
        assertTrue(printed.contains("value1") || printed.contains("123") || printed.contains("value4"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        printer.printRecord();

        String printed = out.toString();
        assertTrue(printed.length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndPrintln() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(out, format));

        spyPrinter.printRecord("abc");

        verify(spyPrinter, times(1)).print("abc");
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintMethodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        try {
            privatePrint.invoke(printer, "obj", "value", 0, 5);
        } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof IOException)) {
                throw e;
            }
        }

        String printed = out.toString();
        assertTrue(printed.length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method privatePrintAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        privatePrintAndEscape.setAccessible(true);

        try {
            privatePrintAndEscape.invoke(printer, "escapeTest", 0, 10);
        } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof IOException)) {
                throw e;
            }
        }

        String printed = out.toString();
        assertTrue(printed.length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuoteInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method privatePrintAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        privatePrintAndQuote.setAccessible(true);

        try {
            privatePrintAndQuote.invoke(printer, "obj", "quotedValue", 0, 11);
        } catch (InvocationTargetException e) {
            if (!(e.getCause() instanceof IOException)) {
                throw e;
            }
        }

        String printed = out.toString();
        assertTrue(printed.length() > 0);
    }
}