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
import org.mockito.Mockito;

class CSVPrinter_67_3Test {

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
    void testPrintRecordWithEmptyIterable() throws IOException {
        Iterable<?> empty = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecord(empty);

        // For empty iterable, print(value) is never called, but println() is called
        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithSingleValue() throws IOException {
        Iterable<String> values = Collections.singletonList("value");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecord(values);

        verify(spyPrinter).print("value");
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("one", 2, null);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecord(values);

        verify(spyPrinter).print("one");
        verify(spyPrinter).print(2);
        verify(spyPrinter).print((Object) null);
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintObjectValueOffsetLen() throws Throwable {
        String testValue = "testValue";
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Call with full string
        printMethod.invoke(printer, testValue, testValue, 0, testValue.length());

        // Call with offset and len subset
        printMethod.invoke(printer, testValue, testValue, 2, 4);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape() throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String val = "escapeTest";

        method.invoke(printer, val, 0, val.length());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote() throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String val = "quoteTest";

        method.invoke(printer, val, val, 0, val.length());
    }
}