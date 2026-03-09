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

class CSVPrinter_61_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    private void invokePrivatePrint(CSVPrinter spyPrinter, Object expectedObject, CharSequence expectedValue, int expectedOffset, int expectedLen) throws Exception {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        printMethod.invoke(spyPrinter, expectedObject, expectedValue, expectedOffset, expectedLen);
    }

    @Test
    @Timeout(8000)
    void testPrint_withNullValue_andNullStringNull() throws Exception {
        when(format.getNullString()).thenReturn(null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print((Object) null);

        String expectedValue = "";
        invokePrivatePrint(spyPrinter, null, expectedValue, 0, expectedValue.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_withNullValue_andNullStringNotNull() throws Exception {
        String nullString = "NULL";
        when(format.getNullString()).thenReturn(nullString);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print((Object) null);

        invokePrivatePrint(spyPrinter, null, nullString, 0, nullString.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_withNonNullValue() throws Exception {
        String value = "testValue";

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(value);

        invokePrivatePrint(spyPrinter, value, value, 0, value.length());
    }
}