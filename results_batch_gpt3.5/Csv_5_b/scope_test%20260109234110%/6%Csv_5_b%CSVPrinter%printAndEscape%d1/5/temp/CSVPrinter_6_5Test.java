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
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        String input = "abc,def";
        invokePrintAndEscape(input, 0, input.length());
        // Expected: "abc" + escape + ',' + "def"
        assertEquals("abc\\,def", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        String input = "abc\\def";
        invokePrintAndEscape(input, 0, input.length());
        // Expected: "abc" + escape + '\' + "def"
        assertEquals("abc\\\\def", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "abc\rdef";
        invokePrintAndEscape(input, 0, input.length());
        // CR replaced with 'r' and escaped
        assertEquals("abc\\rdef", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "abc\ndef";
        invokePrintAndEscape(input, 0, input.length());
        // LF replaced with 'n' and escaped
        assertEquals("abc\\ndef", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialRange() throws Throwable {
        String input = "123,456,789";
        // Only escape substring ",456"
        invokePrintAndEscape(input, 3, 4);
        // substring is ",456"
        // Expected: escape + ',' + "456"
        assertEquals("\\,456", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyRange() throws Throwable {
        String input = "abc";
        invokePrintAndEscape(input, 1, 0);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_singleCharNoEscape() throws Throwable {
        String input = "x";
        invokePrintAndEscape(input, 0, 1);
        assertEquals("x", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_singleCharEscape() throws Throwable {
        String input = ",";
        invokePrintAndEscape(input, 0, 1);
        assertEquals("\\,", out.toString());
    }
}