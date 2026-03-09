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

public class CSVPrinter_63_5Test {

    private CSVPrinter csvPrinter;
    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    public void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        when(formatMock.getDelimiter()).thenReturn(',');
        when(formatMock.getEscapeCharacter()).thenReturn('\\');
        csvPrinter = new CSVPrinter(outMock, formatMock);
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
    public void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "abcdefg";
        invokePrintAndEscape(input, 0, input.length());

        verify(outMock).append(input, 0, input.length());
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "abc,def";
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append "abc"
        // append escape char '\'
        // append ','
        // append "def"
        verify(outMock).append(input, 0, 3);
        verify(outMock).append('\\');
        verify(outMock).append(',');
        verify(outMock).append(input, 4, 7);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithCR() throws Throwable {
        String input = "abc\rd";
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append "abc"
        // append escape char '\'
        // append 'r' (CR replaced by 'r')
        // append "d"
        verify(outMock).append(input, 0, 3);
        verify(outMock).append('\\');
        verify(outMock).append('r');
        verify(outMock).append(input, 4, 5);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithLF() throws Throwable {
        String input = "ab\ncd";
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append "ab"
        // append escape char '\'
        // append 'n' (LF replaced by 'n')
        // append "cd"
        verify(outMock).append(input, 0, 2);
        verify(outMock).append('\\');
        verify(outMock).append('n');
        verify(outMock).append(input, 3, 5);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "ab\\cd";
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append "ab"
        // append escape char '\'
        // append escape char '\'
        // append "cd"
        verify(outMock).append(input, 0, 2);
        verify(outMock).append('\\');
        verify(outMock).append('\\');
        verify(outMock).append(input, 3, 5);
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);

        verify(outMock, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(outMock, never()).append(anyChar());
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_SubSequence() throws Throwable {
        String input = "123,456\n789";
        // offset 4, len 4 -> substring "456\n"
        invokePrintAndEscape(input, 4, 4);

        // Expected calls:
        // append "456"
        // append escape char '\'
        // append 'n'
        verify(outMock).append(input, 4, 7);
        verify(outMock).append('\\');
        verify(outMock).append('n');
        verifyNoMoreInteractions(outMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_AllSpecialChars() throws Throwable {
        String input = "\r\n,\\";
        invokePrintAndEscape(input, 0, input.length());

        // Expected calls:
        // append escape
        // append 'r'
        // append escape
        // append 'n'
        // append escape
        // append ','
        // append escape
        // append '\'
        verify(outMock, times(4)).append('\\');
        verify(outMock).append('r');
        verify(outMock).append('n');
        verify(outMock).append(',');
        verify(outMock).append('\\');
        verifyNoMoreInteractions(outMock);
    }
}