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

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
        printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord) throws Throwable {
        try {
            printAndQuoteMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL);
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNullWithNullObject() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        CharSequence value = "abc";
        invokePrintAndQuote(null, value, 0, value.length(), appendable, false);
        verify(appendable).append(value, 0, value.length());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNullWithNonNullObject() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithNumber() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        CharSequence value = "123";
        invokePrintAndQuote(123, value, 0, value.length(), appendable, false);
        verify(appendable).append(value, 0, value.length());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithNonNumber() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NONE);
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        // Should call printAndEscape, which appends to out. We cannot verify internals, so verify append called at least once.
        verify(appendable, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEmptyNewRecord() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "";
        invokePrintAndQuote("abc", value, 0, 0, appendable, true);
        verify(appendable).append('"');
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordStartsWithControlChar() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "\u0001abc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, true);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordStartsWithCommentChar() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        csvFormat = csvFormat.withCommentMarker('#');
        CharSequence value = "#abc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalContainsLf() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "ab\nc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEndsWithSpace() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "abc ";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNoQuoteNeeded() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append(value, 0, value.length());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalQuoteCharInsideValue() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        csvFormat = csvFormat.withQuote('"');
        CharSequence value = "ab\"c";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        // The quote char inside value should be doubled, so append called multiple times
        verify(appendable, atLeast(2)).append(any(CharSequence.class), anyInt(), anyInt());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalDelimiterInsideValue() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        csvFormat = csvFormat.withDelimiter(',');
        CharSequence value = "ab,c";
        invokePrintAndQuote("abc", value, 0, value.length(), appendable, false);
        verify(appendable).append('"');
        verify(appendable).append(value, 0, value.length());
        verify(appendable).append('"');
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalThrowsIllegalStateException() throws Throwable {
        csvFormat = csvFormat.withQuoteMode(null);
        // forcibly set a QuoteMode that is not recognized by switch
        CSVFormat csvFormatSpy = spy(csvFormat);
        doReturn(null).when(csvFormatSpy).getQuoteMode();

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            printAndQuoteMethod.invoke(csvFormatSpy, "abc", "abc", 0, 3, appendable, false);
        });
        assertTrue(exception.getCause() instanceof IllegalStateException || exception instanceof IllegalStateException);
    }
}