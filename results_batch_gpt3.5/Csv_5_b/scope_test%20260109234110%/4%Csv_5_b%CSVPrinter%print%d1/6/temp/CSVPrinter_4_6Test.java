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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_4_6Test {

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
    void testPrintNullValue_nullStringNull() throws IOException {
        when(format.getNullString()).thenReturn(null);
        // Call print with null value
        printer.print(null);

        // Verify out.append was called at least once with any CharSequence
        verifyAppendCalledAtLeastOnce();
    }

    @Test
    @Timeout(8000)
    void testPrintNullValue_nullStringNonNull() throws IOException {
        final String nullStr = "NULLSTR";
        when(format.getNullString()).thenReturn(nullStr);
        printer.print(null);

        // verify out.append called with nullStr
        verifyAppendCalledAtLeastOnceWith(contains(nullStr));
    }

    @Test
    @Timeout(8000)
    void testPrintNonNullValue() throws IOException {
        String value = "testValue";
        printer.print(value);

        // verify out.append called with string containing value
        verifyAppendCalledAtLeastOnceWith(contains(value));
    }

    @Test
    @Timeout(8000)
    void testPrintPrivateMethodInvocation() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        String obj = "objVal";
        String val = "valVal";

        // invoke private print method
        printMethod.invoke(printer, obj, val, 0, val.length());

        // verify out.append called at least once
        verifyAppendCalledAtLeastOnce();
    }

    private void verifyAppendCalledAtLeastOnce() {
        try {
            verify(out, atLeastOnce()).append(any(CharSequence.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyAppendCalledAtLeastOnceWith(CharSequence seq) {
        try {
            verify(out, atLeastOnce()).append(seq);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}