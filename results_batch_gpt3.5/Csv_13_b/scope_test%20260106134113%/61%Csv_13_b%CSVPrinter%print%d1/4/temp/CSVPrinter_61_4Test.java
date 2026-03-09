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
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVPrinter_61_4Test {

    private CSVFormat formatMock;
    private Appendable appendableMock;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        // Default behavior for getNullString() is null
        when(formatMock.getNullString()).thenReturn(null);
        printer = new CSVPrinter(appendableMock, formatMock);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NullValue_NullStringNull() throws Exception {
        when(formatMock.getNullString()).thenReturn(null);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print((Object) null);

        verify(spyPrinter, times(1)).print((Object) null);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);
        privatePrint.invoke(spyPrinter, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NullValue_NullStringNotNull() throws Exception {
        final String nullString = "NULLVAL";
        when(formatMock.getNullString()).thenReturn(nullString);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print((Object) null);

        verify(spyPrinter, times(1)).print((Object) null);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);
        privatePrint.invoke(spyPrinter, null, nullString, 0, nullString.length());
    }

    @Test
    @Timeout(8000)
    public void testPrint_NonNullValue() throws Exception {
        Object value = 12345;
        String strValue = value.toString();
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(value);

        verify(spyPrinter, times(1)).print(value);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);
        privatePrint.invoke(spyPrinter, value, strValue, 0, strValue.length());
    }

    @Test
    @Timeout(8000)
    public void testPrint_EmptyString() throws Exception {
        String value = "";
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(value);

        verify(spyPrinter, times(1)).print(value);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);
        privatePrint.invoke(spyPrinter, value, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    public void testPrint_PrivatePrintMethodInvocation() throws Exception {
        String value = "abc";
        String strValue = value.toString();

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Call public print with value to set up
        printer.print(value);

        // Directly invoke private print method to ensure accessibility and no exception
        privatePrint.invoke(printer, value, strValue, 0, strValue.length());
    }
}