package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
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

import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndEscapeTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withEscape(BACKSLASH);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String input = "simpletext";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(csvFormat, input, 0, input.length(), out);

        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        char delimiter = csvFormat.getDelimiter();
        String input = "abc" + delimiter + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(csvFormat, input, 0, input.length(), out);

        String expected = "abc" + csvFormat.getEscapeCharacter() + delimiter + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        char escape = csvFormat.getEscapeCharacter();
        String input = "abc" + escape + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(csvFormat, input, 0, input.length(), out);

        String expected = "abc" + escape + escape + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String input = "abc" + CR + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(csvFormat, input, 0, input.length(), out);

        String expected = "abc" + csvFormat.getEscapeCharacter() + 'r' + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String input = "abc" + LF + "def";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(csvFormat, input, 0, input.length(), out);

        String expected = "abc" + csvFormat.getEscapeCharacter() + 'n' + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialInput() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // input: "123" + CR + "456" + LF + "789" + delimiter + "0" + escape + "X"
        char delimiter = csvFormat.getDelimiter();
        char escape = csvFormat.getEscapeCharacter();
        String input = "123" + CR + "456" + LF + "789" + delimiter + "0" + escape + "X";
        StringBuilder out = new StringBuilder();

        // Only print and escape substring "456" + LF + "789" (3 + 1 + 3 = 7 chars)
        int offset = 4;
        int len = 7;
        invokePrintAndEscape(csvFormat, input, offset, len, out);

        String expected = "456" + csvFormat.getEscapeCharacter() + 'n' + "789";
        assertEquals(expected, out.toString());
    }

    private void invokePrintAndEscape(CSVFormat csvFormat, CharSequence value, int offset, int len, Appendable out)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        method.setAccessible(true);
        method.invoke(csvFormat, value, offset, len, out);
    }
}