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
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVPrinter_63_2Test {

    private CSVPrinter printer;
    private CSVFormat format;
    private Appendable out;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_noSpecialChars() throws Exception {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withDelimiter() throws Exception {
        String input = "abc,def";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // Append "abc"
        // Append escape char '\'
        // Append ','
        // Append "def"
        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withEscapeChar() throws Exception {
        String input = "abc\\def";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // Append "abc"
        // Append escape char '\'
        // Append '\'
        // Append "def"
        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append('\\');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withLF() throws Exception {
        String input = "abc\ndef";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // Append "abc"
        // Append escape char '\'
        // Append 'n' (LF replaced)
        // Append "def"
        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withCR() throws Exception {
        String input = "abc\rdef";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // Append "abc"
        // Append escape char '\'
        // Append 'r' (CR replaced)
        // Append "def"
        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append('r');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_partialInput() throws Exception {
        String input = "abc,def,ghi";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        // Only print substring "def,gh"
        int offset = 4;
        int len = 6;

        invokePrintAndEscape(input, offset, len);

        // Expected calls:
        // Append "def"
        // Append escape char '\'
        // Append ','
        // Append "gh"
        verify(out).append(input, 4, 7);
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 8, 10);
        verifyNoMoreInteractions(out);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, value, offset, len);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // Unwrap IOException thrown by printAndEscape
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else {
                throw e;
            }
        }
    }
}