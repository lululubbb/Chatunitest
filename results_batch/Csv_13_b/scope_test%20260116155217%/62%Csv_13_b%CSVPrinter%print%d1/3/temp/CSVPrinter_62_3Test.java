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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_62_3Test {

    private CSVPrinter csvPrinter;
    private Appendable appendableMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendableMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_QuoteCharacterSet() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Setup
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(',');

        // Access private field newRecord and set to true
        setPrivateField(csvPrinter, "newRecord", true);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Spy on csvPrinter to verify interactions with appendableMock
        CSVPrinter spyPrinter = spy(csvPrinter);
        setPrivateField(spyPrinter, "newRecord", true);

        // Invoke print with newRecord = true, so delimiter should NOT be appended
        assertDoesNotThrow(() -> printMethod.invoke(spyPrinter, "obj", "value", 0, 5));

        // Verify delimiter not appended because newRecord is true
        verify(appendableMock, never()).append(',');

        // newRecord should be false after invocation
        boolean newRecordValue = (boolean) getPrivateField(spyPrinter, "newRecord");
        assert !newRecordValue;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_QuoteCharacterSet() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.isQuoteCharacterSet()).thenReturn(true);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(';');

        setPrivateField(csvPrinter, "newRecord", false);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CSVPrinter spyPrinter = spy(csvPrinter);
        setPrivateField(spyPrinter, "newRecord", false);

        printMethod.invoke(spyPrinter, "obj", "value", 1, 3);

        verify(appendableMock).append(';');

        boolean newRecordValue = (boolean) getPrivateField(spyPrinter, "newRecord");
        assert !newRecordValue;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_EscapeCharacterSet() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(true);
        when(formatMock.getDelimiter()).thenReturn('|');

        setPrivateField(csvPrinter, "newRecord", false);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CSVPrinter spyPrinter = spy(csvPrinter);
        setPrivateField(spyPrinter, "newRecord", false);

        printMethod.invoke(spyPrinter, null, "escapeValue", 2, 4);

        verify(appendableMock).append('|');

        boolean newRecordValue = (boolean) getPrivateField(spyPrinter, "newRecord");
        assert !newRecordValue;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuoteNoEscape() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        when(formatMock.isQuoteCharacterSet()).thenReturn(false);
        when(formatMock.isEscapeCharacterSet()).thenReturn(false);
        when(formatMock.getDelimiter()).thenReturn(':');

        setPrivateField(csvPrinter, "newRecord", false);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CSVPrinter spyPrinter = spy(csvPrinter);
        setPrivateField(spyPrinter, "newRecord", false);

        printMethod.invoke(spyPrinter, null, "abcdef", 1, 3);

        verify(appendableMock).append(':');
        verify(appendableMock).append("abcdef", 1, 4);

        boolean newRecordValue = (boolean) getPrivateField(spyPrinter, "newRecord");
        assert !newRecordValue;
    }

    // Utility methods for reflection to set/get private fields

    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = getField(target instanceof java.lang.reflect.Proxy ? target.getClass().getSuperclass() : target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getPrivateField(Object target, String fieldName) {
        try {
            Field field = getField(target instanceof java.lang.reflect.Proxy ? target.getClass().getSuperclass() : target.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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