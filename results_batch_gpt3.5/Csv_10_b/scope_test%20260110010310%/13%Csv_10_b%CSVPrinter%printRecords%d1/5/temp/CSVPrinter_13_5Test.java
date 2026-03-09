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
import org.mockito.Mockito;

class CSVPrinter_13_5Test {

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
    void testPrintRecords_withObjectArrayContainingObjectArrayIterableAndSingleObject() throws Exception {
        Object[] nestedArray = new Object[] { "nested1", "nested2" };
        Iterable<String> iterable = java.util.Arrays.asList("iter1", "iter2");
        String singleObject = "single";

        Object[] values = new Object[] { nestedArray, iterable, singleObject };

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object[]) nestedArray);
        verify(spyPrinter).printRecord((Iterable<?>) iterable);
        verify(spyPrinter).printRecord(singleObject);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyArray() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);
        Object[] values = new Object[0];
        spyPrinter.printRecords(values);
        // Cast to Object[] to disambiguate overloaded methods
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_nullValueInArray() throws Exception {
        Object[] values = new Object[] { null };

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object) isNull());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintMethodUsingReflection() throws Exception {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Call with some sample arguments
        printMethod.invoke(printer, "obj", "value", 0, 5);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeMethodUsingReflection() throws Exception {
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        printAndEscapeMethod.invoke(printer, "escapeValue", 0, 11);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuoteMethodUsingReflection() throws Exception {
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        printAndQuoteMethod.invoke(printer, "obj", "quoteValue", 0, 10);
    }
}