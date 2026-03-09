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
        csvFormat = CSVFormat.DEFAULT;
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out,
            boolean newRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(csvFormat, object, value, offset, len, out, newRecord);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeAll() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL);
        StringBuilder out = new StringBuilder();
        invokePrintAndQuote("abc", "abc", 0, 3, out, false);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeAllNonNull_objectNull() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        StringBuilder out = new StringBuilder();
        invokePrintAndQuote(null, "", 0, 0, out, false);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeAllNonNull_objectNonNull() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        StringBuilder out = new StringBuilder();
        invokePrintAndQuote("abc", "abc", 0, 3, out, false);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeNonNumeric_withNumber() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        invokePrintAndQuote(123, "123", 0, 3, out, false);
        assertEquals("123", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeNonNumeric_withNonNumber() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        StringBuilder out = new StringBuilder();
        invokePrintAndQuote("abc", "abc", 0, 3, out, false);
        assertEquals("\"abc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeNone() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NONE);
        StringBuilder out = new StringBuilder();
        // The method printAndEscape is private, so we test that the output equals the input (no quotes)
        invokePrintAndQuote("abc", "abc", 0, 3, out, false);
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_emptyNewRecord() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        invokePrintAndQuote("",
                "", 0, 0, out, true);
        assertEquals("\"\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_newRecordSpecialChar() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "\u001Fabc"; // char < 0x20 triggers quote
        invokePrintAndQuote("abc", value, 0, value.length(), out, true);
        assertEquals("\"" + value + "\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_newRecordCommentChar() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "#abc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, true);
        assertEquals("\"" + value + "\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_containsLF() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "ab\nc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("\"ab\nc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_containsCR() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "ab\rc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("\"ab\rc\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_containsQuoteChar() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "ab\"c";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        // Quotes doubled inside output
        assertEquals("\"ab\"\"c\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_containsDelimiter() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        csvFormat = csvFormat.withDelimiter(';');
        StringBuilder out = new StringBuilder();
        CharSequence value = "ab;c";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("\"ab;c\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_endsWithSpace() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "abc ";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("\"abc \"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_quoteModeMinimal_noQuoteNeeded() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        CharSequence value = "abc";
        invokePrintAndQuote("abc", value, 0, value.length(), out, false);
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_unexpectedQuoteMode() throws Exception {
        // Use reflection to set quoteMode to null to trigger default MINIMAL (covered above)
        // Instead, create a subclass to override getQuoteMode to return a bogus value to test exception
        CSVFormat format = new CSVFormat(csvFormat.getDelimiter(), csvFormat.getQuoteCharacter(),
                null, csvFormat.getCommentMarker(), csvFormat.getEscapeCharacter(),
                csvFormat.getIgnoreSurroundingSpaces(), csvFormat.getIgnoreEmptyLines(),
                csvFormat.getRecordSeparator(), csvFormat.getNullString(),
                null, csvFormat.getHeader(), csvFormat.getSkipHeaderRecord(),
                csvFormat.getAllowMissingColumnNames(), csvFormat.getIgnoreHeaderCase(),
                csvFormat.getTrim(), csvFormat.getTrailingDelimiter(), csvFormat.getAutoFlush()) {
            @Override
            public QuoteMode getQuoteMode() {
                return null;
            }
        };
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        StringBuilder out = new StringBuilder();
        Exception ex = assertThrows(IllegalStateException.class, () -> {
            method.invoke(format, "abc", "abc", 0, 3, out, false);
        });
        assertTrue(ex.getCause() instanceof IllegalStateException);
        assertTrue(ex.getCause().getMessage().contains("Unexpected Quote value"));
    }
}