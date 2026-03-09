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

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_4_1Test {

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
    void testPrint_NullValue_nullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);
        printer.print(null);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_nullStringNotNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");
        printer.print(null);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrint_NonNullValue() throws IOException {
        String val = "value";
        printer.print(val);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, "abc", "abc", 0, 3);
        printMethod.invoke(printer, "", "", 0, 0);
        printMethod.invoke(printer, "abc", "abc", 1, 2);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        printAndEscape.invoke(printer, "escapeTest", 0, 10);
        printAndEscape.invoke(printer, "", 0, 0);
        printAndEscape.invoke(printer, "partialEscape", 3, 4);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuoteMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        printAndQuote.invoke(printer, "obj", "quoted", 0, 6);
        printAndQuote.invoke(printer, null, "", 0, 0);
        printAndQuote.invoke(printer, 123, "123456", 1, 4);
    }
}