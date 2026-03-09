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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVPrinter_61_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        // Provide default behavior for getNullString() to avoid NullPointerException
        when(format.getNullString()).thenReturn(null);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NullValue_NullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);

        printer.print(null);

        // Verify that format.getNullString() is called
        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    public void testPrint_NullValue_NullStringNotNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");

        printer.print(null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    public void testPrint_NonNullValue() throws IOException {
        String value = "testValue";

        printer.print(value);

        // Should not call getNullString() when value is non-null
        verify(format, never()).getNullString();
    }

    @Test
    @Timeout(8000)
    public void testPrint_InvokesPrivatePrint() throws Throwable {
        String value = "abc";
        String strValue = "abc";
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // We invoke private print directly to ensure coverage
        privatePrint.invoke(printer, value, strValue, 0, strValue.length());
    }
}