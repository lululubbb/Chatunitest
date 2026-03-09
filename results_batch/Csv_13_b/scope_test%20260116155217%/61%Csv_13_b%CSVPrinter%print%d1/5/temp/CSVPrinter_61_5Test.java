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

public class CSVPrinter_61_5Test {

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
    public void testPrint_nullValue_nullStringNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(format.getNullString()).thenReturn(null);

        // Use spy to allow partial mocking
        CSVPrinter spyPrinter = spy(printer);

        // Call public print method with null
        spyPrinter.print(null);

        // Access private print method with correct parameter types
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Invoke private print method with expected arguments: null, empty string, 0, 0
        printMethod.invoke(spyPrinter, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    public void testPrint_nullValue_nullStringNotNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");

        printer.print(null);

        // no exception means success
    }

    @Test
    @Timeout(8000)
    public void testPrint_nonNullValue() throws IOException {
        String value = "value";
        printer.print(value);

        // no exception means success
    }

    @Test
    @Timeout(8000)
    public void testPrint_nonNullValue_emptyString() throws IOException {
        Object value = new Object() {
            @Override
            public String toString() {
                return "";
            }
        };

        printer.print(value);

        // no exception means success
    }
}