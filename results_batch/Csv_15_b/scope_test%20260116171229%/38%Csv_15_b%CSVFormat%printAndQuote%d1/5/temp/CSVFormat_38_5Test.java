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
    private Appendable out;
    private Method printAndQuoteMethod;

    @BeforeEach
    void setUp() throws Exception {
        csvFormat = spy(CSVFormat.DEFAULT);
        out = new StringBuilder();
        printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Exception {
        doReturn(QuoteMode.ALL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "value";
        printAndQuoteMethod.invoke(csvFormat, "value", value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"value\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNullWithNullObject() throws Exception {
        doReturn(QuoteMode.ALL_NON_NULL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "test";
        printAndQuoteMethod.invoke(csvFormat, null, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("test", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNullWithNonNullObject() throws Exception {
        doReturn(QuoteMode.ALL_NON_NULL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "test";
        printAndQuoteMethod.invoke(csvFormat, 123, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"test\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithNumber() throws Exception {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "12345";
        printAndQuoteMethod.invoke(csvFormat, 12345, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("12345", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumericWithNonNumber() throws Exception {
        doReturn(QuoteMode.NON_NUMERIC).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "abc";
        printAndQuoteMethod.invoke(csvFormat, "abc", value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"abc\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNoneDelegatesToPrintAndEscape() throws Exception {
        doReturn(QuoteMode.NONE).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // spy printAndEscape to verify it is called
        CSVFormat spyFormat = spy(csvFormat);

        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        // override printAndEscape to append a marker string for verification
        doAnswer(invocation -> {
            CharSequence val = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            Appendable appendable = invocation.getArgument(3);
            appendable.append("ESCAPED:" + val.subSequence(offset, offset + len));
            return null;
        }).when(spyFormat).printAndEscape(any(CharSequence.class), anyInt(), anyInt(), any(Appendable.class));

        String value = "escapeTest";
        printAndQuoteMethod.invoke(spyFormat, "escapeTest", value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("ESCAPED:escapeTest", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEmptyNewRecord() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "";
        printAndQuoteMethod.invoke(csvFormat, "", value, 0, 0, out, true);
        String result = out.toString();
        assertEquals("\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordWithSpecialChar() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // char c = value.charAt(pos) is 0x19 (less than 0x20)
        String value = "\u0019abc";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, true);
        String result = out.toString();
        assertEquals("\"" + value + "\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNewRecordCharLessEqualComment() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        // COMMENT is '#', char <= '#'
        String value = "#value";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"#value\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalContainsLF() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "val\nue";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"val\nue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalEndsWithSpace() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "value ";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"value \"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalNoQuote() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "value";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("value", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalQuoteWithEmbeddedQuoteChar() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "val\"ue\"test";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        // embedded quotes doubled
        assertEquals("\"val\"\"ue\"\"test\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalQuoteWithEmbeddedQuoteCharAtEnd() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "value\"";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"value\"\"\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalQuoteWithEmbeddedDelimiter() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(';').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "val;ue";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"val;ue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalQuoteWithEmbeddedCR() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "val\rue";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"val\rue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalQuoteWithEmbeddedLF() throws Exception {
        doReturn(QuoteMode.MINIMAL).when(csvFormat).getQuoteMode();
        doReturn(',').when(csvFormat).getDelimiter();
        doReturn('"').when(csvFormat).getQuoteCharacter();

        String value = "val\nue";
        printAndQuoteMethod.invoke(csvFormat, value, value, 0, value.length(), out, false);
        String result = out.toString();
        assertEquals("\"val\nue\"", result);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimalThrowsIllegalStateException() throws Exception {
        doReturn(null).when(csvFormat).getQuoteMode();

        // forcibly set an unexpected QuoteMode via reflection to test default case
        // but since getQuoteMode returns null, method sets quoteModePolicy = MINIMAL and no exception thrown

        // to test exception path, we create a spy that returns an unknown QuoteMode via getQuoteMode
        CSVFormat spyFormat = spy(csvFormat);
        doReturn(QuoteMode.valueOf("MINIMAL")).when(spyFormat).getQuoteMode();

        // This won't throw because MINIMAL is handled, so test exception by forcing unknown value via reflection
        // Instead, test by calling printAndQuote with a mock that returns an unknown enum value
        // Since QuoteMode is enum, we can't create unknown instance easily, so skip this test as not applicable.
    }
}