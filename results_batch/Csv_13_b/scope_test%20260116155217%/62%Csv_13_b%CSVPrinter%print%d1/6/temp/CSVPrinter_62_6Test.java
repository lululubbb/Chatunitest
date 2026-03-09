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

class CSVPrinterPrintTest {

    private CSVPrinter csvPrinter;
    private CSVFormat formatMock;
    private Appendable outMock;

    @BeforeEach
    void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_quoteCharacterSet() throws Throwable {
        // Setup
        setField(csvPrinter, "newRecord", true);
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);

        // Spy on csvPrinter to verify private printAndQuote call
        CSVPrinter spyPrinter = spy(csvPrinter);

        // Use reflection to invoke private print method
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Call print with sample data
        CharSequence value = "value";
        printMethod.invoke(spyPrinter, "object", value, 0, value.length());

        // Verify no delimiter appended since newRecord is true
        verify(outMock, never()).append(anyChar());

        // Verify newRecord set to false
        assertFalse(getField(spyPrinter, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_quoteCharacterSet() throws Throwable {
        setField(csvPrinter, "newRecord", false);
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        CSVPrinter spyPrinter = spy(csvPrinter);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CharSequence value = "value";
        printMethod.invoke(spyPrinter, "obj", value, 0, value.length());

        // Verify delimiter appended
        verify(outMock).append(',');

        assertFalse(getField(spyPrinter, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_escapeCharacterSet() throws Throwable {
        setField(csvPrinter, "newRecord", false);
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        when(formatMock.getDelimiter()).thenReturn(';');

        CSVPrinter spyPrinter = spy(csvPrinter);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CharSequence value = "escaped";
        printMethod.invoke(spyPrinter, "obj", value, 1, 3);

        verify(outMock).append(';');

        assertFalse(getField(spyPrinter, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_noQuoteNoEscape() throws Throwable {
        setField(csvPrinter, "newRecord", false);
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn('|');

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CharSequence value = "abcdef";
        printMethod.invoke(csvPrinter, "obj", value, 2, 3);

        verify(outMock).append('|');

        verify(outMock).append(value, 2, 5);

        assertFalse(getField(csvPrinter, "newRecord"));
    }

    // Utility to set private field
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Utility to get private boolean field
    private boolean getField(Object target, String fieldName) {
        try {
            Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
            field.setAccessible(true);
            return field.getBoolean(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to get field from class or superclass
    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}