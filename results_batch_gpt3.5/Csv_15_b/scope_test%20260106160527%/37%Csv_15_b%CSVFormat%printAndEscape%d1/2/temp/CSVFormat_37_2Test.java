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

public class CSVFormat_37_2Test {

    private CSVFormat csvFormat;
    private Method printAndEscapeMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT.withEscape('\\'); // ensure escape character is set
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        StringBuilder out = new StringBuilder();
        invokePrintAndEscape(input, 0, input.length(), out);
        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withDelimiter() throws Throwable {
        char delim = csvFormat.getDelimiter();
        char escape = csvFormat.getEscapeCharacter();
        String input = "abc" + delim + "def";
        StringBuilder out = new StringBuilder();
        invokePrintAndEscape(input, 0, input.length(), out);
        String expected = "abc" + escape + delim + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withEscapeChar() throws Throwable {
        char escape = csvFormat.getEscapeCharacter();
        String input = "abc" + escape + "def";
        StringBuilder out = new StringBuilder();
        invokePrintAndEscape(input, 0, input.length(), out);
        String expected = "abc" + escape + escape + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withCR() throws Throwable {
        char escape = csvFormat.getEscapeCharacter();
        String input = "abc\rdef";
        StringBuilder out = new StringBuilder();
        invokePrintAndEscape(input, 0, input.length(), out);
        String expected = "abc" + escape + 'r' + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_withLF() throws Throwable {
        char escape = csvFormat.getEscapeCharacter();
        String input = "abc\ndef";
        StringBuilder out = new StringBuilder();
        invokePrintAndEscape(input, 0, input.length(), out);
        String expected = "abc" + escape + 'n' + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_partialOffsetAndLength() throws Throwable {
        String input = "xyz,abc,def";
        StringBuilder out = new StringBuilder();
        // offset 4, len 7 -> "abc,def"
        invokePrintAndEscape(input, 4, 7, out);
        char escape = csvFormat.getEscapeCharacter();
        char delim = csvFormat.getDelimiter();
        String expected = "abc" + escape + delim + "def";
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        StringBuilder out = new StringBuilder();
        invokePrintAndEscape(input, 0, 0, out);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_offsetEqualsLength() throws Throwable {
        String input = "abc";
        StringBuilder out = new StringBuilder();
        invokePrintAndEscape(input, 1, 0, out);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_outAppendableThrowsIOException() {
        CharSequence input = "abc";
        Appendable out = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("append(CharSequence) failed");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("append(CharSequence, int, int) failed");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("append(char) failed");
            }
        };

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            printAndEscapeMethod.invoke(csvFormat, input, 0, input.length(), out);
        });
        Throwable cause = thrown.getCause();
        assertEquals(IOException.class, cause.getClass());
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out) throws Throwable {
        try {
            printAndEscapeMethod.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}