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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_68_3Test {

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
        Object[] values = {"value1", 123, null, "value4"};

        // We spy to verify calls to print(Object)
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(values);

        // Verify print called for each value
        for (Object value : values) {
            verify(spyPrinter).print(value);
        }

        // Verify println called once
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws IOException {
        Object[] values = new Object[0];

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(values);

        // print should never be called
        verify(spyPrinter, never()).print(any());

        // println should be called once
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullArray() {
        assertThrows(NullPointerException.class, () -> printer.printRecord((Object[]) null));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethodViaReflection() throws Throwable {
        // Use reflection to invoke private print(Object, CharSequence, int, int)
        Method privatePrintMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrintMethod.setAccessible(true);

        CharSequence val = "hello,world";
        // Invoke with offset 0 and length val.length()
        try {
            privatePrintMethod.invoke(printer, val, val, 0, val.length());
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}