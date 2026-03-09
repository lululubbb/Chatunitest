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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_61_6Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private Method privatePrintMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IOException {
        MockitoAnnotations.openMocks(this);
        printer = new CSVPrinter(out, format);

        privatePrintMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrintMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws IOException, InvocationTargetException, IllegalAccessException {
        when(format.getNullString()).thenReturn(null);

        CSVPrinter spyPrinter = spy(printer);

        // Do not stub private method, call real method to test behavior
        doCallRealMethod().when(spyPrinter).print(any());

        spyPrinter.print(null);

        verify(format).getNullString();

        verifyPrivatePrintCalled(spyPrinter, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull() throws IOException, InvocationTargetException, IllegalAccessException {
        String nullString = "NULL";
        when(format.getNullString()).thenReturn(nullString);

        CSVPrinter spyPrinter = spy(printer);

        doCallRealMethod().when(spyPrinter).print(any());

        spyPrinter.print(null);

        verify(format).getNullString();

        verifyPrivatePrintCalled(spyPrinter, null, nullString, 0, nullString.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException, InvocationTargetException, IllegalAccessException {
        String value = "testValue";

        CSVPrinter spyPrinter = spy(printer);

        doCallRealMethod().when(spyPrinter).print(any());

        spyPrinter.print(value);

        verify(format, never()).getNullString();

        verifyPrivatePrintCalled(spyPrinter, value, value, 0, value.length());
    }

    private void verifyPrivatePrintCalled(CSVPrinter spyPrinter, Object expectedObject, CharSequence expectedValue, int expectedOffset, int expectedLen) throws InvocationTargetException, IllegalAccessException {
        privatePrintMethod.invoke(spyPrinter, expectedObject, expectedValue, expectedOffset, expectedLen);
    }
}