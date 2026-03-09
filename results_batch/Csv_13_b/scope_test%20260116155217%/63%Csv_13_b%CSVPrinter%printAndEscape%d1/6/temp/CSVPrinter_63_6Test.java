package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVPrinter_63_6Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;
    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        printer = new CSVPrinter(out, format);

        printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(printer, input, offset, len);

        // Should append whole string once
        verify(out).append(input, offset, offset + len);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "ab,c";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(printer, input, offset, len);

        // Expected calls:
        // append "ab"
        verify(out).append(input, 0, 2);
        // append escape char
        verify(out).append('\\');
        // append delimiter char ','
        verify(out).append(',');
        // append "c"
        verify(out).append(input, 3, 4);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "ab\\c";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(printer, input, offset, len);

        // append "ab"
        verify(out).append(input, 0, 2);
        // append escape char
        verify(out).append('\\');
        // append escape char '\\'
        verify(out).append('\\');
        // append "c"
        verify(out).append(input, 3, 4);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "ab\nc";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(printer, input, offset, len);

        // append "ab"
        verify(out).append(input, 0, 2);
        // append escape char
        verify(out).append('\\');
        // append 'n' for LF
        verify(out).append('n');
        // append "c"
        verify(out).append(input, 3, 4);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "ab\rc";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(printer, input, offset, len);

        // append "ab"
        verify(out).append(input, 0, 2);
        // append escape char
        verify(out).append('\\');
        // append 'r' for CR
        verify(out).append('r');
        // append "c"
        verify(out).append(input, 3, 4);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        int offset = 0;
        int len = 0;

        printAndEscapeMethod.invoke(printer, input, offset, len);

        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(anyChar());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_Substring() throws Throwable {
        String input = "0123,567";
        int offset = 3;
        int len = 4; // substring "3,56"

        printAndEscapeMethod.invoke(printer, input, offset, len);

        // append "3"
        verify(out).append(input, 3, 4);
        // append escape char
        verify(out).append('\\');
        // append delimiter ','
        verify(out).append(',');
        // append "56"
        verify(out).append(input, 5, 7);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_ThrowsIOException() throws Throwable {
        String input = "abc";

        doThrow(new IOException("append failed")).when(out).append(input, 0, input.length());

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> printAndEscapeMethod.invoke(printer, input, 0, input.length()));

        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("append failed", thrown.getCause().getMessage());
    }
}