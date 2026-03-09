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
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVPrinter_63_6Test {

    private CSVPrinter printer;
    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getEscapeCharacter()).thenReturn('\\');
        printer = new CSVPrinter(outMock, formatMock);
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

        verify(outMock).append(input, 0, input.length());
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        String input = "hello,world";
        // delimiter is ','
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append "hello"
        // append escape character '\'
        // append ','
        // append "world"

        InOrder inOrder = inOrder(outMock);
        inOrder.verify(outMock).append(input, 0, 5);
        inOrder.verify(outMock).append('\\');
        inOrder.verify(outMock).append(',');
        inOrder.verify(outMock).append(input, 6, 11);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        String input = "hello\\world";
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(outMock);
        inOrder.verify(outMock).append(input, 0, 5);
        inOrder.verify(outMock).append('\\');
        inOrder.verify(outMock).append('\\');
        inOrder.verify(outMock).append(input, 6, 11);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "hello\rworld";
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(outMock);
        inOrder.verify(outMock).append(input, 0, 5);
        inOrder.verify(outMock).append('\\');
        inOrder.verify(outMock).append('r');
        inOrder.verify(outMock).append(input, 6, 11);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "hello\nworld";
        invokePrintAndEscape(input, 0, input.length());

        InOrder inOrder = inOrder(outMock);
        inOrder.verify(outMock).append(input, 0, 5);
        inOrder.verify(outMock).append('\\');
        inOrder.verify(outMock).append('n');
        inOrder.verify(outMock).append(input, 6, 11);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);

        verify(outMock, never()).append(anyChar());
        verify(outMock, never()).append(any(CharSequence.class));
        verify(outMock, never()).append(any(CharSequence.class), anyInt(), anyInt());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialInput() throws Throwable {
        String input = "abc,def";
        // only print "bc,d"
        invokePrintAndEscape(input, 1, 4);

        InOrder inOrder = inOrder(outMock);
        // substring "bc"
        inOrder.verify(outMock).append(input, 1, 3);
        // escape and delimiter ','
        inOrder.verify(outMock).append('\\');
        inOrder.verify(outMock).append(',');
        // substring "d"
        inOrder.verify(outMock).append(input, 4, 5);
        inOrder.verifyNoMoreInteractions();
    }
}