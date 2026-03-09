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

class CSVPrinter_4_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);
        printer.print(null);
        // nullString == null => print called with Constants.EMPTY
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");
        printer.print(null);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "testValue";
        printer.print(value);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrint() throws Throwable {
        String testString = "abc";
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // invoke private print with non-null value
        printMethod.invoke(printer, testString, testString, 0, testString.length());

        // Since print is private and complex, verify append called at least once
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape() throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String value = "escapeTest";
        method.invoke(printer, value, 0, value.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote() throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String value = "quoteTest";
        method.invoke(printer, value, value, 0, value.length());
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValueAndNullStringEmpty() throws IOException {
        when(format.getNullString()).thenReturn(null);
        // print(null) should use Constants.EMPTY internally
        printer.print(null);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}