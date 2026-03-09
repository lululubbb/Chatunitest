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

class CSVPrinter_6_4Test {

    private StringBuilder out;
    private CSVFormat format;
    private CSVPrinter printer;
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

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals(input, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        String input = "hello,world";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("hello\\,world", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        String input = "escape\\char";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("escape\\\\char", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "line1\rline2";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("line1\\rline2", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "line1\nline2";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("line1\\nline2", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialOffsetAndLen() throws Throwable {
        String input = "abc,def\\ghi\r\njkl";
        // substring "def\\ghi\r\n"
        int offset = 4;
        int len = 8;
        invokePrintAndEscape(input, offset, len);
        // The output should be "def\\,ghi\\\\\\r\\n"
        assertEquals("def\\,ghi\\\\\\r\\n", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_singleSpecialCharAtStart() throws Throwable {
        String input = ",start";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("\\,start", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_singleSpecialCharAtEnd() throws Throwable {
        String input = "end\\";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("end\\\\", out.toString());
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        try {
            // Clear the StringBuilder before each invocation
            out.setLength(0);
            printAndEscapeMethod.invoke(printer, value, offset, len);
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }
}