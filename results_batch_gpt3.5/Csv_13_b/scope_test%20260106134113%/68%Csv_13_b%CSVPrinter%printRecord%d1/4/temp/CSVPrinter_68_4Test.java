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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_68_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        printer.printRecord("value1", 123, null, "value4");
        String result = out.toString();
        // The output should contain all values printed and a newline at the end.
        // We don't know exact format details, but at least check that values appear
        // and output ends with a newline.
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("null"));
        assertTrue(result.contains("value4"));
        assertTrue(result.endsWith(System.lineSeparator()) || result.endsWith("\n") || result.endsWith("\r\n"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws IOException {
        printer.printRecord();
        String result = out.toString();
        // Should just print a newline
        assertTrue(result.equals(System.lineSeparator()) || result.equals("\n") || result.equals("\r\n"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintForEachValue() throws Exception {
        CSVPrinter spyPrinter = Mockito.spy(printer);
        Object[] values = {"a", "b", "c"};
        spyPrinter.printRecord(values);
        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintMethodViaReflection() throws Exception {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        StringBuilder testOut = new StringBuilder();
        CSVPrinter localPrinter = new CSVPrinter(testOut, format);

        // Call private print method with a substring of a CharSequence
        String val = "HelloWorld";
        printMethod.invoke(localPrinter, val, val, 0, 5);

        String output = testOut.toString();
        // Output should contain "Hello" at least
        assertTrue(output.contains("Hello"));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeMethodViaReflection() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        StringBuilder testOut = new StringBuilder();
        CSVPrinter localPrinter = new CSVPrinter(testOut, format);

        String val = "EscapeTest";
        method.invoke(localPrinter, val, 0, val.length());

        String output = testOut.toString();
        assertTrue(output.contains("EscapeTest"));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuoteMethodViaReflection() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        StringBuilder testOut = new StringBuilder();
        CSVPrinter localPrinter = new CSVPrinter(testOut, format);

        String val = "QuoteTest";
        method.invoke(localPrinter, val, val, 0, val.length());

        String output = testOut.toString();
        assertTrue(output.contains("QuoteTest"));
    }
}