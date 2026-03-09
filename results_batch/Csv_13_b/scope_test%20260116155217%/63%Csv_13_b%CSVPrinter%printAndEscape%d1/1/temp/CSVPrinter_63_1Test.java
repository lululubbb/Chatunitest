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
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVPrinter_63_1Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');
        printer = new CSVPrinter(out, format);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());
        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "a,b,c";
        // delimiter is ','
        invokePrintAndEscape(input, 0, input.length());

        // The expected calls are:
        // append("a",0,1)
        // append('\\')
        // append(',')
        // append("b,c",2,5)

        // We expect two append(CharSequence, int, int) calls and two append(char) calls
        ArgumentCaptor<CharSequence> csCaptor = ArgumentCaptor.forClass(CharSequence.class);
        ArgumentCaptor<Integer> startCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> endCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(out, times(2)).append(csCaptor.capture(), startCaptor.capture(), endCaptor.capture());
        verify(out).append('\\');
        verify(out).append(',');

        // The first append segment is "a" (0 to 1)
        assertEquals("a", csCaptor.getAllValues().get(0).subSequence(startCaptor.getAllValues().get(0), endCaptor.getAllValues().get(0)).toString());
        // The second append segment is "b,c" (2 to 5)
        assertEquals("b,c", csCaptor.getAllValues().get(1).subSequence(startCaptor.getAllValues().get(1), endCaptor.getAllValues().get(1)).toString());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        // Escape char is '\'
        String input = "ab\\cd";
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append("ab",0,2)
        // append('\\')
        // append('\\')
        // append("cd",3,5)

        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append('\\');
        verify(out).append(input, 3, 5);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "ab\rcd";
        invokePrintAndEscape(input, 0, input.length());

        // Expected:
        // append("ab",0,2)
        // append('\\')
        // append('r')
        // append("cd",3,5)

        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append('r');
        verify(out).append(input, 3, 5);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "ab\ncd";
        invokePrintAndEscape(input, 0, input.length());

        // Expected:
        // append("ab",0,2)
        // append('\\')
        // append('n')
        // append("cd",3,5)

        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, 3, 5);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(anyChar());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_SubSequence() throws Throwable {
        String input = "123,456";
        // Only print "23,4"
        invokePrintAndEscape(input, 1, 4);

        // Expected:
        // append("23",1,3)
        // append('\\')
        // append(',')
        // append("4",4,5)

        verify(out).append(input, 1, 3);
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 4, 5);

        verifyNoMoreInteractions(out);
    }
}