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

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringIsNull() throws Exception {
        when(format.getNullString()).thenReturn(null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Verify private method invocation via reflection
        printMethod.invoke(spyPrinter, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringIsNonNull() throws Exception {
        when(format.getNullString()).thenReturn("NULL");

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Verify private method invocation via reflection
        printMethod.invoke(spyPrinter, null, "NULL", 0, 4);
    }

    @Test
    @Timeout(8000)
    void testPrint_NonNullValue() throws Exception {
        String value = "value";

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(value);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Verify private method invocation via reflection
        printMethod.invoke(spyPrinter, value, value, 0, value.length());
    }
}