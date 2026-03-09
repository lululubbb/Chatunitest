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
    private StringBuilderAppendable out;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT.withEscape('\\'); // ensure escape character is set to backslash
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
        out = new StringBuilderAppendable();
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        // delimiter is comma by default in DEFAULT
        String input = "hello,world";
        invokePrintAndEscape(input, 0, input.length());
        // The comma should be escaped with escape character '\' followed by ','
        assertEquals("hello\\,world", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "escape\\char";
        invokePrintAndEscape(input, 0, input.length());
        // The backslash should be escaped as \\ (escape + escape)
        assertEquals("escape\\\\char", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "line\rbreak";
        invokePrintAndEscape(input, 0, input.length());
        // CR (\r) should be escaped as \r
        assertEquals("line\\rbreak", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "line\nbreak";
        invokePrintAndEscape(input, 0, input.length());
        // LF (\n) should be escaped as \n
        assertEquals("line\\nbreak", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyString() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_Substring() throws Throwable {
        String input = "abc,def\\ghi\r\njkl";
        // substring "def\\ghi\r\n"
        int offset = 4;
        int len = 8;
        invokePrintAndEscape(input, offset, len);
        // substring is "def\ghi\r\n"
        // ',' at pos 3 is not included, so no comma
        // '\' should be escaped, CR and LF escaped
        // expected: def\\ghi\r\n (with escapes)
        assertEquals("def\\\\ghi\\r\\n", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLenExceeding() {
        String input = "abc";
        // offset + len exceed input length should throw IndexOutOfBoundsException from CharSequence.charAt
        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            invokePrintAndEscape(input, 0, 10);
        });
        assertTrue(exception.getCause() instanceof IndexOutOfBoundsException);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        out = new StringBuilderAppendable(); // reset output before each invocation
        try {
            printAndEscapeMethod.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (IllegalAccessException e) {
            throw e;
        }
    }

    /**
     * StringBuilder does not implement Appendable#append(CharSequence, int, int) before Java 9,
     * so we wrap StringBuilder with this helper to ensure compatibility.
     */
    private static class StringBuilderAppendable implements Appendable {
        private final StringBuilder sb = new StringBuilder();

        @Override
        public Appendable append(CharSequence csq) {
            if (csq == null) {
                sb.append("null");
            } else {
                sb.append(csq);
            }
            return this;
        }

        @Override
        public Appendable append(CharSequence csq, int start, int end) {
            if (csq == null) {
                sb.append("null", start, end);
            } else {
                sb.append(csq, start, end);
            }
            return this;
        }

        @Override
        public Appendable append(char c) {
            sb.append(c);
            return this;
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}