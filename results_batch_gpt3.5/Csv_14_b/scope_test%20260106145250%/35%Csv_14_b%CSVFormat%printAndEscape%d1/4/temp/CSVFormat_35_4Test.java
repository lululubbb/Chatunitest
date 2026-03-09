package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
    void setUp() throws Exception {
        // Use DEFAULT instance for tests; can be customized if needed
        csvFormat = CSVFormat.DEFAULT;

        // Access private method printAndEscape via reflection
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
    }

    private void invokePrintAndEscape(CharSequence input, int offset, int len, Appendable out) throws Throwable {
        try {
            printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        StringBuilder out = new StringBuilder();

        // Invoke private method
        invokePrintAndEscape(input, 0, input.length(), out);

        // No escaping, output equals input
        assertEquals(input, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        char delim = csvFormat.getDelimiter();
        Character escapeChar = csvFormat.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character must not be null");
        char escape = escapeChar;

        String input = "start" + delim + "end";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // The delimiter should be escaped
        String expected = "start" + escape + delim + "end";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        Character escapeChar = csvFormat.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character must not be null");
        char escape = escapeChar;

        String input = "before" + escape + "after";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // The escape character itself should be escaped by prefixing another escape char
        String expected = "before" + escape + escape + "after";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        Character escapeChar = csvFormat.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character must not be null");
        char escape = escapeChar;

        String input = "line1\nline2";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // LF replaced by 'n' and escaped
        String expected = "line1" + escape + 'n' + "line2";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        Character escapeChar = csvFormat.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character must not be null");
        char escape = escapeChar;

        String input = "line1\rline2";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // CR replaced by 'r' and escaped
        String expected = "line1" + escape + 'r' + "line2";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, 0, out);

        // Output should be empty
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialInput() throws Throwable {
        Character escapeChar = csvFormat.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character must not be null");
        char escape = escapeChar;

        String input = "abc\ndef";
        StringBuilder out = new StringBuilder();

        // Only print substring "bc\nd"
        invokePrintAndEscape(input, 1, 4, out);

        // substring is "bc\nd"
        // '\n' is escaped to \n
        String expected = "bc" + escape + 'n' + "d";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_allSpecialCharsTogether() throws Throwable {
        char delim = csvFormat.getDelimiter();
        Character escapeChar = csvFormat.getEscapeCharacter();
        assertNotNull(escapeChar, "Escape character must not be null");
        char escape = escapeChar;

        // Use explicit char values for CR and LF since CSVFormat.CR and CSVFormat.LF do not exist
        char CR = '\r';
        char LF = '\n';
        String input = "" + CR + LF + delim + escape;
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // Each char replaced and escaped:
        // CR -> \r, LF -> \n, delim -> \delim, escape -> \escape
        String expected = "" + escape + 'r' + escape + 'n' + escape + delim + escape + escape;
        assertEquals(expected, out.toString());
    }
}