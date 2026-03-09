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

    private org.mockito.MockedStatic<MockitoAnnotations> mocks;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        openMocks = MockitoAnnotations.openMocks(this);

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscape()).thenReturn('\\');

        printer = new CSVPrinter(out, format);

        printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        // offset=0, len=input.length()
        invokePrintAndEscape(input, 0, input.length());

        // Should append the whole string once
        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "abc,def";
        invokePrintAndEscape(input, 0, input.length());

        // Expect append("abc"), append(escape), append(','), append("def")
        verify(out).append(input, 0, 3);
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "ab\\cd";
        invokePrintAndEscape(input, 0, input.length());

        // Expect append("ab"), append(escape), append(escape), append("cd")
        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append('\\');
        verify(out).append(input, 3, 5);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "ab\rcd";
        invokePrintAndEscape(input, 0, input.length());

        // Expect append("ab"), append(escape), append('r'), append("cd")
        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append('r');
        verify(out).append(input, 3, 5);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "ab\ncd";
        invokePrintAndEscape(input, 0, input.length());

        // Expect append("ab"), append(escape), append('n'), append("cd")
        verify(out).append(input, 0, 2);
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, 3, 5);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyString() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);

        // No append calls expected
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(anyChar());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_PartialString() throws Throwable {
        String input = "123,456\n789";
        // offset=4, len=4 => substring "456\n"
        invokePrintAndEscape(input, 4, 4);

        // "456\n" => append("456"), append(escape), append('n')
        verify(out).append(input, 4, 7);
        verify(out).append('\\');
        verify(out).append('n');
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