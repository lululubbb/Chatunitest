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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_11_5Test {

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
    void testPrintRecord_withMultipleValues_callsPrintAndPrintln() throws IOException {
        // Arrange
        Object[] values = {"value1", 123, null, "value4"};

        // Spy the printer to verify calls to print(Object) and println()
        CSVPrinter spyPrinter = spy(printer);

        // Avoid calling the real print(Object) to prevent IOException from mocked Appendable
        doNothing().when(spyPrinter).print(any());

        // Act
        spyPrinter.printRecord(values);

        // Assert
        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues_callsOnlyPrintln() throws IOException {
        // Spy the printer to verify calls to print(Object) and println()
        CSVPrinter spyPrinter = spy(printer);

        // Avoid calling the real print(Object)
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Prepare a string with special characters to test escaping
        String value = "a,b\"c";

        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        // Act
        method.invoke(printer, value, 0, value.length());

        // Verify that Appendable.append was called at least once (escaping writes to out)
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        String value = "quoted";

        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, value, value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // print(Object, CharSequence, int, int) is private, test via reflection

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        String value = "test";
        printMethod.invoke(printer, value, value, 0, value.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

}