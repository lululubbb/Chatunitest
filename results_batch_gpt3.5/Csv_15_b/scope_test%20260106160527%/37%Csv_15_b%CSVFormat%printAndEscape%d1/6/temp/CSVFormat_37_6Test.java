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
import org.mockito.Mockito;

class CSVFormatPrintAndEscapeTest {

    private CSVFormat csvFormat;
    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Use DEFAULT instance for tests with escape character set to backslash
        csvFormat = CSVFormat.DEFAULT.withEscape('\\'); // ensure escape character is non-null

        // Access private method printAndEscape via reflection
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars_appendsWholeSegment() throws Throwable {
        String input = "simpletext";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter_escapesDelimiter() throws Throwable {
        char delimiter = csvFormat.getDelimiter(); // default comma ','
        String input = "abc" + delimiter + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        String expected = "abc" + csvFormat.getEscapeCharacter() + delimiter + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar_escapesEscapeChar() throws Throwable {
        char escape = csvFormat.getEscapeCharacter();
        String input = "abc" + escape + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        String expected = "abc" + escape + escape + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR_escapesCRAsR() throws Throwable {
        char cr = '\r';
        String input = "abc" + cr + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        String expected = "abc" + csvFormat.getEscapeCharacter() + 'r' + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF_escapesLFAsN() throws Throwable {
        char lf = '\n';
        String input = "abc" + lf + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        String expected = "abc" + csvFormat.getEscapeCharacter() + 'n' + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialOffsetLen() throws Throwable {
        String input = "12345,6789";
        StringBuilder out = new StringBuilder();

        // offset 2, length 5 -> substring "345,6"
        invokePrintAndEscape(input, 2, 5, out);

        // ',' at pos 5 (index 5 in input), which is at offset+3
        String expected = "345" + csvFormat.getEscapeCharacter() + ',' + "6";
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
    void testPrintAndEscape_offsetLenAtBounds() throws Throwable {
        String input = "abc,def\r\nghi\\jkl";
        StringBuilder out = new StringBuilder();

        // Use entire string
        invokePrintAndEscape(input, 0, input.length(), out);

        // Expected escape for ',', '\r', '\n', and '\\'
        char esc = csvFormat.getEscapeCharacter();
        String expected = "abc" + esc + "," + "def" + esc + "r" + esc + "n" + "ghi" + esc + "\\" + "jkl";

        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_appendableThrowsIOException() throws Throwable {
        Appendable throwingAppendable = Mockito.mock(Appendable.class);
        Mockito.when(throwingAppendable.append(Mockito.any(CharSequence.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new IOException("append failed"));

        IOException thrown = assertThrows(IOException.class, () -> {
            invokePrintAndEscape("abc", 0, 3, throwingAppendable);
        });
        assertEquals("append failed", thrown.getMessage());
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out)
            throws Throwable {
        try {
            printAndEscapeMethod.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            // Unwrap thrown exception from reflection
            throw e.getCause();
        }
    }
}