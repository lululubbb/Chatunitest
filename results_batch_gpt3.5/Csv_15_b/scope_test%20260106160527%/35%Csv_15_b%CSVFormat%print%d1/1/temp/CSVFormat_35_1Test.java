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

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNull_QuoteModeNotAll() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Setup CSVFormat with nullString == null and quoteMode != ALL
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);

        StringBuilder out = new StringBuilder();

        // Use reflection to call private print method to verify invocation
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        CSVFormat spyFormat = spy(format);
        doAnswer(invocation -> {
            CharSequence value = invocation.getArgument(1);
            assertEquals("", value.toString());
            return null;
        }).when(spyFormat).print(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        spyFormat.print(null, out, true);
        verify(spyFormat).print(eq(null), eq(""), eq(0), eq(0), eq(out), eq(true));
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringSet_QuoteModeAll() throws IOException {
        // Setup CSVFormat with nullString set and quoteMode ALL
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.ALL,
                null,
                null,
                false,
                true,
                "\r\n",
                "NULL",
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);

        StringBuilder out = new StringBuilder();

        CSVFormat spyFormat = spy(format);
        doAnswer(invocation -> {
            CharSequence value = invocation.getArgument(1);
            assertEquals("\"NULL\"", value.toString());
            return null;
        }).when(spyFormat).print(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        spyFormat.print(null, out, false);
        verify(spyFormat).print(eq(null), eq("\"NULL\""), eq(0), eq(6), eq(out), eq(false));
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringSet_QuoteModeNotAll() throws IOException {
        // Setup CSVFormat with nullString set and quoteMode not ALL
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                "NULL",
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);

        StringBuilder out = new StringBuilder();

        CSVFormat spyFormat = spy(format);
        doAnswer(invocation -> {
            CharSequence value = invocation.getArgument(1);
            assertEquals("NULL", value.toString());
            return null;
        }).when(spyFormat).print(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        spyFormat.print(null, out, true);
        verify(spyFormat).print(eq(null), eq("NULL"), eq(0), eq(4), eq(out), eq(true));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_TrimTrue() throws IOException {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                null,
                false,
                false,
                false,
                true,
                false,
                false);

        StringBuilder out = new StringBuilder();

        CharSequence input = "  test  ";

        CSVFormat spyFormat = spy(format);
        doAnswer(invocation -> {
            CharSequence value = invocation.getArgument(1);
            assertEquals("test", value.toString());
            return null;
        }).when(spyFormat).print(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        spyFormat.print(input, out, false);
        verify(spyFormat).print(eq(input), eq("test"), eq(0), eq(4), eq(out), eq(false));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsNotCharSequence_TrimFalse() throws IOException {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                false,
                false);

        StringBuilder out = new StringBuilder();

        Object input = 12345;

        CSVFormat spyFormat = spy(format);
        doAnswer(invocation -> {
            CharSequence value = invocation.getArgument(1);
            assertEquals("12345", value.toString());
            return null;
        }).when(spyFormat).print(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        spyFormat.print(input, out, true);
        verify(spyFormat).print(eq(input), eq("12345"), eq(0), eq(5), eq(out), eq(true));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsNotCharSequence_TrimTrue() throws IOException {
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                null,
                false,
                false,
                false,
                true,
                false,
                false);

        StringBuilder out = new StringBuilder();

        Object input = "  spaced  ";

        CSVFormat spyFormat = spy(format);
        doAnswer(invocation -> {
            CharSequence value = invocation.getArgument(1);
            assertEquals("spaced", value.toString());
            return null;
        }).when(spyFormat).print(any(), any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class), anyBoolean());

        spyFormat.print(input, out, false);
        verify(spyFormat).print(eq(input), eq("spaced"), eq(0), eq(6), eq(out), eq(false));
    }
}