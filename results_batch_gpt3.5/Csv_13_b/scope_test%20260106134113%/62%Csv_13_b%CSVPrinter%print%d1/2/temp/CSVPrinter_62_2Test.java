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
import org.mockito.Mockito;

public class CSVPrinter_62_2Test {

    private CSVPrinter csvPrinter;
    private Appendable outMock;
    private CSVFormat formatMock;

    private Method printMethod;
    private Method printAndQuoteMethod;
    private Method printAndEscapeMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);

        // Use real constructor with mocks; declare IOException thrown
        csvPrinter = new CSVPrinter(outMock, formatMock);

        // Access private print method
        printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Access private printAndQuote method
        printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        // Access private printAndEscape method
        printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordTrue_QuoteCharacterSet() throws Throwable {
        // Setup newRecord = true (initial state is true)
        // format.isQuoteCharacterSet() returns true
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);

        // We spy csvPrinter to verify private printAndQuote call via reflection
        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        // Use a doAnswer to intercept the private printAndQuote call via reflection
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            // call the real private method
            printAndQuoteMethod.invoke(spyPrinter, args);
            return null;
        }).when(spyPrinter).printAndQuote(any(), any(), anyInt(), anyInt());

        // Invoke print with value "abcdef", offset 1, len 3 -> "bcd"
        printMethod.invoke(spyPrinter, "obj", "abcdef", 1, 3);

        // Since newRecord was true, out.append(delimiter) should NOT be called
        verify(outMock, never()).append(anyChar());

        // Verify private printAndQuote called once with exact args via reflection
        verify(spyPrinter, times(1)).printAndQuote("obj", "abcdef", 1, 3);

        // After invocation, newRecord should be false
        boolean newRecordField = (boolean) getPrivateField(spyPrinter, "newRecord");
        assertFalse(newRecordField);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordFalse_QuoteCharacterSet() throws Throwable {
        // Setup newRecord = false
        setPrivateField(csvPrinter, "newRecord", false);

        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            printAndQuoteMethod.invoke(spyPrinter, args);
            return null;
        }).when(spyPrinter).printAndQuote(any(), any(), anyInt(), anyInt());

        printMethod.invoke(spyPrinter, 123, "value", 0, 5);

        // out.append(delimiter) should be called once with ','
        verify(outMock, times(1)).append(',');

        // Verify private printAndQuote called once with exact args via reflection
        verify(spyPrinter, times(1)).printAndQuote(123, "value", 0, 5);

        boolean newRecordField = (boolean) getPrivateField(spyPrinter, "newRecord");
        assertFalse(newRecordField);
    }

    @Test
    @Timeout(8000)
    public void testPrint_EscapeCharacterSet() throws Throwable {
        // newRecord = false to test delimiter append
        setPrivateField(csvPrinter, "newRecord", false);

        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        when(formatMock.getDelimiter()).thenReturn(';');

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            printAndEscapeMethod.invoke(spyPrinter, args);
            return null;
        }).when(spyPrinter).printAndEscape(any(), anyInt(), anyInt());

        printMethod.invoke(spyPrinter, null, "escapeTest", 2, 4);

        // out.append(delimiter) called once with ';'
        verify(outMock, times(1)).append(';');

        // Verify private printAndEscape called once with "escapeTest", 2, 4 via reflection
        verify(spyPrinter, times(1)).printAndEscape("escapeTest", 2, 4);

        boolean newRecordField = (boolean) getPrivateField(spyPrinter, "newRecord");
        assertFalse(newRecordField);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NoQuoteNoEscape() throws Throwable {
        // newRecord = false to test delimiter append
        setPrivateField(csvPrinter, "newRecord", false);

        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn('|');

        CharSequence value = "123456789";

        printMethod.invoke(csvPrinter, "obj", value, 3, 4);

        // out.append(delimiter) called once with '|'
        verify(outMock, times(1)).append('|');

        // out.append(value, offset, offset+len) called once with (3,7)
        verify(outMock, times(1)).append(value, 3, 7);

        boolean newRecordField = (boolean) getPrivateField(csvPrinter, "newRecord");
        assertFalse(newRecordField);
    }

    // Helper to get private field value via reflection
    private Object getPrivateField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to set private field value via reflection
    private void setPrivateField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}