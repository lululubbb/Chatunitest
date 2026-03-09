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
        // Use default CSVFormat instance for tests
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNull_QuoteModeNotAll() throws IOException {
        // Setup CSVFormat with nullString = null and quoteMode != ALL
        CSVFormat format = new CSVFormat(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                QuoteMode.MINIMAL,
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                null, // nullString = null
                null,
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(),
                CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter(),
                CSVFormat.DEFAULT.getAutoFlush());

        StringBuilder out = new StringBuilder();
        format.print(null, out, true);

        // nullString is null, so should print EMPTY (empty string)
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringSet_QuoteModeAll() throws IOException {
        // Setup CSVFormat with nullString set and QuoteMode.ALL
        CSVFormat format = new CSVFormat(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                QuoteMode.ALL,
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                "NULL",
                null,
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(),
                CSVFormat.DEFAULT.getTrim(),
                CSVFormat.DEFAULT.getTrailingDelimiter(),
                CSVFormat.DEFAULT.getAutoFlush());

        StringBuilder out = new StringBuilder();
        format.print(null, out, false);

        // Should print quoted nullString "NULL"
        String expected = format.getQuoteCharacter() + "NULL" + format.getQuoteCharacter();
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_TrimFalse() throws IOException {
        // Setup CSVFormat with trim = false
        CSVFormat format = new CSVFormat(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                QuoteMode.MINIMAL,
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                null,
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(),
                false, // trim = false
                CSVFormat.DEFAULT.getTrailingDelimiter(),
                CSVFormat.DEFAULT.getAutoFlush());

        StringBuilder out = new StringBuilder();
        CharSequence value = "  testValue  ";
        format.print(value, out, true);

        // The printed value should include spaces because trim is false
        assertTrue(out.toString().contains("testValue"));
        assertTrue(out.toString().contains("  "));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsNotCharSequence_TrimTrue() throws IOException {
        // Setup CSVFormat with trim = true
        CSVFormat format = new CSVFormat(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                QuoteMode.MINIMAL,
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                null,
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase(),
                true, // trim = true
                CSVFormat.DEFAULT.getTrailingDelimiter(),
                CSVFormat.DEFAULT.getAutoFlush());

        StringBuilder out = new StringBuilder();
        Integer value = 12345;
        format.print(value, out, false);

        // The printed value should be "12345"
        assertTrue(out.toString().contains("12345"));
    }

    @Test
    @Timeout(8000)
    void testPrint_InvokesPrivatePrintMethod() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        // Spy on CSVFormat to verify private print method invocation
        CSVFormat spyFormat = spy(csvFormat);

        StringBuilder out = new StringBuilder();
        String value = "value";

        // Use reflection to get private print method
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // Call focal print method
        spyFormat.print(value, out, true);

        // Verify private print method was called with expected parameters
        verify(spyFormat, times(1)).print(eq(value), eq(value), eq(0), eq(value.length()), eq(out), eq(true));
    }
}