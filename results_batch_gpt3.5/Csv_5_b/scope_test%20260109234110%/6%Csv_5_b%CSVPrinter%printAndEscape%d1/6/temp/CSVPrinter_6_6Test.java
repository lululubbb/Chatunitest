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
        String input = "abc,def";
        // Expected: append "abc", then append escape and ',', then append "def"
        invokePrintAndEscape(input, 0, input.length());
        // Verify append calls in order
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3); // "abc"
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "abc\\def";
        invokePrintAndEscape(input, 0, input.length());
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3); // "abc"
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "abc\rdef";
        invokePrintAndEscape(input, 0, input.length());
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3); // "abc"
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        inOrder.verify(out).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "abc\ndef";
        invokePrintAndEscape(input, 0, input.length());
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3); // "abc"
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);
        verifyNoInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "0123,56789";
        // substring from index 2 length 5 = "23,56"
        invokePrintAndEscape(input, 2, 5);
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 2, 4); // "23"
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 5, 7); // "56"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_ThrowsIOException() throws Throwable {
        String input = "ab,cd";
        doThrow(new IOException("append failed")).when(out).append(any(CharSequence.class), anyInt(), anyInt());
        IOException thrown = assertThrows(IOException.class, () -> invokePrintAndEscape(input, 0, input.length()));
        assertEquals("append failed", thrown.getMessage());
    }
}