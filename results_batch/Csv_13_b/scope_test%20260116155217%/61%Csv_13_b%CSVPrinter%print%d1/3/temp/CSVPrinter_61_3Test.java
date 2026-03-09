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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVPrinter_61_3Test {

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
    public void testPrint_NullValue_NullStringNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(format.getNullString()).thenReturn(null);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        verify(format).getNullString();

        // Reflectively invoke the private print method with expected parameters
        printMethod.invoke(spyPrinter, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NullValue_NullStringNotNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String nullString = "NULL";
        when(format.getNullString()).thenReturn(nullString);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        verify(format).getNullString();

        printMethod.invoke(spyPrinter, null, nullString, 0, nullString.length());
    }

    @Test
    @Timeout(8000)
    public void testPrint_NonNullValue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String value = "testValue";

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(value);

        printMethod.invoke(spyPrinter, value, value, 0, value.length());
    }
}