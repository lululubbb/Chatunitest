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

import static org.mockito.Mockito.*;

class CSVPrinter_63_3Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

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
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "abcde";
        // No special chars, should just append once
        invokePrintAndEscape(input, 0, input.length());

        verify(out, times(1)).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        // Delimiter is ','
        String input = "ab,c";
        // Should append "ab", then escape + ',', then "c"
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 3, 4);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        // Escape char is '\'
        String input = "ab\\c";
        // Should append "ab", then escape + '\', then "c"
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append('\\');
        verify(out).append(input, 3, 4);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "ab\r\nc";
        // CR replaced with 'r', LF replaced with 'n'
        invokePrintAndEscape(input, 0, input.length());

        // append "ab"
        verify(out).append(input, 0, 2);
        // escape + 'r'
        verify(out).append('\\');
        verify(out).append('r');
        // escape + 'n'
        verify(out).append('\\');
        verify(out).append('n');
        // append "c"
        verify(out).append(input, 4, 5);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialRange() throws Throwable {
        String input = "xyz,abc";
        // Only print substring "z,ab" (index 2 to 6)
        invokePrintAndEscape(input, 2, 4);

        // substring is "z,ab"
        // z no special, append "z"
        verify(out).append(input, 2, 3);
        // ',' escape
        verify(out).append('\\');
        verify(out).append(',');
        // append "ab"
        verify(out).append(input, 4, 6);
        verifyNoMoreInteractions(out);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        try {
            printAndEscapeMethod.invoke(printer, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}