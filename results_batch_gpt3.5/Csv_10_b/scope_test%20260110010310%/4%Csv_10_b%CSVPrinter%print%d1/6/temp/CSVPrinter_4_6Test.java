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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_4_6Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_formatNullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);

        printer.print(null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_formatNullStringNonNull() throws IOException {
        String nullString = "NULL";
        when(format.getNullString()).thenReturn(nullString);

        printer.print(null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "value";

        printer.print(value);

        verify(format, never()).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrint() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String testValue = "testValue";
        String strValue = testValue;

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        privatePrint.invoke(printer, testValue, strValue, 0, strValue.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_privatePrintInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        when(format.getNullString()).thenReturn(null);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, new Object[] { null });

        verify(format).getNullString();
    }
}