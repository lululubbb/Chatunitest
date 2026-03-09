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

public class CSVFormat_37_1Test {

    private CSVFormat csvFormat;
    private Method printAndEscapeMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT.withEscape('"'); // ensure escape character is not null
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
        char delimiter = csvFormat.getDelimiter();
        String input = "before" + delimiter + "after";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // The delimiter should be escaped with escape character (which is double quote by default)
        char escape = csvFormat.getEscapeCharacter();
        String expected = "before" + escape + delimiter + "after";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithEscapeChar() throws Throwable {
        char escape = csvFormat.getEscapeCharacter();
        String input = "start" + escape + "end";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // The escape character is escaped by prefixing itself
        String expected = "start" + escape + escape + "end";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithCR() throws Throwable {
        char escape = csvFormat.getEscapeCharacter();
        String input = "line1\rline2";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // CR replaced by 'r' and escaped
        String expected = "line1" + escape + 'r' + "line2";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithLF() throws Throwable {
        char escape = csvFormat.getEscapeCharacter();
        String input = "line1\nline2";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), out);

        // LF replaced by 'n' and escaped
        String expected = "line1" + escape + 'n' + "line2";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        StringBuilder out = new StringBuilder();

        invokePrintAndEscape(input, 0, 0, out);

        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "abc,def";
        StringBuilder out = new StringBuilder();

        // Only process "c,de"
        int offset = 2;
        int len = 4;
        invokePrintAndEscape(input, offset, len, out);

        // ',' is delimiter and should be escaped
        char escape = csvFormat.getEscapeCharacter();
        String expected = "c" + escape + ',' + "de";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_ThrowsIOException() {
        CharSequence value = "abc";
        Appendable out = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("append(CharSequence) failed");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("append(char) failed");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("append(CharSequence, int, int) failed");
            }
        };

        assertThrows(IOException.class, () -> invokePrintAndEscape(value, 0, value.length(), out));
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out) throws Throwable {
        try {
            printAndEscapeMethod.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            // unwrap underlying exception
            throw e.getCause();
        }
    }
}