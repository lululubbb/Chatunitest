package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
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
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatPrintAndEscapeTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        // Use INFORMIX_UNLOAD which has delimiter PIPE and escape BACKSLASH for diverse characters
        csvFormat = CSVFormat.INFORMIX_UNLOAD;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_noSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length(), appendable);

        // Should append the whole string once
        verify(appendable, times(1)).append(input, 0, input.length());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withDelimiter() throws Throwable {
        // delimiter is PIPE '|'
        String input = "abc|def";
        // Expected: "abc" + escape + '|' + "def"
        invokePrintAndEscape(input, 0, input.length(), appendable);

        // Verify append calls
        // append("abc")
        verify(appendable).append(input, 0, 3);
        // append escape char
        verify(appendable).append(BACKSLASH);
        // append delimiter char '|'
        verify(appendable).append('|');
        // append "def"
        verify(appendable).append(input, 4, 7);
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withCRandLF() throws Throwable {
        String input = "line1\r\nline2\nline3\rend";
        // delimiter is PIPE '|', escape is BACKSLASH
        invokePrintAndEscape(input, 0, input.length(), appendable);

        // Segments and escapes:
        // "line1" + \ + 'r' + "" + \ + 'n' + "line2" + \ + 'n' + "line3" + \ + 'r' + "end"
        verify(appendable).append(input, 0, 5);   // "line1"
        verify(appendable).append(BACKSLASH);
        verify(appendable).append('r');
        verify(appendable).append(BACKSLASH);
        verify(appendable).append('n');
        verify(appendable).append(input, 7, 12);  // "line2"
        verify(appendable).append(BACKSLASH);
        verify(appendable).append('n');
        verify(appendable).append(input, 13, 18); // "line3"
        verify(appendable).append(BACKSLASH);
        verify(appendable).append('r');
        verify(appendable).append(input, 19, 22); // "end"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_withEscapeChar() throws Throwable {
        // Input contains escape char BACKSLASH itself
        String input = "test\\escape";
        invokePrintAndEscape(input, 0, input.length(), appendable);

        // Should split at backslash and escape it
        verify(appendable).append(input, 0, 4);   // "test"
        verify(appendable).append(BACKSLASH);
        verify(appendable).append(BACKSLASH);
        verify(appendable).append(input, 5, 11);  // "escape"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_partialOffsetLength() throws Throwable {
        String input = "12345|6789";
        // Only print from offset 2 length 5: "345|6"
        invokePrintAndEscape(input, 2, 5, appendable);

        // Segments: "345" + \ + '|' + "6"
        verify(appendable).append(input, 2, 5);   // "345"
        verify(appendable).append(BACKSLASH);
        verify(appendable).append('|');
        verify(appendable).append(input, 6, 7);   // "6"
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_emptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0, appendable);

        // Should not append anything
        verifyNoInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_offsetEqualsLength() throws Throwable {
        String input = "abc";
        // length 0 from offset 1
        invokePrintAndEscape(input, 1, 0, appendable);

        verifyNoInteractions(appendable);
    }

    // Helper to invoke private method printAndEscape via reflection
    private void invokePrintAndEscape(CharSequence value, int offset, int len, Appendable out) throws Throwable {
        Method method = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        method.setAccessible(true);
        try {
            method.invoke(csvFormat, value, offset, len, out);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}