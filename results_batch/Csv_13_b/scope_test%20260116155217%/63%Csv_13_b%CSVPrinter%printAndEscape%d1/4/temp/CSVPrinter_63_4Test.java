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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVPrinter_63_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;
    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws Exception {
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
        String input = "abcdefg";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        // Should append once with full string
        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "abc,def";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        // Expected calls:
        // append("abc")
        // append(escape char)
        // append(',')
        // append("def")
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        // input contains escape char '\\'
        String input = "ab\\cd";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 2);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(input, 3, 5);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "ab\ncd";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 2);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append(input, 3, 5);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "ab\rcd";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 2);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        inOrder.verify(out).append(input, 3, 5);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        printAndEscapeMethod.invoke(printer, input, 0, 0);

        verifyNoInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_SubSequence() throws Throwable {
        String input = "0123456789";
        // substring "3456" with offset 3, length 4
        printAndEscapeMethod.invoke(printer, input, 3, 4);

        verify(out).append(input, 3, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_MultipleSpecialChars() throws Throwable {
        // input with multiple special chars CR, LF, delimiter, escape
        String input = "a\r,b\n\\d";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        InOrder inOrder = inOrder(out);
        // a
        inOrder.verify(out).append(input, 0, 1);
        // CR -> \r
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        // ,
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        // b
        inOrder.verify(out).append(input, 3, 4);
        // LF -> \n
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        // \
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        // d
        inOrder.verify(out).append(input, 6, 7);

        verifyNoMoreInteractions(out);
    }
}