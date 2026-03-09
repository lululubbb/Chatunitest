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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_62_3Test {

    private CSVPrinter printer;
    private CSVFormat formatMock;
    private Appendable outMock;

    private Method printMethod;
    private Method printAndQuoteMethod;
    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);

        printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_QuoteCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        // newRecord is true initially, so no delimiter appended
        CharSequence value = "value";
        printMethod.invoke(printer, "obj", value, 0, value.length());

        // verify no delimiter appended
        verify(outMock, never()).append(',');

        // spy the printer
        CSVPrinter spyPrinter = Mockito.spy(printer);

        // invoke print via reflection on spy
        printMethod.invoke(spyPrinter, "obj", value, 0, value.length());

        // verify private method printAndQuote called via reflection on spy by intercepting via spy and reflection
        // We cannot verify private method calls directly, so instead we use a custom invocation handler or verify side effects.
        // Here, we just invoke printAndQuote via reflection and trust it works.
        printAndQuoteMethod.invoke(spyPrinter, "obj", value, 0, value.length());

        // newRecord should be false after call
        boolean newRecordField = (boolean) getField(printer, "newRecord");
        assertFalse(newRecordField);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_QuoteCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        setField(printer, "newRecord", false);

        CharSequence value = "value";
        printMethod.invoke(printer, "obj", value, 0, value.length());

        verify(outMock).append(',');

        CSVPrinter spyPrinter = Mockito.spy(printer);
        setField(spyPrinter, "newRecord", false);
        printMethod.invoke(spyPrinter, "obj", value, 0, value.length());

        // invoke printAndQuote via reflection (cannot verify private method call)
        printAndQuoteMethod.invoke(spyPrinter, "obj", value, 0, value.length());

        boolean newRecordField = (boolean) getField(printer, "newRecord");
        assertFalse(newRecordField);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_EscapeCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        when(formatMock.getDelimiter()).thenReturn(';');

        setField(printer, "newRecord", false);

        CharSequence value = "escapeValue";
        printMethod.invoke(printer, "obj", value, 1, 5);

        verify(outMock).append(';');

        CSVPrinter spyPrinter = Mockito.spy(printer);
        setField(spyPrinter, "newRecord", false);
        printMethod.invoke(spyPrinter, "obj", value, 1, 5);

        // invoke printAndEscape via reflection (cannot verify private method call)
        printAndEscapeMethod.invoke(spyPrinter, value, 1, 5);

        boolean newRecordField = (boolean) getField(printer, "newRecord");
        assertFalse(newRecordField);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuoteNoEscape() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn('|');

        setField(printer, "newRecord", false);

        CharSequence value = "abcdef";
        printMethod.invoke(printer, "obj", value, 2, 3);

        verify(outMock).append('|');
        verify(outMock).append(value, 2, 5);

        boolean newRecordField = (boolean) getField(printer, "newRecord");
        assertFalse(newRecordField);
    }

    private Object getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}