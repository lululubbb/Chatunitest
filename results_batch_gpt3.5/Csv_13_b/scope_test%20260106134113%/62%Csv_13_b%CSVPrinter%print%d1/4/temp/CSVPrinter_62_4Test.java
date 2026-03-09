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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterPrintTest {

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

        CSVPrinter spyPrinter = spy(printer);
        setNewRecord(spyPrinter, true);

        CharSequence value = "value";
        Object object = Integer.valueOf(123);
        int offset = 0;
        int len = 5;

        printMethod.invoke(spyPrinter, object, value, offset, len);

        verify(outMock, never()).append(anyChar());
        verify(spyPrinter).printAndQuote(object, value, offset, len);

        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_QuoteCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        CSVPrinter spyPrinter = spy(printer);
        setNewRecord(spyPrinter, false);

        CharSequence value = "val";
        Object object = "obj";
        int offset = 1;
        int len = 2;

        printMethod.invoke(spyPrinter, object, value, offset, len);

        verify(outMock).append(',');
        verify(spyPrinter).printAndQuote(object, value, offset, len);
        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_EscapeCharacterSet() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        when(formatMock.getDelimiter()).thenReturn(';');

        CSVPrinter spyPrinter = spy(printer);
        setNewRecord(spyPrinter, false);

        CharSequence value = "escapeTest";
        Object object = new Object();
        int offset = 2;
        int len = 4;

        printMethod.invoke(spyPrinter, object, value, offset, len);

        verify(outMock).append(';');
        verify(spyPrinter).printAndEscape(value, offset, len);
        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_NoQuoteNoEscape() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);
        setNewRecord(spyPrinter, true);

        CharSequence value = "abcdef";
        Object object = null;
        int offset = 1;
        int len = 3;

        printMethod.invoke(spyPrinter, object, value, offset, len);

        verify(outMock, never()).append(anyChar());
        verify(outMock).append(value, offset, offset + len);

        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuoteNoEscape() throws Throwable {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn('|');

        CSVPrinter spyPrinter = spy(printer);
        setNewRecord(spyPrinter, false);

        CharSequence value = "123456789";
        Object object = "obj";
        int offset = 3;
        int len = 4;

        printMethod.invoke(spyPrinter, object, value, offset, len);

        verify(outMock).append('|');
        verify(outMock).append(value, offset, offset + len);

        assertFalse(getNewRecord(spyPrinter));
    }

    // Utility to set private field newRecord
    private void setNewRecord(CSVPrinter printerInstance, boolean value) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            field.setBoolean(printerInstance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Utility to get private field newRecord
    private boolean getNewRecord(CSVPrinter printerInstance) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            return field.getBoolean(printerInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}