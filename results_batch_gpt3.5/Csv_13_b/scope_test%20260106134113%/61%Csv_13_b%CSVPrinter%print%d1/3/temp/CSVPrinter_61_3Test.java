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

class CSVPrinter_61_3Test {

    private Appendable appendable;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        appendable = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(appendable, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(format.getNullString()).thenReturn(null);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        printMethod.invoke(printer, new Object[] { null });

        verify(format).getNullString();

        Method printPrivate = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printPrivate.setAccessible(true);
        printPrivate.invoke(printer, (Object) null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNonNull() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(format.getNullString()).thenReturn("NULL");

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        printMethod.invoke(printer, new Object[] { null });

        verify(format).getNullString();

        Method printPrivate = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printPrivate.setAccessible(true);
        printPrivate.invoke(printer, (Object) null, "NULL", 0, 4);
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String value = "testValue";

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        printMethod.invoke(printer, value);

        Method printPrivate = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printPrivate.setAccessible(true);
        printPrivate.invoke(printer, value, value, 0, value.length());
    }
}