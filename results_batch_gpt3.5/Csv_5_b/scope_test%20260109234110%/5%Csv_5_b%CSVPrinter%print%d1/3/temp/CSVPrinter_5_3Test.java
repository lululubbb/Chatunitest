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
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_5_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_Quoting() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        // Set newRecord = true by reflection
        setNewRecord(printer, true);

        CharSequence value = "value";
        Object object = "object";

        // Invoke private print method
        invokePrint(printer, object, value, 0, value.length());

        InOrder inOrder = inOrder(out, format);
        // Since newRecord = true, no delimiter appended
        verify(format, times(1)).isQuoting();
        verify(format, times(0)).isEscaping();
        verify(out, never()).append(',');

        // After print, newRecord should be false
        assertFalse(getNewRecord(printer));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Quoting() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        setNewRecord(printer, false);

        CharSequence value = "value";
        Object object = "object";

        invokePrint(printer, object, value, 0, value.length());

        verify(out).append(',');
        verify(format, times(1)).isQuoting();
        verify(format, times(0)).isEscaping();

        assertFalse(getNewRecord(printer));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Escaping() throws Throwable {
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        setNewRecord(printer, false);

        CharSequence value = "escape";
        Object object = "obj";

        invokePrint(printer, object, value, 0, value.length());

        verify(out).append(';');
        verify(format, times(0)).isQuoting();
        verify(format, times(1)).isEscaping();

        assertFalse(getNewRecord(printer));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuotingNoEscaping() throws Throwable {
        when(format.getDelimiter()).thenReturn('|');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        setNewRecord(printer, false);

        CharSequence value = "abcdef";
        Object object = "obj";

        // Correct len parameter to be length of substring: offset=1, len=3 means substring from 1 to 4 exclusive
        invokePrint(printer, object, value, 1, 3);

        verify(out).append('|');
        verify(format, times(0)).isQuoting();
        verify(format, times(0)).isEscaping();
        verify(out).append(value, 1, 4);

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

    // Helper to set private field newRecord
    private void setNewRecord(CSVPrinter printer, boolean value) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.setBoolean(printer, value);
    }

    // Helper to get private field newRecord
    private boolean getNewRecord(CSVPrinter printer) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        return field.getBoolean(printer);
    }
}