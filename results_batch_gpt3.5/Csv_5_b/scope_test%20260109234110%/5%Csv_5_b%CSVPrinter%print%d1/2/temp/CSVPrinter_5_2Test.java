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

class CSVPrinter_5_2Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    private void invokePrint(Object printerInstance, Object object, CharSequence value, int offset, int len) throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(printerInstance, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_Quoting() throws Throwable {
        // newRecord true initially, so no delimiter appended
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);
        CharSequence value = "hello world";

        // Spy on printer
        CSVPrinter spyPrinter = spy(printer);
        setFieldNewRecord(spyPrinter, true);

        // Mock private method printAndQuote via reflection
        doAnswer(invocation -> null).when(spyPrinter).printAndQuote(any(), any(), anyInt(), anyInt());

        invokePrint(spyPrinter, "obj", value, 0, 5);

        // Verify no delimiter appended because newRecord was true
        verify(out, never()).append(any(CharSequence.class));

        // Verify printAndQuote called
        verify(spyPrinter).printAndQuote("obj", value, 0, 5);

        // newRecord should be false after call
        assertFalse(getFieldNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Quoting() throws Throwable {
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);
        CharSequence value = "hello world";

        CSVPrinter spyPrinter = spy(printer);
        setFieldNewRecord(spyPrinter, false);

        doAnswer(invocation -> null).when(spyPrinter).printAndQuote(any(), any(), anyInt(), anyInt());

        invokePrint(spyPrinter, "obj", value, 0, 5);

        // Verify delimiter appended once
        verify(out).append(format.getDelimiter());

        verify(spyPrinter).printAndQuote("obj", value, 0, 5);

        assertFalse(getFieldNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Escaping() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);
        CharSequence value = "abcdef";

        CSVPrinter spyPrinter = spy(printer);
        setFieldNewRecord(spyPrinter, false);

        doAnswer(invocation -> null).when(spyPrinter).printAndEscape(any(), anyInt(), anyInt());

        invokePrint(spyPrinter, "obj", value, 1, 3);

        verify(out).append(format.getDelimiter());

        verify(spyPrinter).printAndEscape(value, 1, 3);

        assertFalse(getFieldNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuotingNoEscaping() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);
        CharSequence value = "abcdef";

        setFieldNewRecord(printer, false);

        invokePrint(printer, "obj", value, 2, 3);

        // Verify delimiter appended
        verify(out).append(format.getDelimiter());

        // Verify append called with correct subsequence
        verify(out).append(value, 2, 5);

        assertFalse(getFieldNewRecord(printer));
    }

    // Helper methods to access private boolean newRecord field
    private void setFieldNewRecord(CSVPrinter printerInstance, boolean value) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        field.set(printerInstance, value);
    }

    private boolean getFieldNewRecord(CSVPrinter printerInstance) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField("newRecord");
        field.setAccessible(true);
        return (boolean) field.get(printerInstance);
    }

    // Use reflection to invoke private methods printAndQuote and printAndEscape on spy

    private void printAndQuote(Object printerInstance, Object object, CharSequence value, int offset, int len) throws Throwable {
        Method m = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        m.setAccessible(true);
        try {
            m.invoke(printerInstance, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private void printAndEscape(Object printerInstance, CharSequence value, int offset, int len) throws Throwable {
        Method m = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        m.setAccessible(true);
        try {
            m.invoke(printerInstance, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    // Mockito requires methods to be overridable for spying/mocking.
    // Since CSVPrinter is final, we cannot subclass or add proxy methods.
    // Instead, we use Mockito's spy to override the private methods via reflection.

    // To allow Mockito to mock private methods, we use doAnswer with reflection calls.

    // Override printAndQuote and printAndEscape methods on spyPrinter

    private CSVPrinter spy(CSVPrinter original) {
        CSVPrinter spy = mock(CSVPrinter.class, invocation -> {
            Method method = invocation.getMethod();
            if (method.getName().equals("printAndQuote")) {
                try {
                    printAndQuote(original, invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3));
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
                return null;
            } else if (method.getName().equals("printAndEscape")) {
                try {
                    printAndEscape(original, invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2));
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
                return null;
            } else {
                return invocation.callRealMethod();
            }
        });
        // Copy fields from original to spy to maintain state
        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            outField.set(spy, outField.get(original));

            Field formatField = CSVPrinter.class.getDeclaredField("format");
            formatField.setAccessible(true);
            formatField.set(spy, formatField.get(original));

            Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
            newRecordField.setAccessible(true);
            newRecordField.set(spy, newRecordField.get(original));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return spy;
    }

    // Replace calls to spy(printer) with spy(printer) method above

    // Update tests to use spy(printer) method

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_Quoting_Updated() throws Throwable {
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);
        CharSequence value = "hello world";

        CSVPrinter spyPrinter = spy(printer);
        setFieldNewRecord(spyPrinter, true);

        invokePrint(spyPrinter, "obj", value, 0, 5);

        verify(out, never()).append(any(CharSequence.class));
        verify(spyPrinter).printAndQuote("obj", value, 0, 5);
        assertFalse(getFieldNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Quoting_Updated() throws Throwable {
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);
        CharSequence value = "hello world";

        CSVPrinter spyPrinter = spy(printer);
        setFieldNewRecord(spyPrinter, false);

        invokePrint(spyPrinter, "obj", value, 0, 5);

        verify(out).append(format.getDelimiter());
        verify(spyPrinter).printAndQuote("obj", value, 0, 5);
        assertFalse(getFieldNewRecord(spyPrinter));
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Escaping_Updated() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);
        CharSequence value = "abcdef";

        CSVPrinter spyPrinter = spy(printer);
        setFieldNewRecord(spyPrinter, false);

        invokePrint(spyPrinter, "obj", value, 1, 3);

        verify(out).append(format.getDelimiter());
        verify(spyPrinter).printAndEscape(value, 1, 3);
        assertFalse(getFieldNewRecord(spyPrinter));
    }
}