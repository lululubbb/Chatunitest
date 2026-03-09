package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndEscapeTest {

    private CSVFormat csvFormat;
    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.INFORMIX_UNLOAD; // Use a CSVFormat with a non-null escape char
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        // delimiter is '|' in INFORMIX_UNLOAD, so test with '|'
        String input = "value1|value2";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // The '|' delimiter should be escaped with escape char '\'
        // So output: value1\|value2
        assertEquals("value1\\|value2", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        // escape char is '\' in INFORMIX_UNLOAD
        String input = "value\\quote";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // The escape char '\' should be escaped by doubling it
        // So output: value\\quote
        assertEquals("value\\\\quote", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCRandLF() throws Throwable {
        String input = "line1\r\nline2\nline3\rline4";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // CR is replaced by 'r' and LF by 'n' after escape char '\'
        // So output should have \r\n\n\r sequences
        String expected = "line1\\r\\nline2\\nline3\\rline4";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialInput() throws Throwable {
        String input = "abc|def\\ghi\rjkl\nmno";
        StringBuilder out = new StringBuilder();

        // print substring "def\\ghi\rj"
        int offset = 4;
        int len = 9;

        invokePrintAndEscape(input, offset, len, out);

        // substring is: def\ghi\rj
        // '|' is delimiter, '\' is escape char, '\r' is CR
        // So '\' and '\r' are escaped

        // Expected output:
        // def\\ghi\rj
        String expected = "def\\\\ghi\\rj";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, 0, out);

        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_offsetLenEdgeCases() throws Throwable {
        String input = "abc|def";
        StringBuilder out = new StringBuilder();

        // offset = length, len = 0, no output expected
        invokePrintAndEscape(input, input.length(), 0, out);
        assertEquals("", out.toString());

        // offset + len = input.length()
        out.setLength(0);
        invokePrintAndEscape(input, 0, input.length(), out);
        assertEquals("abc\\|def", out.toString());
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out) throws Throwable {
        try {
            printAndEscapeMethod.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}