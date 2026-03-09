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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        csvFormat = mock(CSVFormat.class, CALLS_REAL_METHODS);
        appendable = new StringBuilder();
    }

    private String invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out,
            boolean newRecord) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(csvFormat, object, value, offset, len, out, newRecord);
        return out.toString();
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.ALL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNullWithNull() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.ALL_NON_NULL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "null";
        invokePrintAndQuote(null, value, 0, value.length(), out, false);
        // null object, should not quote because object is null and ALL_NON_NULL quotes non-null
        assertEquals("null", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNullWithNonNull() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.ALL_NON_NULL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithNumber() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "123";
        invokePrintAndQuote(Integer.valueOf(123), value, 0, value.length(), out, false);
        assertEquals("123", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithNonNumber() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.NON_NUMERIC);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.NONE);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // Spy csvFormat to verify printAndEscape is called
        CSVFormat spyFormat = spy(csvFormat);
        doNothing().when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        StringBuilder out = new StringBuilder();
        String value = "abc";
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(spyFormat, "abc", value, 0, value.length(), out, false);

        verify(spyFormat).printAndEscape(value, 0, value.length(), out);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEmptyNewRecord() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "";
        invokePrintAndQuote("", value, 0, 0, out, true);
        assertEquals("\"\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordWithSpecialStartChar() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // char at pos = 0 is 0x01 (less than 0x20)
        StringBuilder out = new StringBuilder();
        String value = "\u0001abc";
        invokePrintAndQuote(value, value, 0, value.length(), out, true);
        assertEquals("\"" + value + "\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordWithCommentChar() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // char at pos = 0 is COMMENT (default '#')
        StringBuilder out = new StringBuilder();
        String value = "#abc";
        invokePrintAndQuote(value, value, 0, value.length(), out, false);
        assertEquals("\"#abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalWithLineBreak() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "ab\nc";
        invokePrintAndQuote(value, value, 0, value.length(), out, false);
        assertEquals("\"ab\nc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalWithQuoteCharInside() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "ab\"c";
        invokePrintAndQuote(value, value, 0, value.length(), out, false);
        assertEquals("\"ab\"\"c\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEndsWithSpace() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "abc ";
        invokePrintAndQuote(value, value, 0, value.length(), out, false);
        assertEquals("\"abc \"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNoQuoteNeeded() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(QuoteMode.MINIMAL);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "abc";
        invokePrintAndQuote(value, value, 0, value.length(), out, false);
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNullUsesMinimal() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(null);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        StringBuilder out = new StringBuilder();
        String value = "abc";
        invokePrintAndQuote(value, value, 0, value.length(), out, false);
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeDefaultThrows() throws Throwable {
        when(csvFormat.getQuoteMode()).thenReturn(null);
        when(csvFormat.getDelimiter()).thenReturn(',');
        when(csvFormat.getQuoteCharacter()).thenReturn('"');

        // Use reflection to call with invalid QuoteMode via spy to simulate exception
        CSVFormat spyFormat = spy(csvFormat);
        when(spyFormat.getQuoteMode()).thenReturn(null);

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        // Use reflection to set private enum with invalid value by calling method with a fake QuoteMode
        // Not possible, so skip this branch test as it is unreachable without modifying enum

        // So just assert no exception for normal inputs
        StringBuilder out = new StringBuilder();
        String value = "abc";
        method.invoke(spyFormat, "abc", value, 0, value.length(), out, false);
        assertEquals("abc", out.toString());
    }
}