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

class CSVFormatPrintAndEscapeTest {

    private CSVFormat csvFormat;
    private Appendable appendable;
    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT.withEscape('\\');
        appendable = mock(Appendable.class);
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "abcdefg";
        invokePrintAndEscape(input, 0, input.length(), appendable);

        // Expect append called once with whole string segment
        verify(appendable).append(input, 0, input.length());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        // delimiter is comma by default in DEFAULT
        String input = "abc,def";
        // Expected: "abc" then escape + ',' then "def"
        invokePrintAndEscape(input, 0, input.length(), appendable);

        // Verify append calls in order
        verify(appendable).append(input, 0, 3); // "abc"
        verify(appendable).append('\\'); // escape char
        verify(appendable).append(','); // delimiter char
        verify(appendable).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        // Set escape character to backslash
        csvFormat = CSVFormat.DEFAULT.withEscape('\\');
        String input = "abc\\def";
        invokePrintAndEscape(input, 0, input.length(), appendable);

        verify(appendable).append(input, 0, 3); // "abc"
        verify(appendable).append('\\'); // escape char
        verify(appendable).append('\\'); // escape char repeated because input char is escape
        verify(appendable).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCR() throws Throwable {
        String input = "a\rb";
        invokePrintAndEscape(input, 0, input.length(), appendable);

        verify(appendable).append(input, 0, 1); // "a"
        verify(appendable).append('\\');
        verify(appendable).append('r');
        verify(appendable).append(input, 2, 3); // "b"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withLF() throws Throwable {
        String input = "a\nb";
        invokePrintAndEscape(input, 0, input.length(), appendable);

        verify(appendable).append(input, 0, 1); // "a"
        verify(appendable).append('\\');
        verify(appendable).append('n');
        verify(appendable).append(input, 2, 3); // "b"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withOffsetAndLen() throws Throwable {
        String input = "xyz,abc\rdef\n";
        // substring "abc\rdef" from index 4 length 7
        invokePrintAndEscape(input, 4, 7, appendable);

        // substring: "abc\rdef"
        // expect: "abc" + escape + 'r' + "def"
        verify(appendable).append(input, 4, 7); // "abc"
        verify(appendable).append('\\');
        verify(appendable).append('r');
        verify(appendable).append(input, 8, 11); // "def"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0, appendable);

        verify(appendable, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(appendable, never()).append(anyChar());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_lenZero() throws Throwable {
        String input = "abc";
        invokePrintAndEscape(input, 1, 0, appendable);

        verify(appendable, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(appendable, never()).append(anyChar());
        verifyNoMoreInteractions(appendable);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out) throws Throwable {
        try {
            printAndEscapeMethod.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            // unwrap
            throw e.getCause();
        }
    }
}