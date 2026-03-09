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

class CSVPrinter_63_5Test {

    private CSVPrinter printer;
    private CSVFormat format;
    private Appendable out;

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
        String input = "abc,def";
        // The delimiter is ',' so it should escape ',' with '\,'
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append "abc"
        // append escape '\'
        // append ','
        // append "def"
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeCharacter() throws Throwable {
        String input = "abc\\def";
        // escape char is '\', so '\' should be escaped as '\\'
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "abc\ndef";
        // LF replaced with 'n' and escaped
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        inOrder.verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "abc\rdef";
        // CR replaced with 'r' and escaped
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3);
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        inOrder.verify(out).append(input, 4, 7);
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
    void testPrintAndEscape_PartialInput() throws Throwable {
        String input = "abc,def";
        // Only print "bc,d"
        invokePrintAndEscape(input, 1, 4);

        // substring is "bc,d"
        // 'b', 'c' normal, ',' escaped, 'd' normal
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 1, 3); // "bc"
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 4, 5); // "d"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_AllSpecialChars() throws Throwable {
        // String with CR, LF, delimiter ',' and escape '\'
        String input = "\r\n,\\";

        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(out);
        // '\r' -> '\r'
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('r');
        // '\n' -> '\n'
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('n');
        // ',' delimiter
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append(',');
        // '\' escape char
        inOrder.verify(out).append('\\');
        inOrder.verify(out).append('\\');
        verifyNoMoreInteractions(out);
    }

}