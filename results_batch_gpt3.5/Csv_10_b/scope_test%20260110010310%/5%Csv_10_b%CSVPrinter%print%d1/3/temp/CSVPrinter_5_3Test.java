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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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

    private void invokePrint(Object object, CharSequence value, int offset, int len) throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_NoDelimiter_Quoting() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));

        // Set newRecord to true initially (default), no delimiter expected on first print
        Field fieldNewRecord = CSVPrinter.class.getDeclaredField("newRecord");
        fieldNewRecord.setAccessible(true);
        fieldNewRecord.set(spyPrinter, true);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // First call: newRecord true, no delimiter appended
        printMethod.invoke(spyPrinter, "obj", "value", 0, 5);

        InOrder inOrder = inOrder(out);
        // Because printAndQuote is private, verify that out.append was called at least once during printAndQuote
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());

        // newRecord must be false after call
        assertFalse(fieldNewRecord.getBoolean(spyPrinter));

        // Prepare for second call: newRecord false, delimiter expected
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        fieldNewRecord.set(spyPrinter, false);

        printMethod.invoke(spyPrinter, "obj2", "value2", 0, 6);

        inOrder.verify(out).append(',');
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Delimiter_Quoting() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        Field fieldNewRecord = CSVPrinter.class.getDeclaredField("newRecord");
        fieldNewRecord.setAccessible(true);
        fieldNewRecord.set(printer, false);

        invokePrint("obj", "value", 0, 5);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(',');
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Delimiter_Escaping() throws Throwable {
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        Field fieldNewRecord = CSVPrinter.class.getDeclaredField("newRecord");
        fieldNewRecord.setAccessible(true);
        fieldNewRecord.set(printer, false);

        invokePrint("obj", "value", 0, 5);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(';');
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Delimiter_NoQuotingNoEscaping() throws Throwable {
        when(format.getDelimiter()).thenReturn('|');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        Field fieldNewRecord = CSVPrinter.class.getDeclaredField("newRecord");
        fieldNewRecord.setAccessible(true);
        fieldNewRecord.set(printer, false);

        invokePrint("obj", "value", 1, 3);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append('|');
        inOrder.verify(out).append("value", 1, 4);
    }

    @Test
    @Timeout(8000)
    void testPrint_ThrowsIOExceptionFromAppendDelimiter() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        Field fieldNewRecord = CSVPrinter.class.getDeclaredField("newRecord");
        fieldNewRecord.setAccessible(true);
        fieldNewRecord.set(printer, false);

        doThrow(new IOException("append delimiter failed")).when(out).append(',');

        IOException thrown = assertThrows(IOException.class, () -> invokePrint("obj", "value", 0, 5));
        assertEquals("append delimiter failed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrint_ThrowsIOExceptionFromAppendValue() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        Field fieldNewRecord = CSVPrinter.class.getDeclaredField("newRecord");
        fieldNewRecord.setAccessible(true);
        fieldNewRecord.set(printer, true);

        doThrow(new IOException("append value failed")).when(out).append(any(CharSequence.class), anyInt(), anyInt());

        IOException thrown = assertThrows(IOException.class, () -> invokePrint("obj", "value", 0, 5));
        assertEquals("append value failed", thrown.getMessage());
    }
}