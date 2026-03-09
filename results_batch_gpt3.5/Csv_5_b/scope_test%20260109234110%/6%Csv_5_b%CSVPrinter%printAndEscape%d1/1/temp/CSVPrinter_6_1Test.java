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

class CSVPrinterPrintAndEscapeTest {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
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
        String input = "a,b";
        // Expect: append "a", then append escape '\', then append ',', then append "b"
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 1);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 2, 3);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        String input = "a\\b";
        // Expect: append "a", then append escape '\', then append '\', then append "b"
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 1);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(input, 2, 3);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "a\rb";
        // Expect: append "a", then append escape '\', then append 'r', then append "b"
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 1);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        inOrder.verify(out).append(input, 2, 3);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "a\nb";
        // Expect: append "a", then append escape '\', then append 'n', then append "b"
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 1);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append(input, 2, 3);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialOffsetAndLen() throws Throwable {
        String input = "xyz,a\nb";
        // Use offset=3, len=3 means substring "a\nb"
        invokePrintAndEscape(input, 3, 3);

        InOrder inOrder = inOrder(out);
        // substring is "a\nb" length 3
        // expect append "a", then escape + 'n', then append "b"
        inOrder.verify(out).append(input, 3, 4);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append(input, 5, 6);
        verifyNoMoreInteractions(out);
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
}