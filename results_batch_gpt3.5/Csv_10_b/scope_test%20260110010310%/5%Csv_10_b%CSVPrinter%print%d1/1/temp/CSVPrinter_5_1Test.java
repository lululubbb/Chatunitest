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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinterPrintTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Quoting() throws Throwable {
        // Arrange
        setField(printer, "newRecord", false);
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        CharSequence value = "value";
        Object object = 123; // number to test quoting with number

        // Act
        invokePrint(printer, object, value, 0, value.length());

        // Assert
        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).getDelimiter();
        inOrder.verify(out).append(',');
        // printAndQuote appends quoted value, so verify out.append called at least once
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        // newRecord is set to false
        assertField(printer, "newRecord", false);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_Quoting() throws Throwable {
        // Arrange
        setField(printer, "newRecord", true);
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        CharSequence value = "quoted";
        Object object = "text";

        // Act
        invokePrint(printer, object, value, 0, value.length());

        // Assert
        verify(out, never()).append(anyChar());
        // printAndQuote called and appends something
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        assertField(printer, "newRecord", false);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Escaping() throws Throwable {
        // Arrange
        setField(printer, "newRecord", false);
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        CharSequence value = "escape";
        Object object = "escape";

        // Act
        invokePrint(printer, object, value, 1, 4);

        // Assert
        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).getDelimiter();
        inOrder.verify(out).append(';');
        // printAndEscape appends something
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        assertField(printer, "newRecord", false);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_NoQuotingNoEscaping() throws Throwable {
        // Arrange
        setField(printer, "newRecord", true);
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        CharSequence value = "plain text";
        Object object = "plain text";

        // Act
        invokePrint(printer, object, value, 6, 4);

        // Assert
        verify(out, never()).append(anyChar());
        verify(out, never()).append(any(CharSequence.class), eq(0), eq(4));
        verify(out).append(value, 6, 10);
        assertField(printer, "newRecord", false);
    }

    // Helper method to invoke private print method via reflection
    private void invokePrint(CSVPrinter printer, Object object, CharSequence value, int offset, int len) throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Helper method to set private field via reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to assert private field value
    private void assertField(Object target, String fieldName, Object expected) {
        try {
            java.lang.reflect.Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object actual = field.get(target);
            if (expected == null) {
                if (actual != null) {
                    throw new AssertionError(fieldName + " expected null but was " + actual);
                }
            } else if (!expected.equals(actual)) {
                throw new AssertionError(fieldName + " expected " + expected + " but was " + actual);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}