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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVPrinter_5_2Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordFalse_Quoting() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Arrange
        setNewRecord(printer, false);
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        String value = "value";
        int offset = 0;
        int len = value.length();

        CSVPrinter spyPrinter = spy(printer);

        // Act
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        printMethod.invoke(spyPrinter, "object", value, offset, len);

        // Assert
        verify(out).append(',');
        // We cannot verify private method calls directly with Mockito, so rely on side effects.
        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordTrue_Quoting() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Arrange
        setNewRecord(printer, true);
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        String value = "val";
        int offset = 0;
        int len = value.length();

        CSVPrinter spyPrinter = spy(printer);

        // Act
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        printMethod.invoke(spyPrinter, "obj", value, offset, len);

        // Assert
        verify(out, never()).append(anyChar());
        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordFalse_Escaping() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Arrange
        setNewRecord(printer, false);
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        String value = "escapeTest";
        int offset = 1;
        int len = 4;

        CSVPrinter spyPrinter = spy(printer);

        // Act
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        printMethod.invoke(spyPrinter, "obj", value, offset, len);

        // Assert
        verify(out).append(';');
        assertFalse(getNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    public void testPrint_NewRecordTrue_NoQuotingNoEscaping() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Arrange
        setNewRecord(printer, true);
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        String value = "abcdef";
        int offset = 2;
        int len = 3;

        // Act
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        printMethod.invoke(printer, "obj", value, offset, len);

        // Assert
        verify(out).append(value, offset, offset + len);
        assertFalse(getNewRecord(printer));
    }

    // Helper to set private boolean newRecord field
    private void setNewRecord(CSVPrinter printer, boolean value) throws NoSuchFieldException, IllegalAccessException {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printer, value);
    }

    // Helper to get private boolean newRecord field
    private boolean getNewRecord(CSVPrinter printer) throws NoSuchFieldException, IllegalAccessException {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        return field.getBoolean(printer);
    }
}