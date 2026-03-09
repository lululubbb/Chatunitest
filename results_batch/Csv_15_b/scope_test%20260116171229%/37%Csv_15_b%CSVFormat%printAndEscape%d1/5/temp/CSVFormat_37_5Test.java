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
    private Appendable out;

    private Method printAndEscapeMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        // Use DEFAULT instance for tests
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);

        // Access private method printAndEscape via reflection
        printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        int offset = 0;
        int len = input.length();

        // Expect append called once with entire input
        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        // Verify append(CharSequence, int, int) called once with entire string
        verify(out).append(input, offset, offset + len);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        // Delimiter is ',' for DEFAULT
        char delim = csvFormat.getDelimiter();
        String input = "abc" + delim + "def";
        int offset = 0;
        int len = input.length();

        // Expect append called multiple times: before delimiter, then escape + delimiter, then rest
        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        // Verify calls in order
        verify(out).append(input, 0, 3);
        verify(out).append(csvFormat.getEscapeCharacter());
        verify(out).append(delim);
        verify(out).append(input, 4, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        // Escape character is '"' for DEFAULT
        char escape = csvFormat.getEscapeCharacter();
        String input = "ab" + escape + "cd";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        // Verify calls in order
        verify(out).append(input, 0, 2);
        verify(out).append(escape);
        verify(out).append(escape);
        verify(out).append(input, 3, 4);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCRandLF() throws Throwable {
        // Use string with CR and LF
        String input = "a\r\nb";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        // Verify expected sequence:
        // append "a"
        // append escape + 'r' for CR
        // append escape + 'n' for LF
        // append "b"
        verify(out).append(input, 0, 1); // "a"
        verify(out).append(csvFormat.getEscapeCharacter());
        verify(out).append('r');
        verify(out).append(csvFormat.getEscapeCharacter());
        verify(out).append('n');
        verify(out).append(input, 3, 4); // "b"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "0123456789";
        int offset = 2;
        int len = 5; // substring "23456"
        // Insert delimiter at position 4 (relative to input start), which is index 4
        // So substring is "23456", delimiter at index 4 is '4' which is not delimiter, so no escapes expected.
        // Let's change char at index 4 to delimiter
        char delim = csvFormat.getDelimiter();
        StringBuilder sb = new StringBuilder(input);
        sb.setCharAt(4, delim);
        String testInput = sb.toString();

        printAndEscapeMethod.invoke(csvFormat, testInput, offset, len, out);

        // The substring is "23456" with '4' replaced by delimiter at index 4
        // So substring is chars at indices 2 to 6: '2','3','4','5','6' but '4' replaced with delimiter
        // So expect append "23"
        // then append escape + delimiter
        // then append "56"
        verify(out).append(testInput, 2, 4);
        verify(out).append(csvFormat.getEscapeCharacter());
        verify(out).append(delim);
        verify(out).append(testInput, 5, 7);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptySubstring() throws Throwable {
        String input = "abc";
        int offset = 1;
        int len = 0;

        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        // No append calls expected because length is zero
        verifyNoInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_SingleCharIsDelimiter() throws Throwable {
        char delim = csvFormat.getDelimiter();
        String input = String.valueOf(delim);
        int offset = 0;
        int len = 1;

        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        // Should append escape then delimiter
        verify(out).append(csvFormat.getEscapeCharacter());
        verify(out).append(delim);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_SingleCharIsCR() throws Throwable {
        String input = "\r";
        int offset = 0;
        int len = 1;

        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        verify(out).append(csvFormat.getEscapeCharacter());
        verify(out).append('r');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_SingleCharIsLF() throws Throwable {
        String input = "\n";
        int offset = 0;
        int len = 1;

        printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);

        verify(out).append(csvFormat.getEscapeCharacter());
        verify(out).append('n');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_ThrowsIOException() throws Throwable {
        // Setup Appendable to throw IOException on append(CharSequence, int, int)
        doThrow(new IOException("Test IOException")).when(out).append(any(CharSequence.class), anyInt(), anyInt());

        String input = "test";
        int offset = 0;
        int len = input.length();

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            printAndEscapeMethod.invoke(csvFormat, input, offset, len, out);
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("Test IOException", thrown.getCause().getMessage());
    }
}