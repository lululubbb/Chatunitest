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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_4_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    private Method privatePrintMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
        privatePrintMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrintMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_formatNullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_formatNullStringNotNull() throws IOException {
        final String nullStr = "NULL";
        when(format.getNullString()).thenReturn(nullStr);
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String val = "abc";
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(val);
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintMethod_invocation() throws IOException, InvocationTargetException, IllegalAccessException {
        String val = "hello";
        privatePrintMethod.invoke(printer, val, val, 0, val.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintMethod_nullObject() throws Exception {
        // Pass "" as CharSequence to avoid NullPointerException in length()
        privatePrintMethod.invoke(printer, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintMethod_emptyString() throws Exception {
        privatePrintMethod.invoke(printer, "", "", 0, 0);
    }
}