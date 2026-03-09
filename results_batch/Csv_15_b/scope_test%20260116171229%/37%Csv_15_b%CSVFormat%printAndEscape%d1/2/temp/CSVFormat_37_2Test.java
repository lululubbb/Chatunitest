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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_37_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT.withEscape('\\').withDelimiter(',');
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        method.setAccessible(true);
        try {
            method.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            // rethrow the cause if it's IOException, else rethrow InvocationTargetException
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            } else {
                throw e;
            }
        }
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "abcdefg";
        StringBuilder sb = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), sb);

        assertEquals("abcdefg", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "abc,def";
        StringBuilder sb = new StringBuilder();

        // delimiter is ',' by default in csvFormat
        invokePrintAndEscape(input, 0, input.length(), sb);

        // The ',' should be escaped with escape char '\'
        // So output should be: "abc\,def"
        assertEquals("abc\\,def", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "abc\\def";
        StringBuilder sb = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), sb);

        // '\' should be escaped as '\\'
        assertEquals("abc\\\\def", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithCR() throws Throwable {
        String input = "abc\rdef";
        StringBuilder sb = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), sb);

        // CR is replaced with 'r' and escaped: abc\rdef -> abc\rdef with '\' before 'r'
        assertEquals("abc\\rdef", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithLF() throws Throwable {
        String input = "abc\ndef";
        StringBuilder sb = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), sb);

        // LF is replaced with 'n' and escaped: abc\ndef -> abc\ndef with '\' before 'n'
        assertEquals("abc\\ndef", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_EmptyValue() throws Throwable {
        String input = "";
        StringBuilder sb = new StringBuilder();

        invokePrintAndEscape(input, 0, input.length(), sb);

        assertEquals("", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "xyz,abc,def";
        StringBuilder sb = new StringBuilder();

        // offset 4, length 3 -> substring "abc"
        invokePrintAndEscape(input, 4, 3, sb);

        assertEquals("abc", sb.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_AppendableThrowsIOException() throws Throwable {
        Appendable throwingAppendable = mock(Appendable.class);
        doThrow(new IOException("append failed")).when(throwingAppendable).append(any(CharSequence.class), anyInt(),
                anyInt());

        IOException thrown = assertThrows(IOException.class, () -> {
            invokePrintAndEscape("abc", 0, 3, throwingAppendable);
        });
        assertEquals("append failed", thrown.getMessage());
    }
}