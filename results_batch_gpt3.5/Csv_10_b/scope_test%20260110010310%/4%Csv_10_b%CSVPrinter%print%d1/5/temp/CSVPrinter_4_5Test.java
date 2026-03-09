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

import org.apache.commons.csv.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_4_5Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
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
    void testPrint_nullValue_nullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);

        printer.print(null);

        // verify that print(Object, CharSequence, int, int) called with empty string
        try {
            Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
            printMethod.setAccessible(true);
            // We cannot verify internal calls directly, but we can verify interactions on out or format
            // Since print ultimately writes to 'out', verify that out was used (indirectly)
            verify(format).getNullString();
        } catch (NoSuchMethodException e) {
            fail("print method not found");
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");

        printer.print(null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "test";

        printer.print(value);

        // verify that format.getNullString() is not called
        verify(format, never()).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintInvocation() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String value = "valueToPrint";

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // invoke private print with value and substring parameters
        privatePrint.invoke(printer, value, value, 0, value.length());

        // No exceptions means success
    }
}