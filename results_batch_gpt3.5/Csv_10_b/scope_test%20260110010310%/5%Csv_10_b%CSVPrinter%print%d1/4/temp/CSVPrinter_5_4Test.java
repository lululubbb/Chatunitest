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

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinterPrintTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_quoting() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", true);

        String value = "value";
        Object object = new Object();

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, object, value, 0, value.length());

        // Because newRecord was true, no delimiter appended before
        verify(out, never()).append(',');

        // Quoting enabled, so printAndQuote should be called internally.
        // We cannot directly verify private method call, but we can verify interactions with out.
        // printAndQuote appends quotes and value, so verify out.append called at least once.
        verify(out, atLeastOnce()).append(any(CharSequence.class));
        assertFalse(getField(printer, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_quoting() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", false);

        String value = "value";
        Object object = new Object();

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, object, value, 0, value.length());

        // Because newRecord was false, delimiter appended before
        verify(out).append(',');

        verify(out, atLeastOnce()).append(any(CharSequence.class));
        assertFalse(getField(printer, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_escaping() throws Throwable {
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", false);

        String value = "escapeTest";
        Object object = new Object();

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, object, value, 0, value.length());

        verify(out).append(';');
        verify(out, atLeastOnce()).append(any(CharSequence.class));
        assertFalse(getField(printer, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_noQuotingNoEscaping() throws Throwable {
        when(format.getDelimiter()).thenReturn('|');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        printer = new CSVPrinter(out, format);
        setField(printer, "newRecord", false);

        String value = "plainValue";
        Object object = new Object();

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, object, value, 0, value.length());

        verify(out).append('|');
        verify(out).append(value, 0, value.length());
        assertFalse(getField(printer, "newRecord"));
    }

    // Utility to set private field
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Utility to get private boolean field
    private boolean getField(CSVPrinter target, String fieldName) {
        try {
            java.lang.reflect.Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getBoolean(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}