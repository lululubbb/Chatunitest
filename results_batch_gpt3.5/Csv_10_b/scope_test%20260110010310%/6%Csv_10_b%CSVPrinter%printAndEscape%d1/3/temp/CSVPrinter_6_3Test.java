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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_6_3Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscape()).thenReturn('\\');
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        String input = "value1,value2";
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 6);
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 7, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        String input = "value\\test";
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 5);
        verify(out).append('\\');
        verify(out).append('\\');
        verify(out).append(input, 6, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "line1\rline2";
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 5);
        verify(out).append('\\');
        verify(out).append('r');
        verify(out).append(input, 6, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "line1\nline2";
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 5);
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, 6, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialRange() throws Throwable {
        String input = "abc,def\nghi\rjkl\\mno";
        int offset = 4;
        int len = 6;
        invokePrintAndEscape(input, offset, len);

        verify(out).append(input, offset, offset + 3);
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, offset + 4, offset + len);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyString() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);

        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_offsetLenZero() throws Throwable {
        String input = "abc";
        invokePrintAndEscape(input, 1, 0);

        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(anyChar());
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, value, offset, len);
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }
}