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
        // Use default CSVFormat instance for tests, can customize via withQuoteMode etc.
        csvFormat = CSVFormat.DEFAULT;
    }

    private String invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out,
            boolean newRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(csvFormat, object, value, offset, len, out, newRecord);
        return out.toString();
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll_quotesAll() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL);
        StringBuilder out = new StringBuilder();
        String input = "value";
        String result = invokePrintAndQuote("value", input, 0, input.length(), out, false);
        assertEquals("\"value\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNull_objectNonNull_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        StringBuilder out = new StringBuilder();
        String input = "hello";
        String result = invokePrintAndQuote("hello", input, 0, input.length(), out, false);
        assertEquals("\"hello\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNull_objectNull_noQuotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        StringBuilder out = new StringBuilder();
        String input = "null";
        String result = invokePrintAndQuote(null, input, 0, input.length(), out, false);
        // null object triggers no quote in ALL_NON_NULL mode, so minimal quoting rules apply
        // but minimal quoting will quote if necessary, here 'n' is 'n' no special char, minimal no quote
        assertEquals("null", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_number_noQuote() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        String input = "12345";
        String result = invokePrintAndQuote(12345, input, 0, input.length(), out, false);
        assertEquals("12345", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_nonNumber_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        String input = "abc";
        String result = invokePrintAndQuote("abc", input, 0, input.length(), out, false);
        assertEquals("\"abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone_callsPrintAndEscape() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NONE);
        Appendable out = mock(Appendable.class);
        CharSequence value = "value";
        // We spy on csvFormat to verify printAndEscape is called
        CSVFormat spyFormat = spy(csvFormat);
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(spyFormat, "value", value, 0, value.length(), out, false);
        verify(spyFormat).printAndEscape(value, 0, value.length(), out);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_emptyTokenNewRecord_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        String input = "";
        String result = invokePrintAndQuote("", input, 0, 0, out, true);
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_newRecordFirstCharNeedsQuote() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        // char at pos = 0 is 0x01 which is < 0x20 so requires quoting when newRecord true
        CharSequence value = "\u0001abc";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, true);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("\u0001abc"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_startCharLessEqualComment_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        // COMMENT is '#', char <= '#' means quote
        CharSequence value = "#comment";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("#comment"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsLF_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "abc\ndef";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("abc\ndef"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsCR_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "abc\rdef";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("abc\rdef"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsQuoteChar_quotesAndDoublesQuotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL).withQuote('"');
        StringBuilder out = new StringBuilder();
        CharSequence value = "ab\"cd\"ef";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        // The quotes inside should be doubled
        assertTrue(result.contains("\"\""));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_containsDelimiter_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL).withDelimiter(',');
        StringBuilder out = new StringBuilder();
        CharSequence value = "ab,cd";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
        assertTrue(result.contains("ab,cd"));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_endCharLessEqualSP_quotes() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "abc ";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, false);
        assertTrue(result.startsWith("\""));
        assertTrue(result.endsWith("\""));
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_noQuote_appendsValueDirectly() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "abc";
        String result = invokePrintAndQuote("value", value, 0, value.length(), out, false);
        assertEquals("abc", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeUnexpected_throwsIllegalStateException() throws Exception {
        csvFormat = csvFormat.withQuoteMode(null);
        // Use reflection to set private field quoteMode to an invalid enum value via proxy
        CSVFormat spyFormat = spy(csvFormat);
        doReturn(null).when(spyFormat).getQuoteMode();

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        // We expect IllegalStateException only if quoteMode is not handled - but null sets MINIMAL
        // So forcibly set to an invalid enum by subclassing or mocking getQuoteMode
        CSVFormat formatWithInvalidQuoteMode = spy(csvFormat);
        doReturn(QuoteMode.valueOf("ALL")).when(formatWithInvalidQuoteMode).getQuoteMode();
        // This won't throw, so try an invalid enum by mocking getQuoteMode to a new enum instance
        QuoteMode invalidQuoteMode = mock(QuoteMode.class);
        doReturn(invalidQuoteMode).when(formatWithInvalidQuoteMode).getQuoteMode();

        // Use reflection to invoke with invalid quoteMode to trigger default case
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            method.invoke(formatWithInvalidQuoteMode, "value", "abc", 0, 3, new StringBuilder(), false);
        });
        assertTrue(thrown.getCause().getMessage().startsWith("Unexpected Quote value"));
    }
}