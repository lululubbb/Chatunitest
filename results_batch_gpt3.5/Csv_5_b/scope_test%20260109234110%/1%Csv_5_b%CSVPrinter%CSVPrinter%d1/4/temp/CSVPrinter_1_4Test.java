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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_1_4Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_validParameters_shouldInitializeFields() {
        doNothing().when(format).validate();

        CSVPrinter printer = new CSVPrinter(out, format);

        assertNotNull(printer);
        assertSame(out, printer.getOut());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_shouldThrowNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_shouldThrowNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_formatValidateThrows_shouldPropagateException() {
        doThrow(new IllegalArgumentException("invalid")).when(format).validate();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(out, format));
        assertEquals("invalid", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintPrivateMethods_viaReflection() throws Exception {
        // Mock Appendable.append(CharSequence) to return the Appendable itself
        when(out.append(any(CharSequence.class))).thenReturn(out);
        // Also mock Appendable.append(CharSequence, int, int) to return Appendable itself for any substring append
        when(out.append(any(CharSequence.class), anyInt(), anyInt())).thenReturn(out);
        // Mock Appendable.append(char) to return Appendable itself
        when(out.append(anyChar())).thenReturn(out);

        doNothing().when(format).validate();

        CSVPrinter printer = new CSVPrinter(out, format);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        // Call print(Object, CharSequence, int, int) with normal string
        printMethod.invoke(printer, "obj", "value", 0, 5);

        // Call printAndEscape with value
        printAndEscapeMethod.invoke(printer, "escapeValue", 0, 11);

        // Call printAndQuote with value
        printAndQuoteMethod.invoke(printer, "obj", "quoteValue", 0, 10);
    }
}