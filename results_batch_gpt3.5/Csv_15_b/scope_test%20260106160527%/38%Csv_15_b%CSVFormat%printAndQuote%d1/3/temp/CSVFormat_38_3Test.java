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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
    }

    private String invokePrintAndQuote(CSVFormat format, Object object, CharSequence value, int offset, int len,
            boolean newRecord) throws Exception {
        StringBuilder out = new StringBuilder();
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(format, object, value, offset, len, out, newRecord);
        return out.toString();
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_AllQuoteMode_All() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        String value = "abc,def";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("\"abc,def\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_AllQuoteMode_AllNonNull_NonNullObject() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL_NON_NULL);
        String value = "xyz";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("\"xyz\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_AllQuoteMode_AllNonNull_NullObject() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL_NON_NULL);
        String value = "xyz";
        String result = invokePrintAndQuote(format, null, value, 0, value.length(), false);
        assertEquals("xyz", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NonNumeric_ObjectIsNumber() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NON_NUMERIC);
        Integer number = 123;
        String value = "123";
        String result = invokePrintAndQuote(format, number, value, 0, value.length(), false);
        assertEquals("123", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NonNumeric_ObjectIsNotNumber() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NON_NUMERIC);
        String value = "abc";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("\"abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_NoneQuoteMode() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NONE);
        String value = "a,b,c";
        StringBuilder out = new StringBuilder();
        Method printAndEscape = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class,
                int.class, Appendable.class);
        printAndEscape.setAccessible(true);

        CSVFormat spyFormat = spy(format);
        doAnswer(invocation -> {
            CharSequence val = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int length = invocation.getArgument(2);
            Appendable appendable = invocation.getArgument(3);
            appendable.append(val, off, off + length);
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        StringBuilder resultOut = new StringBuilder();
        method.invoke(spyFormat, value, value, 0, value.length(), resultOut, false);
        assertEquals("a,b,c", resultOut.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_EmptyLen_NewRecord() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "";
        String result = invokePrintAndQuote(format, value, value, 0, 0, true);
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_EmptyLen_NotNewRecord() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "";
        String result = invokePrintAndQuote(format, value, value, 0, 0, false);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_NewRecordWithSpecialChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "\u001Fabc"; // char < 0x20 at start
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), true);
        assertEquals("\"" + value + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_StartCharLessEqualComment() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "#abc"; // COMMENT char is '#'
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("\"#abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_ContainsLF() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "abc\ndef";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("\"abc\ndef\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_ContainsCR() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "abc\rdef";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("\"abc\rdef\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_ContainsQuoteChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withQuote('"');
        String value = "a\"b\"c";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        // quotes doubled inside output
        assertEquals("\"a\"\"b\"\"c\"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_EndsWithSpace() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "abc ";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("\"abc \"", result);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_MinimalQuoteMode_NoQuoteNeeded() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        String value = "abc";
        String result = invokePrintAndQuote(format, value, value, 0, value.length(), false);
        assertEquals("abc", result);
    }

}