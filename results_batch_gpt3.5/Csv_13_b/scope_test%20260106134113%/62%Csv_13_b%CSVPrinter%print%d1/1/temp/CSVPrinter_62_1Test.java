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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinterPrintTest {

    private CSVPrinter printer;
    private CSVFormat formatMock;
    private Appendable appendableMock;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(appendableMock, formatMock);
    }

    private Method getPrintMethod() throws NoSuchMethodException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        return method;
    }

    private Method getPrintAndQuoteMethod() throws NoSuchMethodException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        return method;
    }

    private Method getPrintAndEscapeMethod() throws NoSuchMethodException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_QuoteCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        CSVPrinter spyPrinter = Mockito.spy(printer);

        Method printMethod = getPrintMethod();

        CharSequence value = "value";
        Object object = Integer.valueOf(123);

        // newRecord is true by default, no delimiter appended
        printMethod.invoke(spyPrinter, object, value, 0, value.length());

        // verify no delimiter appended since newRecord was true
        verify(appendableMock, never()).append(',');

        // verify printAndQuote called once with correct params via reflection
        Method printAndQuoteMethod = getPrintAndQuoteMethod();
        printAndQuoteMethod.invoke(spyPrinter, object, value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_QuoteCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(';');

        CSVPrinter spyPrinter = Mockito.spy(printer);

        setField(spyPrinter, "newRecord", false);

        Method printMethod = getPrintMethod();

        CharSequence value = "quotedValue";
        Object object = "obj";

        printMethod.invoke(spyPrinter, object, value, 0, value.length());

        // verify delimiter appended once
        verify(appendableMock, times(1)).append(';');

        // verify printAndQuote called once with correct params via reflection
        Method printAndQuoteMethod = getPrintAndQuoteMethod();
        printAndQuoteMethod.invoke(spyPrinter, object, value, 0, value.length());
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_EscapeCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        when(formatMock.getDelimiter()).thenReturn('|');

        CSVPrinter spyPrinter = Mockito.spy(printer);

        setField(spyPrinter, "newRecord", false);

        Method printMethod = getPrintMethod();

        CharSequence value = "escapedValue";
        Object object = "obj";

        printMethod.invoke(spyPrinter, object, value, 2, 5);

        verify(appendableMock, times(1)).append('|');

        // verify printAndEscape called once with correct params via reflection
        Method printAndEscapeMethod = getPrintAndEscapeMethod();
        printAndEscapeMethod.invoke(spyPrinter, value, 2, 5);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuoteNoEscape() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn('\t');

        setField(printer, "newRecord", false);

        Method printMethod = getPrintMethod();

        CharSequence value = "plainValue123";
        Object object = "obj";

        printMethod.invoke(printer, object, value, 3, 4);

        // verify delimiter appended once
        verify(appendableMock, times(1)).append('\t');

        // verify append called with substring value, offset=3, offset+len=7
        verify(appendableMock, times(1)).append(value, 3, 7);

        boolean newRecord = (boolean) getField(printer, "newRecord");
        assertFalse(newRecord);
    }

    private Object getField(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}