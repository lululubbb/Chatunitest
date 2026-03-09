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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_5_5Test {

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

    private void verifyPrivateMethodCalled(Object spy, String methodName, Class<?>[] paramTypes, Object... params) throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        // We invoke the method once with the parameters to make sure it is callable
        method.invoke(spy, params);
        // Mockito cannot verify private method calls directly, so this is a workaround: 
        // we spy the method by overriding it if possible, or rely on indirect verification.
        // Since method is private, we cannot verify directly with Mockito.
        // So this method is just a placeholder if needed.
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_formatQuoting() throws Throwable {
        // Setup
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        // newRecord is true initially, so no delimiter appended

        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Invoke print
        printMethod.invoke(spyPrinter, "obj", "value123", 0, 5);

        // Verify delimiter not appended because newRecord true
        verify(out, never()).append(anyChar());

        // After print, newRecord should be false, verify by invoking again and expecting delimiter append
        when(format.getDelimiter()).thenReturn(',');
        printMethod.invoke(spyPrinter, "obj2", "value456", 0, 6);
        verify(out).append(',');

    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_formatQuoting() throws Throwable {
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);
        when(format.getDelimiter()).thenReturn(',');

        // Set newRecord to false via reflection
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        // Spy to verify printAndQuote call indirectly
        CSVPrinter spyPrinter = spy(printer);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyPrinter, "obj", "value123", 1, 4);

        // Verify delimiter appended once
        verify(out).append(',');

        // Since printAndQuote is private, verify effect instead of direct call:
        // For example, verify that out.append was called with the expected subsequence or characters
        // Here we just verify that out.append was called at least once after delimiter
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());

        // newRecord set to false after print, remains false
        assertFalse((Boolean) newRecordField.get(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_formatEscaping() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);
        when(format.getDelimiter()).thenReturn(';');

        // Set newRecord to false
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        CSVPrinter spyPrinter = spy(printer);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyPrinter, 123, "escapedValue", 2, 5);

        verify(out).append(';');

        // Verify effect of printAndEscape (private) by checking out.append calls with subsequence
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());

        assertFalse((Boolean) newRecordField.get(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordFalse_noQuotingNoEscaping() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);
        when(format.getDelimiter()).thenReturn('|');

        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Use a CharSequence with known content
        CharSequence value = "0123456789";

        printMethod.invoke(printer, null, value, 3, 4);

        // Verify delimiter appended
        verify(out).append('|');

        // Verify append called with correct subsequence
        verify(out).append(value, 3, 7);

        assertFalse((Boolean) newRecordField.get(printer));
    }

    @Test
    @Timeout(8000)
    void testPrint_appendThrowsIOException() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);
        when(format.getDelimiter()).thenReturn(',');

        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        doThrow(new IOException("append failed")).when(out).append(any(CharSequence.class), anyInt(), anyInt());

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        InvocationTargetException ite = assertThrows(InvocationTargetException.class, () -> printMethod.invoke(printer, null, "failtest", 0, 4));
        Throwable cause = ite.getCause();
        assertTrue(cause instanceof IOException);
        assertEquals("append failed", cause.getMessage());
    }
}