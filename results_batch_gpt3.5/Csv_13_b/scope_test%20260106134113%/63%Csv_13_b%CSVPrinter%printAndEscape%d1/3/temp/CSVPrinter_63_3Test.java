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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

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

        invokePrintAndEscape(input, 0, input.length());

        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        String input = "ab,c";

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 2);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 3, 4);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        String input = "ab\\c";

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 2);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(input, 3, 4);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "ab\nc";

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 2);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append(input, 3, 4);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "ab\rc";

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 2);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        inOrder.verify(out).append(input, 3, 4);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_subsequence() throws Throwable {
        String input = "xyz,abc\n123";
        int offset = 4;
        int len = 5;

        invokePrintAndEscape(input, offset, len);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 4, 7);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append(input, 8, 9);
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