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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_13_4Test {

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
    void testPrintRecords_withObjectArray_callsPrintRecordArray() throws IOException {
        Object[] values = new Object[] {
                new Object[] { "a", "b" },
                "string",
                java.util.Arrays.asList("x", "y")
        };

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object[]) values[0]);
        verify(spyPrinter).printRecord("string");
        verify(spyPrinter).printRecord((Iterable<?>) java.util.Arrays.asList("x", "y"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyArray() throws IOException {
        Object[] values = new Object[0];
        printer.printRecords(values);
        // No exception, no output expected
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_nullValueInArray() throws IOException {
        Object[] values = new Object[] {null};
        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(values);
        verify(spyPrinter).printRecord((Object) null);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        // Call with simple string
        method.invoke(printer, "text", 0, 4);

        // Call with empty string
        method.invoke(printer, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "object", "quoted", 0, 6);
        method.invoke(printer, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "object", "chars", 0, 5);
        method.invoke(printer, null, "", 0, 0);
    }
}