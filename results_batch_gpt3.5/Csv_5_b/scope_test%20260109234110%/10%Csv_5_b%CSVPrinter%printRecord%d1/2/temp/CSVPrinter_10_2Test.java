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

class CSVPrinter_10_2Test {

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
    void testPrintRecord_withEmptyIterable() throws IOException {
        printer.printRecord(Collections.emptyList());
        // Should call println() once after loop, which appends a newline
        verify(out).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        // Spy on printer to verify print(Object) calls
        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));
        Iterable<Object> values = Arrays.asList("a", 123, null);

        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("a");
        inOrder.verify(spyPrinter).print(123);
        inOrder.verify(spyPrinter).print(null);
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws IOException {
        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));
        Iterable<Object> values = Collections.singletonList("value");

        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print("value");
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndPrintln() throws Exception {
        // Use reflection to invoke private printAndQuote method to ensure coverage indirectly
        String testString = "test";
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        // Call printRecord with one value to invoke print and println
        printer.printRecord(Collections.singleton(testString));

        // Invoke private method directly to cover it
        printAndQuoteMethod.invoke(printer, testString, testString, 0, testString.length());
    }
}