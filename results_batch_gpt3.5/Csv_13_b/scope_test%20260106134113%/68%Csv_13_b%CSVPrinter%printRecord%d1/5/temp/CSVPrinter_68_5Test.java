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

class CSVPrinter_68_5Test {

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
        // Given values of different types including null
        Object[] values = { "hello", 123, null, 45.67 };

        // Spy on printer to verify print(Object) calls
        CSVPrinter spyPrinter = Mockito.spy(printer);

        // When
        spyPrinter.printRecord(values);

        // Then
        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws IOException {
        Object[] values = new Object[0];

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullArray() {
        assertThrows(NullPointerException.class, () -> printer.printRecord((Object[]) null));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        Appendable spyOut = spy(new StringBuilder());
        CSVPrinter localPrinter = new CSVPrinter(spyOut, format);

        method.invoke(localPrinter, "test", 0, 4);

        verify(spyOut, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        Appendable spyOut = spy(new StringBuilder());
        CSVPrinter localPrinter = new CSVPrinter(spyOut, format);

        method.invoke(localPrinter, "obj", "quote", 0, 5);

        verify(spyOut, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        Appendable spyOut = spy(new StringBuilder());
        CSVPrinter localPrinter = new CSVPrinter(spyOut, format);

        method.invoke(localPrinter, "obj", "value", 0, 5);

        verify(spyOut, atLeastOnce()).append(anyChar());
    }
}