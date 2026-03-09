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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_4_2Test {

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
    void testPrint_nullValue_nullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);

        printer.print(null);

        // verify that append was called with empty string (Constants.EMPTY)
        verify(out).append("");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNonNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");

        printer.print(null);

        verify(out).append("NULL");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String val = "value";

        printer.print(val);

        verify(out).append("value");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintInvocation() throws Throwable {
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Call with normal string
        privatePrint.invoke(printer, "abc", "abc", 0, 3);

        // Call with empty string
        privatePrint.invoke(printer, "", "", 0, 0);

        // Call with null object and empty string
        privatePrint.invoke(printer, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscapeAndQuote_privateMethods() throws Throwable {
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        // Call printAndEscape with normal string
        printAndEscape.invoke(printer, "escapeTest", 0, 10);

        // Call printAndEscape with empty string
        printAndEscape.invoke(printer, "", 0, 0);

        // Call printAndQuote with normal string and object
        printAndQuote.invoke(printer, "obj", "quoteTest", 0, 9);

        // Call printAndQuote with null object and empty string
        printAndQuote.invoke(printer, null, "", 0, 0);
    }
}