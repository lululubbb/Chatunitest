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
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());
        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        String input = "abc,def";
        // ',' at position 3
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append "abc"
        // append escape char
        // append ','
        // append "def"
        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        when(format.getEscapeCharacter()).thenReturn('\\');
        String input = "abc\\def";
        // '\' at position 3
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append('\\');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "abc\ndef";
        // '\n' at position 3
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "abc\rdef";
        // '\r' at position 3
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append('r');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_offsetAndLen() throws Throwable {
        String input = "0123456789";
        // substring "3456" (offset 3, len 4)
        invokePrintAndEscape(input, 3, 4);
        verify(out).append(input, 3, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(anyChar());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_allSpecialChars() throws Throwable {
        // Compose string with all special chars: CR, LF, delimiter, escape
        when(format.getDelimiter()).thenReturn(';');
        when(format.getEscapeCharacter()).thenReturn('~');
        String input = "\r\n;~";
        invokePrintAndEscape(input, 0, input.length());

        // Expected sequence:
        // '\r' -> append escape '~', 'r'
        // '\n' -> append escape '~', 'n'
        // ';' -> append escape '~', ';'
        // '~' -> append escape '~', '~'
        // No normal chars, so no append with range calls expected

        verify(out, times(4)).append('~');
        verify(out).append('r');
        verify(out).append('n');
        verify(out).append(';');
        verify(out).append('~');
        verifyNoMoreInteractions(out);
    }
}