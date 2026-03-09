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
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_5_4Test {

    private CSVPrinter csvPrinter;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, format);
    }

    private void invokePrint(Object printerInstance, Object object, CharSequence value, int offset, int len) throws Exception {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        printMethod.invoke(printerInstance, object, value, offset, len);
    }

    private void invokePrintAndQuote(Object spyPrinter, Object object, CharSequence value, int offset, int len) throws Exception {
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
        printAndQuoteMethod.invoke(spyPrinter, object, value, offset, len);
    }

    private void invokePrintAndEscape(Object spyPrinter, CharSequence value, int offset, int len) throws Exception {
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
        printAndEscapeMethod.invoke(spyPrinter, value, offset, len);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_NoQuotingNoEscaping_AppendsValueDirectly() throws Exception {
        // Setup format mock
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        // newRecord is true initially, so no delimiter appended
        CharSequence value = "abcdef";
        int offset = 1;
        int len = 3; // substring "bcd"

        invokePrint(csvPrinter, "obj", value, offset, len);

        // Verify out.append called with substring value, offset to offset+len
        verify(out).append(value, offset, offset + len);

        // next call should append delimiter because newRecord is false now
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);
        // call again
        invokePrint(csvPrinter, "obj", value, offset, len);

        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).getDelimiter();
        inOrder.verify(out).append(';');
        inOrder.verify(out).append(value, offset, offset + len);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_AppendDelimiterBeforeValue() throws Exception {
        // Setup format mock
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);
        when(format.getDelimiter()).thenReturn(',');

        // Set newRecord to false by calling print once
        invokePrint(csvPrinter, "obj", "value", 0, 5);

        // Now newRecord is false, next call should prepend delimiter
        CharSequence value = "12345";
        int offset = 1;
        int len = 3;

        invokePrint(csvPrinter, "obj", value, offset, len);

        InOrder inOrder = inOrder(out, format);
        inOrder.verify(format).getDelimiter();
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(value, offset, offset + len);
    }

    @Test
    @Timeout(8000)
    void testPrint_Quoting_DelegatesToPrintAndQuote() throws Exception {
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        // Spy on CSVPrinter to verify private printAndQuote called
        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));

        // Reset newRecord to true via reflection to ensure consistent test state
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(spyPrinter, true);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CharSequence value = "quotedValue";
        int offset = 0;
        int len = value.length();

        // Use reflection to invoke print to trigger printAndQuote
        printMethod.invoke(spyPrinter, "obj", value, offset, len);

        // Verify that out.append(CharSequence, int, int) was never called because quoting is true
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());

        // Also verify that out.append(char) was never called because printAndQuote should handle quoting
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrint_Escaping_DelegatesToPrintAndEscape() throws Exception {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        CSVPrinter spyPrinter = spy(new CSVPrinter(out, format));

        // Reset newRecord to true via reflection to ensure consistent test state
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(spyPrinter, true);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CharSequence value = "escapedValue";
        int offset = 2;
        int len = 5;

        printMethod.invoke(spyPrinter, "obj", value, offset, len);

        // Verify that out.append(CharSequence, int, int) was never called because escaping is true
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());

        // Also verify that out.append(char) was never called because printAndEscape should handle escaping
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFlagIsSetToFalseAfterCall() throws Exception {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        // First call newRecord is true, no delimiter appended
        CharSequence value = "abc";
        invokePrint(csvPrinter, "obj", value, 0, 3);

        // Access private field newRecord via reflection
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        boolean newRecordValue = (boolean) newRecordField.get(csvPrinter);
        assertFalse(newRecordValue, "newRecord should be false after print call");
    }

    @Test
    @Timeout(8000)
    void testPrint_ThrowsIOExceptionFromAppend() throws Exception {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        doThrow(new IOException("append failed")).when(out).append(any(CharSequence.class), anyInt(), anyInt());

        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
            invokePrint(csvPrinter, "obj", "value", 0, 5);
        });

        // The cause should be IOException
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("append failed", ex.getCause().getMessage());
    }
}