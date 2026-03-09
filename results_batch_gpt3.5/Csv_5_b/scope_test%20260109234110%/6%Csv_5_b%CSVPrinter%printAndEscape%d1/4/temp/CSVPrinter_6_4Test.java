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

class CSVPrinterPrintAndEscapeTest {

    private CSVPrinter printer;
    private StringBuilder out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
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
        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "text,with,comma";
        invokePrintAndEscape(input, 0, input.length());
        // commas should be escaped with '\'
        assertEquals("text\\,with\\,comma", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "text\\with\\escape";
        invokePrintAndEscape(input, 0, input.length());
        // escape char '\' should be escaped with '\'
        assertEquals("text\\\\with\\\\escape", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "line1\rline2";
        invokePrintAndEscape(input, 0, input.length());
        // CR replaced with \r
        assertEquals("line1\\rline2", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "line1\nline2";
        invokePrintAndEscape(input, 0, input.length());
        // LF replaced with \n
        assertEquals("line1\\nline2", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "abc,def\r\nghi\\jkl";
        // print substring "def\r\n"
        invokePrintAndEscape(input, 4, 4);
        // substring is "def\r\n"
        // ',' not present, but CR and LF present and should be escaped
        assertEquals("def\\r\\n", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_AllSpecialCharsTogether() throws Throwable {
        // string with delimiter ',', escape '\\', CR '\r', LF '\n'
        String input = ",\\\r\n";
        invokePrintAndEscape(input, 0, input.length());
        // each char escaped: \, \\ \r \n
        assertEquals("\\,\\\\\\r\\n", out.toString());
    }
}