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

    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscape()).thenReturn('\\');

        printer = new CSVPrinter(out, format);

        printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        try {
            printAndEscapeMethod.invoke(printer, value, offset, len);
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
        String input = "abc,def";
        invokePrintAndEscape(input, 0, input.length());
        // Expect escape before delimiter ','
        assertEquals("abc\\,def", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "abc\\def";
        invokePrintAndEscape(input, 0, input.length());
        // Escape before escape char '\'
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
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "xxabc,defyy";
        // Only print substring "abc,def"
        invokePrintAndEscape(input, 2, 7);
        assertEquals("abc\\,def", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_AllSpecialChars() throws Throwable {
        String input = "\r\n,\\";
        invokePrintAndEscape(input, 0, input.length());
        // \r, \n, delimiter ',', escape '\' all escaped
        assertEquals("\\r\\n\\,\\\\", out.toString());
    }
}