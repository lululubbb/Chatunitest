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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVPrinter_6_2Test {

    private CSVPrinter printer;
    private StringBuilder out;
    private CSVFormat format;

    @BeforeEach
    public void setUp() throws IOException {
        out = new StringBuilder();
        format = CSVFormat.DEFAULT.withDelimiter(',').withEscape('\\');
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "abc123";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("abc123", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withDelimiter() throws Throwable {
        String input = "ab,c";
        invokePrintAndEscape(input, 0, input.length());
        // The comma should be escaped: ab\,c
        assertEquals("ab\\,c", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withEscapeChar() throws Throwable {
        String input = "ab\\c";
        invokePrintAndEscape(input, 0, input.length());
        // The backslash should be escaped: ab\\c
        assertEquals("ab\\\\c", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withCRandLF() throws Throwable {
        String input = "a\r\nb";
        invokePrintAndEscape(input, 0, input.length());
        // CR (\r) replaced with 'r' and escaped, LF (\n) replaced with 'n' and escaped: a\r\nb -> a\r\nb
        assertEquals("a\\rb\\n", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_partialOffsetAndLen() throws Throwable {
        String input = "123,45\\67\r\n89";
        // substring "45\\67\r\n"
        int offset = 4;
        int len = 6;
        invokePrintAndEscape(input, offset, len);
        // substring is "45\67\r\n"
        // expected: 45\\67\r\n escaped: '4','5','\\','6','7','\r','\n'
        // \r and \n replaced and escaped, \ escaped
        // output: 45\\67\r\n
        assertEquals("45\\\\67\\r\\n", out.toString());
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
}