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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinterPrintAndEscapeTest {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private Method printAndEscapeMethod;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        mocks = MockitoAnnotations.openMocks(this);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscape()).thenReturn('\\');

        printer = new CSVPrinter(out, format);

        printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws IOException, InvocationTargetException, IllegalAccessException {
        String input = "simpletext";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        // Expect out.append(value, start, pos) once with whole string
        verify(out, times(1)).append(input, 0, input.length());
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws IOException, InvocationTargetException, IllegalAccessException {
        String input = "a,b";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        // Should append "a"
        verify(out).append(input, 0, 1);
        // Then append escape char
        verify(out).append('\\');
        // Then append delimiter ','
        verify(out).append(',');
        // Then append "b"
        verify(out).append(input, 2, 3);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws IOException, InvocationTargetException, IllegalAccessException {
        String input = "a\\b";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        // Should append "a"
        verify(out).append(input, 0, 1);
        // Then append escape char
        verify(out).append('\\');
        // Then append escape char again
        verify(out).append('\\');
        // Then append "b"
        verify(out).append(input, 2, 3);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws IOException, InvocationTargetException, IllegalAccessException {
        String input = "a\nb";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        // Should append "a"
        verify(out).append(input, 0, 1);
        // Then append escape char
        verify(out).append('\\');
        // Then append 'n' for LF
        verify(out).append('n');
        // Then append "b"
        verify(out).append(input, 2, 3);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws IOException, InvocationTargetException, IllegalAccessException {
        String input = "a\rb";
        printAndEscapeMethod.invoke(printer, input, 0, input.length());

        // Should append "a"
        verify(out).append(input, 0, 1);
        // Then append escape char
        verify(out).append('\\');
        // Then append 'r' for CR
        verify(out).append('r');
        // Then append "b"
        verify(out).append(input, 2, 3);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws IOException, InvocationTargetException, IllegalAccessException {
        String input = "";
        printAndEscapeMethod.invoke(printer, input, 0, 0);

        // No append calls expected
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLength() throws IOException, InvocationTargetException, IllegalAccessException {
        String input = "123,456\n789\\0";
        // substring "456\n7" starting at index 4 length 5
        printAndEscapeMethod.invoke(printer, input, 4, 5);

        // Check calls:
        // substring is "456\n7"
        // chars: '4','5','6','\n','7'
        // special char at pos=7 (offset 4 + 3) is LF

        // append "456"
        verify(out).append(input, 4, 7);
        // append escape char
        verify(out).append('\\');
        // append 'n'
        verify(out).append('n');
        // append "7"
        verify(out).append(input, 8, 9);
    }
}