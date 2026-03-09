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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinterPrintTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
        // Reset newRecord to true before each test using reflection
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            field.set(printer, true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
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
    void testPrint_newRecordTrue_noDelimiter_quoting() throws Throwable {
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);
        when(format.getDelimiter()).thenReturn(',');

        String value = "quotedValue";
        int offset = 0;
        int len = value.length();

        // First print: newRecord == true, no delimiter appended
        invokePrint("obj", value, offset, len);
        verify(out, never()).append(anyChar());
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());

        // Second print: newRecord == false, delimiter appended
        invokePrint("obj2", value, offset, len);
        verify(out).append(',');

        // Third print: delimiter appended again
        invokePrint("obj3", value, offset, len);
        verify(out, times(2)).append(',');
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_noDelimiter_escaping() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);
        when(format.getDelimiter()).thenReturn(';');

        String value = "escapedValue";
        int offset = 0;
        int len = value.length();

        // First print: newRecord == true, no delimiter appended
        invokePrint(null, value, offset, len);
        verify(out, never()).append(anyChar());
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());

        // Second print: delimiter appended
        invokePrint(null, value, offset, len);
        verify(out).append(';');
    }

    @Test
    @Timeout(8000)
    void testPrint_newRecordTrue_noDelimiter_noQuotingNoEscaping() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);
        when(format.getDelimiter()).thenReturn('\t');

        String value = "plainValue";
        int offset = 1;
        int len = 4;

        // First print: newRecord == true, no delimiter appended
        invokePrint(null, value, offset, len);
        verify(out, never()).append(anyChar());
        verify(out).append(value, offset, offset + len);

        // Second print: delimiter appended
        invokePrint(null, value, offset, len);
        verify(out).append('\t');
    }

    @Test
    @Timeout(8000)
    void testPrint_offsetLenBounds() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        String value = "1234567890";
        int offset = 2;
        int len = 5;

        invokePrint(null, value, offset, len);
        verify(out).append(value, offset, offset + len);
    }

    @Test
    @Timeout(8000)
    void testPrint_IOExceptionFromAppend() throws Throwable {
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        String value = "fail";

        doThrow(new IOException("fail append")).when(out).append(any(CharSequence.class), anyInt(), anyInt());

        IOException thrown = assertThrows(IOException.class, () -> invokePrint(null, value, 0, value.length()));
        assertEquals("fail append", thrown.getMessage());
    }
}