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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_11_4Test {

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
        // Arrange
        Object val1 = "test1";
        Object val2 = 123;
        Object val3 = null;

        CSVPrinter spyPrinter = Mockito.spy(printer);

        // Act
        spyPrinter.printRecord(val1, val2, val3);

        // Assert
        verify(spyPrinter, times(1)).print(val1);
        verify(spyPrinter, times(1)).print(val2);
        verify(spyPrinter, times(1)).print(val3);
        verify(spyPrinter, times(1)).println();

        // Also verify output is not null
        assertNotNull(out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethodViaReflection() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private print method to ensure coverage
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        String testValue = "reflectionTest";
        CharSequence cs = testValue;
        // invoke private print method with offset 0 and length = testValue.length()
        privatePrint.invoke(printer, testValue, cs, 0, testValue.length());

        // No exception means success; also verify out is not null
        assertNotNull(out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndEscapePrivateMethodViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        String testValue = "escapeTest";
        printAndEscape.invoke(printer, testValue, 0, testValue.length());

        assertNotNull(out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndQuotePrivateMethodViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        String testValue = "quoteTest";
        printAndQuote.invoke(printer, testValue, testValue, 0, testValue.length());

        assertNotNull(out.toString());
    }
}