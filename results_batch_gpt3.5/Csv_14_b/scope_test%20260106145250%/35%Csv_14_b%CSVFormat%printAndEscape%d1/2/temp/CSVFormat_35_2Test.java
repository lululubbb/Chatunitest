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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVFormatPrintAndEscapeTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        // Use DEFAULT instance for testing
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length(), out);

        // It should append the whole string once, no escape chars
        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        // The delimiter in DEFAULT is COMMA ','
        String input = "abc,def";
        // Expected calls:
        // append "abc"
        // append escape char (default is '"')
        // append ','
        // append "def"

        invokePrintAndEscape(input, 0, input.length(), out);

        // Verify append calls in order
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 3); // "abc"
        inOrder.verify(out).append('"');
        inOrder.verify(out).append(',');
        inOrder.verify(out).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCR() throws Throwable {
        String input = "hello\rworld";
        invokePrintAndEscape(input, 0, input.length(), out);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 5); // "hello"
        inOrder.verify(out).append('"');
        inOrder.verify(out).append('r'); // CR replaced with 'r'
        inOrder.verify(out).append(input, 6, 11); // "world"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithLF() throws Throwable {
        String input = "line1\nline2";
        invokePrintAndEscape(input, 0, input.length(), out);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 5); // "line1"
        inOrder.verify(out).append('"');
        inOrder.verify(out).append('n'); // LF replaced with 'n'
        inOrder.verify(out).append(input, 6, 11); // "line2"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        // Escape char in DEFAULT is DOUBLE_QUOTE_CHAR = '"'
        String input = "quote\"test";
        invokePrintAndEscape(input, 0, input.length(), out);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 0, 5); // "quote"
        inOrder.verify(out).append('"');
        inOrder.verify(out).append('"'); // escape char itself appended
        inOrder.verify(out).append(input, 6, 10); // "test"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0, out);

        // No append calls expected for empty input
        verifyNoInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_PartialInput() throws Throwable {
        String input = "abc,def\r\n\"ghi";
        // Only print substring "def\r\n"
        int offset = 4;
        int len = 5; // substring "def\r\n"
        invokePrintAndEscape(input, offset, len, out);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(input, 4, 7); // "def"
        inOrder.verify(out).append('"');
        inOrder.verify(out).append('r');
        inOrder.verify(out).append('"');
        inOrder.verify(out).append('n');
        verifyNoMoreInteractions(out);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out) throws Throwable {
        Method method = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        method.setAccessible(true);
        try {
            method.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}