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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVPrinter_67_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        // Use real CSVFormat instance or mock with default behavior to avoid NullPointerException inside CSVPrinter
        when(format.getRecordSeparator()).thenReturn(System.lineSeparator());
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<?> empty = Collections.emptyList();
        printer.printRecord(empty);
        // Should print a newline only, so out should contain a newline (LF or CRLF)
        String output = out.toString();
        // We don't know exact newline char, but output should not be empty
        assertFalse(output.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withSingleValue() throws IOException {
        Iterable<Object> values = Collections.singletonList("value");
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(1)).print("value");
        String output = out.toString();
        assertFalse(output.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("one", "two", "three");
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(1)).print("one");
        verify(spyPrinter, times(1)).print("two");
        verify(spyPrinter, times(1)).print("three");
        String output = out.toString();
        assertFalse(output.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withNullValue() throws IOException {
        Iterable<Object> values = Arrays.asList("one", null, "three");
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(1)).print("one");
        verify(spyPrinter, times(1)).print((Object) null);
        verify(spyPrinter, times(1)).print("three");
        String output = out.toString();
        assertFalse(output.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_reflectionInvoke() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterable<Object> values = Arrays.asList("a", "b");
        Method printRecordMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Iterable.class);
        printRecordMethod.setAccessible(true);

        printRecordMethod.invoke(printer, values);

        String output = out.toString();
        assertFalse(output.isEmpty());
    }
}