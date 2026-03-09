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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_10_1Test {

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
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null);

        // Spy on printer to verify print calls
        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("value1");
        inOrder.verify(spyPrinter).print(123);
        inOrder.verify(spyPrinter).print(null);
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecord(values);

        // No print calls expected, only println
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintObjectValue() throws Throwable {
        // Use reflection to invoke private print(Object, CharSequence, int, int)
        Method privatePrintMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrintMethod.setAccessible(true);

        CharSequence cs = "someValue";
        // Call with offset 0 and length cs.length()
        privatePrintMethod.invoke(printer, "objectValue", cs, 0, cs.length());

        // No exception expected, verify no interactions with out mock
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintAndEscape() throws Throwable {
        Method privatePrintAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        privatePrintAndEscape.setAccessible(true);

        CharSequence cs = "escapeValue";
        privatePrintAndEscape.invoke(printer, cs, 0, cs.length());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintAndQuote() throws Throwable {
        Method privatePrintAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        privatePrintAndQuote.setAccessible(true);

        CharSequence cs = "quoteValue";
        privatePrintAndQuote.invoke(printer, "object", cs, 0, cs.length());

        verifyNoMoreInteractions(out);
    }
}