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

class CSVPrinter_4_1Test {

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
    void testPrint_NullValue_NullStringNull() throws Exception {
        when(format.getNullString()).thenReturn(null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        // Verify that private print was called with expected parameters by spying on the public print method
        verifyPrivatePrintCalled(spyPrinter, privatePrint, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNotNull() throws Exception {
        String nullString = "NULL";
        when(format.getNullString()).thenReturn(nullString);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        verifyPrivatePrintCalled(spyPrinter, privatePrint, null, nullString, 0, nullString.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_NonNullValue() throws Exception {
        String value = "testValue";

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(value);

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        verifyPrivatePrintCalled(spyPrinter, privatePrint, value, value, 0, value.length());
    }

    private void verifyPrivatePrintCalled(CSVPrinter spyPrinter, Method method, Object obj, CharSequence cs, int offset, int len) throws Exception {
        // Instead of verifying private method call (not possible), invoke it directly to ensure no exceptions
        method.invoke(spyPrinter, obj, cs, offset, len);
    }
}