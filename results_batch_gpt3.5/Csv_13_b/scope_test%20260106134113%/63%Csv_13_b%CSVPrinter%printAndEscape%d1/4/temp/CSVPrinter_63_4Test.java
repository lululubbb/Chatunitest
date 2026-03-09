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
import org.mockito.InOrder;

class CSVPrinter_63_4Test {

    private CSVPrinter csvPrinter;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');
        csvPrinter = new CSVPrinter(out, format);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(csvPrinter, value, offset, len);
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
        String input = "abc,def";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        // "abc"
        inOrder.verify(out).append(input, 0, 3);
        // escape + delimiter
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        // "def"
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "abc\\def";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        // "abc"
        inOrder.verify(out).append(input, 0, 3);
        // escape + escape char
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        // "def"
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "abc\rdef";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        // "abc"
        inOrder.verify(out).append(input, 0, 3);
        // escape + 'r'
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        // "def"
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "abc\ndef";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        // "abc"
        inOrder.verify(out).append(input, 0, 3);
        // escape + 'n'
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        // "def"
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLen() throws Throwable {
        String input = "123,456,789";
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        // substring "456,7"
        int offset = 4;
        int len = 5;

        invokePrintAndEscape(input, offset, len);

        InOrder inOrder = inOrder(out);
        // "456"
        inOrder.verify(out).append(input, 4, 7);
        // escape + delimiter (',')
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        // "7"
        inOrder.verify(out).append(input, 8, 9);
        verifyNoMoreInteractions(out);
    }
}