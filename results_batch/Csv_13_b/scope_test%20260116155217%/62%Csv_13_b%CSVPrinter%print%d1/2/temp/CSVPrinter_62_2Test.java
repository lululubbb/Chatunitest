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

import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_62_2Test {

    private CSVPrinter csvPrinter;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_QuoteCharacterSet() throws Throwable {
        // Setup
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoteCharacterSet()).thenReturn(true);
        when(format.isEscapeCharacterSet()).thenReturn(false);
        setField(csvPrinter, "newRecord", false);

        CharSequence value = "value";

        // Invoke private method print
        invokePrint(csvPrinter, "object", value, 0, value.length());

        // Verify append called with delimiter
        verify(out).append(',');
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_QuoteCharacterSet() throws Throwable {
        when(format.isQuoteCharacterSet()).thenReturn(true);
        when(format.isEscapeCharacterSet()).thenReturn(false);
        setField(csvPrinter, "newRecord", true);

        CharSequence value = "value";

        invokePrint(csvPrinter, "object", value, 0, value.length());

        // Since newRecord is true, append(delimiter) is not called
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrint_EscapeCharacterSet() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoteCharacterSet()).thenReturn(false);
        when(format.isEscapeCharacterSet()).thenReturn(true);
        setField(csvPrinter, "newRecord", false);

        CharSequence value = "value";

        invokePrint(csvPrinter, "object", value, 0, value.length());

        verify(out).append(',');
    }

    @Test
    @Timeout(8000)
    void testPrint_NoQuoteNoEscape() throws Throwable {
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoteCharacterSet()).thenReturn(false);
        when(format.isEscapeCharacterSet()).thenReturn(false);
        setField(csvPrinter, "newRecord", false);

        CharSequence value = "value";

        invokePrint(csvPrinter, "object", value, 1, 3);

        verify(out).append(value, 1, 4); // offset + len = 1 + 3 = 4
        verify(out).append(',');
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_NoDelimiter() throws Throwable {
        when(format.isQuoteCharacterSet()).thenReturn(false);
        when(format.isEscapeCharacterSet()).thenReturn(false);
        setField(csvPrinter, "newRecord", true);

        CharSequence value = "value";

        invokePrint(csvPrinter, "object", value, 0, value.length());

        verify(out, never()).append(anyChar());
        verify(out).append(value, 0, value.length());
    }

    // Helper method to invoke private print method
    private void invokePrint(CSVPrinter printer, Object object, CharSequence value, int offset, int len)
            throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class);
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
            Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}