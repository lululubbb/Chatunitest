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

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withEscape('\\').withDelimiter(',');
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length(), out);

        // Verify append called once with whole string
        verify(out).append(input, 0, input.length());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "hello,world";
        // delimiter is ',' in DEFAULT

        // Expected calls:
        // append "hello"
        // append escape char
        // append ','
        // append "world"

        invokePrintAndEscape(input, 0, input.length(), out);

        verify(out).append(input, 0, 5); // "hello"
        verify(out).append('\\');       // escape
        verify(out).append(',');        // delimiter escaped
        verify(out).append(input, 6, 11); // "world"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithEscapeChar() throws Throwable {
        // Set escape char to '*'
        csvFormat = csvFormat.withEscape('*');
        out = mock(Appendable.class);
        String input = "abc*def";

        invokePrintAndEscape(input, 0, input.length(), out);

        verify(out).append(input, 0, 3); // "abc"
        verify(out).append('*');         // escape
        verify(out).append('*');         // escaped escape char
        verify(out).append(input, 4, 7); // "def"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_WithCRandLF() throws Throwable {
        String input = "line1\r\nline2\nline3\rend";
        // delimiter is ',' and escape is '\\'

        invokePrintAndEscape(input, 0, input.length(), out);

        // Expected sequence:
        // append "line1"
        // append '\\','r' (CR)
        // append '\\','n' (LF)
        // append "line2"
        // append '\\','n' (LF)
        // append "line3"
        // append '\\','r' (CR)
        // append "end"

        verify(out).append(input, 0, 5); // "line1"
        verify(out).append('\\');
        verify(out).append('r');
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, 7, 12); // "line2"
        verify(out).append('\\');
        verify(out).append('n');
        verify(out).append(input, 13, 18); // "line3"
        verify(out).append('\\');
        verify(out).append('r');
        verify(out).append(input, 19, 22); // "end"
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, 0, out);
        verify(out, never()).append(anyChar());
        verify(out, never()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, never()).append(any(CharSequence.class));
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_PartialInput() throws Throwable {
        String input = "abc,def";
        // Only escape "c,d"
        invokePrintAndEscape(input, 2, 3, out);

        // substring = "c,d"
        // c = 'c' no escape
        // ',' delimiter -> escape
        // 'd' no escape

        verify(out).append(input, 2, 3); // "c"
        verify(out).append('\\');
        verify(out).append(',');
        verify(out).append(input, 4, 5); // "d"
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