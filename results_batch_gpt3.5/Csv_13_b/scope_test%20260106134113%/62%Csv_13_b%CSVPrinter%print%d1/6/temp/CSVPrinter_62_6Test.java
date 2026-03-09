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

import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_62_6Test {

    private CSVPrinter printer;
    private CSVFormat format;
    private Appendable out;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_QuoteCharacterSet() throws Throwable {
        setField(printer, "newRecord", true);
        when(format.isQuoteCharacterSet()).thenReturn(true);
        when(format.isEscapeCharacterSet()).thenReturn(false);

        CharSequence value = "value";
        Object object = new Object();

        invokePrint(printer, object, value, 0, value.length());

        // Should call printAndQuote, no delimiter appended since newRecord true
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_QuoteCharacterSet() throws Throwable {
        setField(printer, "newRecord", false);
        when(format.isQuoteCharacterSet()).thenReturn(true);
        when(format.isEscapeCharacterSet()).thenReturn(false);
        when(format.getDelimiter()).thenReturn(',');

        CharSequence value = "value";
        Object object = new Object();

        invokePrint(printer, object, value, 0, value.length());

        // Should append delimiter once
        verify(out).append(',');
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_EscapeCharacterSet() throws Throwable {
        setField(printer, "newRecord", false);
        when(format.isQuoteCharacterSet()).thenReturn(false);
        when(format.isEscapeCharacterSet()).thenReturn(true);
        when(format.getDelimiter()).thenReturn(';');

        CharSequence value = "escapeValue";
        Object object = new Object();

        invokePrint(printer, object, value, 0, value.length());

        verify(out).append(';');
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuoteNoEscape() throws Throwable {
        setField(printer, "newRecord", false);
        when(format.isQuoteCharacterSet()).thenReturn(false);
        when(format.isEscapeCharacterSet()).thenReturn(false);
        when(format.getDelimiter()).thenReturn('|');

        CharSequence value = "abcdef";
        Object object = new Object();

        invokePrint(printer, object, value, 1, 3);

        // Should append delimiter
        verify(out).append('|');
        // Should append value from offset 1 to offset+len = 1+3=4
        verify(out).append(value, 1, 4);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_NoQuoteNoEscape() throws Throwable {
        setField(printer, "newRecord", true);
        when(format.isQuoteCharacterSet()).thenReturn(false);
        when(format.isEscapeCharacterSet()).thenReturn(false);

        CharSequence value = "abcdef";
        Object object = new Object();

        invokePrint(printer, object, value, 2, 2);

        // Should not append delimiter
        verify(out, never()).append(anyChar());
        verify(out).append(value, 2, 4);
    }

    // Helper to invoke private print method
    private void invokePrint(CSVPrinter printer, Object object, CharSequence value, int offset, int len) throws Throwable {
        try {
            Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
            printMethod.setAccessible(true);
            printMethod.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Helper to set private field via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}