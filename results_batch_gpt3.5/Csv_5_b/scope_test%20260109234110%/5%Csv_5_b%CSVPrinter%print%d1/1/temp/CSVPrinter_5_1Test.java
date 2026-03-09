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

class CSVPrinter_5_1Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Quoting() throws Throwable {
        // Arrange
        setNewRecord(printer, false);
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        // Spy on printer to verify private printAndQuote called
        CSVPrinter spyPrinter = spy(printer);

        CharSequence value = "value";
        Object object = "value";
        int offset = 0;
        int len = value.length();

        // Act
        invokePrint(spyPrinter, object, value, offset, len);

        // Assert
        InOrder inOrder = inOrder(out, spyPrinter);
        inOrder.verify(out).append(',');
        inOrder.verify(spyPrinter).printAndQuote(object, value, offset, len);

        // newRecord should be false after print
        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_Quoting() throws Throwable {
        // Arrange
        setNewRecord(printer, true);
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);

        CharSequence value = "val";
        Object object = 123; // Number to test quoting with number
        int offset = 0;
        int len = value.length();

        // Act
        invokePrint(spyPrinter, object, value, offset, len);

        // Assert
        // No delimiter appended because newRecord true
        verify(out, never()).append(',');

        verify(spyPrinter).printAndQuote(object, value, offset, len);

        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Escaping() throws Throwable {
        // Arrange
        setNewRecord(printer, false);
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        CSVPrinter spyPrinter = spy(printer);

        CharSequence value = "escape";
        Object object = value;
        int offset = 1;
        int len = 3;

        // Act
        invokePrint(spyPrinter, object, value, offset, len);

        // Assert
        InOrder inOrder = inOrder(out, spyPrinter);
        inOrder.verify(out).append(';');
        inOrder.verify(spyPrinter).printAndEscape(value, offset, len);

        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_NoQuoteNoEscape() throws Throwable {
        // Arrange
        setNewRecord(printer, true);
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        CharSequence value = "abcdef";
        Object object = value;
        int offset = 2;
        int len = 3; // substring "cde"

        // Act
        invokePrint(printer, object, value, offset, len);

        // Assert
        verify(out).append(value, offset, offset + len);

        assertFalse(getNewRecord(printer));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuoteNoEscape() throws Throwable {
        // Arrange
        setNewRecord(printer, false);
        when(format.getDelimiter()).thenReturn('|');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        CharSequence value = "xyz";
        Object object = value;
        int offset = 0;
        int len = 3;

        // Act
        invokePrint(printer, object, value, offset, len);

        // Assert
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('|');
        inOrder.verify(out).append(value, offset, offset + len);

        assertFalse(getNewRecord(printer));
    }

    // Helper to invoke private print method
    private void invokePrint(CSVPrinter printer, Object object, CharSequence value, int offset, int len) throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Helper to set private newRecord field
    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, value);
    }

    // Helper to get private newRecord field
    private boolean getNewRecord(CSVPrinter printer) throws Exception {
        var field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        return field.getBoolean(printer);
    }
}