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

class CSVPrinter_4_5Test {

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

        verify(format).getNullString();
        verifyNoMoreInteractions(format);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNonNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");

        printer.print(null);

        verify(format).getNullString();
        verifyNoMoreInteractions(format);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "value";

        printer.print(value);

        verify(format, never()).getNullString();
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrint() throws Throwable {
        String testValue = "test";

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Call public print to set up the internal call
        printer.print(testValue);

        // Directly invoke private print to cover branches
        printMethod.invoke(printer, testValue, testValue, 0, testValue.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintAndEscapeAndQuote() throws Throwable {
        // Access private print method and test its internal calls indirectly by reflection
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        String val = "escape,quote\"test";
        // invoke printAndEscape
        printAndEscape.invoke(printer, val, 0, val.length());

        // invoke printAndQuote
        printAndQuote.invoke(printer, val, val, 0, val.length());

        // invoke private print with normal value
        privatePrint.invoke(printer, val, val, 0, val.length());
    }
}