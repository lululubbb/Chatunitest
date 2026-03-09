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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_10_6Test {

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
    void testPrintRecord_withIterable_callsPrintAndPrintln() throws IOException {
        Iterable<Object> values = Arrays.asList("val1", 123, null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable_callsPrintlnOnly() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_Object_CharSequence_offset_len() throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CharSequence value = "testValue";
        // Call with offset 0 and length = value length
        printMethod.invoke(printer, "object", value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape() throws Throwable {
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        CharSequence value = "escape,me";
        printAndEscapeMethod.invoke(printer, value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote() throws Throwable {
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        CharSequence value = "quote\"me";
        printAndQuoteMethod.invoke(printer, "object", value, 0, value.length());
    }
}