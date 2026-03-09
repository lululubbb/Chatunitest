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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterPrintTest {

    private CSVPrinter csvPrinter;
    private CSVFormat formatMock;
    private Appendable appendableMock;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendableMock, formatMock);
    }

    private Method getPrintMethod() throws NoSuchMethodException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_QuoteCharacterSet() throws Throwable {
        // Arrange
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);

        setField(csvPrinter, "newRecord", true);

        CharSequence value = "testValue";
        Object object = Integer.valueOf(123);

        // Act
        Method printMethod = getPrintMethod();
        printMethod.invoke(csvPrinter, object, value, 0, value.length());

        // Assert
        // Because newRecord is true, no delimiter appended
        verify(appendableMock, never()).append(anyChar());

        // printAndQuote is private, we verify appendable append calls indirectly by spying CSVPrinter
        // Here just verify that newRecord becomes false
        assertFalse(getField(csvPrinter, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_QuoteCharacterSet() throws Throwable {
        // Arrange
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        setField(csvPrinter, "newRecord", false);

        CharSequence value = "testValue";
        Object object = "object";

        // Act
        Method printMethod = getPrintMethod();
        printMethod.invoke(csvPrinter, object, value, 0, value.length());

        // Assert
        verify(appendableMock).append(delimiter);
        assertFalse(getField(csvPrinter, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_EscapeCharacterSet() throws Throwable {
        // Arrange
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        char delimiter = ';';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        setField(csvPrinter, "newRecord", false);

        CharSequence value = "escapedValue";
        Object object = "object";

        // Act
        Method printMethod = getPrintMethod();
        printMethod.invoke(csvPrinter, object, value, 0, value.length());

        // Assert
        verify(appendableMock).append(delimiter);
        assertFalse(getField(csvPrinter, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuoteNoEscape() throws Throwable {
        // Arrange
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        char delimiter = '|';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        setField(csvPrinter, "newRecord", false);

        CharSequence value = "plainValue";
        Object object = "object";

        // Act
        Method printMethod = getPrintMethod();
        printMethod.invoke(csvPrinter, object, value, 1, 4);

        // Assert
        verify(appendableMock).append(delimiter);
        verify(appendableMock).append(value, 1, 5);
        assertFalse(getField(csvPrinter, "newRecord"));
    }

    @Test
    @Timeout(8000)
    void testPrint_IOExceptionPropagated() throws Throwable {
        // Arrange
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        char delimiter = ',';
        when(formatMock.getDelimiter()).thenReturn(delimiter);

        setField(csvPrinter, "newRecord", false);

        CharSequence value = "value";
        Object object = "object";

        doThrow(new IOException("append failed")).when(appendableMock).append(value, 0, value.length());

        // Act & Assert
        Method printMethod = getPrintMethod();
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            printMethod.invoke(csvPrinter, object, value, 0, value.length());
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("append failed", thrown.getCause().getMessage());
    }

    // Helper to set private field via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to get private field via reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object target, String fieldName) {
        try {
            Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to find field in class hierarchy
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