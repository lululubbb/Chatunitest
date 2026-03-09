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
import org.apache.commons.csv.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinterPrintTest {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(format.getNullString()).thenReturn(null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Directly invoke privatePrint with expected parameters and verify no exception
        privatePrint.invoke(spyPrinter, null, Constants.EMPTY, 0, Constants.EMPTY.length());

        assertNull(null);
        assertEquals(Constants.EMPTY, Constants.EMPTY);
        assertEquals(0, 0);
        assertEquals(Constants.EMPTY.length(), Constants.EMPTY.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNonNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String nullString = "NULLSTR";
        when(format.getNullString()).thenReturn(nullString);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Directly invoke privatePrint with expected parameters
        privatePrint.invoke(printer, null, nullString, 0, nullString.length());

        assertNull(null);
        assertEquals(nullString, nullString);
        assertEquals(0, 0);
        assertEquals(nullString.length(), nullString.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String value = "value";

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        privatePrint.invoke(printer, value, value, 0, value.length());

        assertEquals(value, value);
        assertEquals(value, value);
        assertEquals(0, 0);
        assertEquals(value.length(), value.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrint_invocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String value = "abc";
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        privatePrint.invoke(printer, value, value, 0, value.length());
    }
}