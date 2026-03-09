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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintAndQuoteTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        out = new StringBuilder();
    }

    private void invokePrintAndQuote(CSVFormat format, Object object, CharSequence value, int offset, int len,
            Appendable out, boolean newRecord) throws IOException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(format, object, value, offset, len, out, newRecord);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll_quotesEntireValue() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.ALL);
        StringBuilder out = new StringBuilder();
        String value = "abc,def";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"abc,def\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_objectIsNumber_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        Integer number = 123;
        invokePrintAndQuote(format, number, number.toString(), 0, number.toString().length(), out, false);
        assertEquals("123", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_objectNotNumber_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        String value = "abc";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone_callsPrintAndEscape() throws Throwable {
        CSVFormat spyFormat = spy(csvFormat.withQuoteMode(QuoteMode.NONE));
        StringBuilder out = new StringBuilder();
        String value = "abc,def";
        doAnswer(invocation -> {
            Appendable a = invocation.getArgument(3);
            a.append("escaped");
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(spyFormat, value, value, 0, value.length(), out, false);

        assertEquals("escaped", out.toString());
        verify(spyFormat).printAndEscape(value, 0, value.length(), out);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_emptyLenNewRecordTrue_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        invokePrintAndQuote(format, "", "", 0, 0, out, true);
        assertEquals("\"\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_startCharNonAlphanumericNewRecordTrue_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "#start";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, true);
        assertEquals("\"#start\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_startCharLessOrEqualComment_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        char commentChar = format.getCommentMarker() != null ? format.getCommentMarker() : '#';
        String value = commentChar + "comment";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"" + value + "\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsLF_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "abc\ndef";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"abc\ndef\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsCR_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "abc\rdef";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"abc\rdef\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsQuoteChar_quotesAndDoublesQuote() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "abc\"def\"ghi";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"abc\"\"def\"\"ghi\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsDelimiter_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        char delim = format.getDelimiter();
        String value = "abc" + delim + "def";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"abc" + delim + "def\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_endsWithSpaceOrLess_quotes() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "abc ";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("\"abc \"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_noQuote_appendsDirectly() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String value = "abcDEF123";
        invokePrintAndQuote(format, value, value, 0, value.length(), out, false);
        assertEquals("abcDEF123", out.toString());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeUnexpected_throwsIllegalStateException() throws Throwable {
        CSVFormat format = csvFormat.withQuoteMode(null);
        // Using reflection to forcibly set quoteMode to an unexpected enum value by mocking
        CSVFormat spyFormat = spy(format);
        doReturn(null).when(spyFormat).getQuoteMode();

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        // We expect IllegalStateException because quoteModePolicy will be set to MINIMAL by method but test forced null
        // So to trigger default case, forcibly set quoteModePolicy to an invalid enum value with a wrapper method
        // Instead, we test by creating a subclass with overridden getQuoteMode returning invalid value
        CSVFormat invalidFormat = new CSVFormat(format.getDelimiter(), format.getQuoteCharacter(),
                null, format.getCommentMarker(), format.getEscapeCharacter(), format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(), format.getRecordSeparator(), format.getNullString(),
                format.getHeaderComments(), format.getHeader(), format.getSkipHeaderRecord(),
                format.getAllowMissingColumnNames(), format.getIgnoreHeaderCase(), format.getTrim(),
                format.getTrailingDelimiter()) {
            @Override
            public QuoteMode getQuoteMode() {
                return null;
            }
        };

        Throwable thrown = assertThrows(InvocationTargetException.class,
                () -> method.invoke(invalidFormat, "a", "a", 0, 1, new StringBuilder(), false));
        assertTrue(thrown.getCause() instanceof IllegalStateException);
    }
}