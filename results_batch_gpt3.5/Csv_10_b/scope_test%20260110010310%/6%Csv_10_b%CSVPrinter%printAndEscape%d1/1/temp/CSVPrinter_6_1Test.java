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

class CSVPrinter_6_1Test {

    private CSVPrinter printer;
    private StringBuilder out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
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
        String input = "abcdefg";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals(input, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "abc,def";
        invokePrintAndEscape(input, 0, input.length());
        // Expected: "abc\<escape>,\<escape>def" -> "abc\,def"
        assertEquals("abc\\,def", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "abc\\def";
        invokePrintAndEscape(input, 0, input.length());
        // Expected: "abc\\\<escape>d\<escape>ef" -> "abc\\\def"
        assertEquals("abc\\\\def", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "abc\rdef";
        invokePrintAndEscape(input, 0, input.length());
        // CR replaced by 'r' and escaped
        assertEquals("abc\\rdef", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "abc\ndef";
        invokePrintAndEscape(input, 0, input.length());
        // LF replaced by 'n' and escaped
        assertEquals("abc\\ndef", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "xxxabc,defyyy";
        // print substring "abc,def"
        invokePrintAndEscape(input, 3, 7);
        assertEquals("abc\\,def", out.toString());
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
    void testPrintAndEscape_SingleSpecialCharAtStart() throws Throwable {
        String input = ",abc";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("\\,abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_SingleSpecialCharAtEnd() throws Throwable {
        String input = "abc,";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("abc\\,", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_AllSpecialChars() throws Throwable {
        // delimiter = ',', escape = '\', CR = \r, LF = \n
        String input = ",\\\r\n,";
        invokePrintAndEscape(input, 0, input.length());
        // Expected: \, \\ \r \n \,
        assertEquals("\\,\\\\\\r\\n\\,", out.toString());
    }
}