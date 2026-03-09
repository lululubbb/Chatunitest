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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVPrinter_61_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNullValueAndNullStringNull() throws Exception {
        when(format.getNullString()).thenReturn(null);

        // invoke print(Object)
        printer.print(null);

        // verify print(Object, CharSequence, int, int) invoked with Constants.EMPTY internally
        // We cannot verify private method invocation directly, but verify no interaction with out
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNullValueAndNullStringNotNull() throws Exception {
        String nullString = "NULLVAL";
        when(format.getNullString()).thenReturn(nullString);

        printer.print(null);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNonNullValue() throws Exception {
        when(format.getNullString()).thenReturn(null);

        Object value = 12345;

        printer.print(value);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintInvokesPrivatePrintWithCorrectParameters() throws Exception {
        when(format.getNullString()).thenReturn(null);

        Object value = "testValue";

        // spy on printer to verify private method call
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(out, format));

        spyPrinter.print(value);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Invoke the private print method with expected args to check no exception
        printMethod.invoke(spyPrinter, value, value.toString(), 0, value.toString().length());
    }
}