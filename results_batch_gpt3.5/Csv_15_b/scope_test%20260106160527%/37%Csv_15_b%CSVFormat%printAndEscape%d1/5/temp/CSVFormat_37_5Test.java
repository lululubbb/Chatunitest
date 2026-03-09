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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_37_5Test {

    private CSVFormat csvFormat;
    private Method printAndEscapeMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT;
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithDelimiter() throws Throwable {
        // delimiter is comma for DEFAULT CSVFormat
        String input = "abc,def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // Expected output: abc",def (comma escaped with escape char '"')
        String expected = "abc\",def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithEscapeChar() throws Throwable {
        // The escape character itself should be escaped
        // DEFAULT escape char is DOUBLE_QUOTE_CHAR (")
        String input = "abc\"def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // The double quote char in input should be escaped as " (escape char) followed by "
        // So expected: abc""def
        String expected = "abc\"\"def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithCRAndLF() throws Throwable {
        String input = "abc\r\ndef";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // CR replaced by 'r', LF replaced by 'n', both prefixed by escape char (")
        // So expected: abc"r"n"ndef
        String expected = "abc\"r\"n\"ndef";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "1234,5678";
        StringBuilder out = new StringBuilder();

        // Only print substring "34,"
        invokePrintAndEscape(input, 2, 3, out);

        // substring is "34,"
        // The comma should be escaped
        // So expected: 34",
        String expected = "34\",";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_NullAppendableThrows() {
        String input = "abc";

        assertThrows(NullPointerException.class, () -> {
            invokePrintAndEscape(input, 0, input.length(), null);
        });
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out) throws Throwable {
        try {
            printAndEscapeMethod.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}