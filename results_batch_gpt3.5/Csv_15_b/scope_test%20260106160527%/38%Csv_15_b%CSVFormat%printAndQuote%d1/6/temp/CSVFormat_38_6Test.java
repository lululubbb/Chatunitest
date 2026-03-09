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

    @BeforeEach
    void setUp() {
        // Use DEFAULT CSVFormat instance for testing
        csvFormat = CSVFormat.DEFAULT;
        out = mock(Appendable.class);
    }

    private void invokePrintAndQuote(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(csvFormat, object, value, offset, len, out, newRecord);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAll() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL);
        String val = "abc,def";
        invokePrintAndQuote(val, val, 0, val.length(), out, false);
        // verify append called with quote chars and value
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, atLeastOnce()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNull_ObjectIsNumber() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        Number num = 123;
        String val = "123";
        invokePrintAndQuote(num, val, 0, val.length(), out, false);
        // Should quote because ALL_NON_NULL always quotes
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, atLeastOnce()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeAllNonNull_ObjectIsNull() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.ALL_NON_NULL);
        Object obj = null;
        String val = "";
        // Passing null object and empty value, should quote because ALL_NON_NULL
        invokePrintAndQuote(obj, val, 0, val.length(), out, false);
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, atLeastOnce()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_ObjectIsNumber() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        Number num = 42;
        String val = "42";
        invokePrintAndQuote(num, val, 0, val.length(), out, false);
        // Should not quote numeric object
        verify(out).append(val, 0, val.length());
        verify(out, never()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNonNumeric_ObjectIsString() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NON_NUMERIC);
        String val = "hello";
        invokePrintAndQuote(val, val, 0, val.length(), out, false);
        // Should quote non-numeric object
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out, atLeastOnce()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeNone() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.NONE);
        String val = "abc,def";
        // We spy to verify printAndEscape is called internally
        CSVFormat spyFormat = spy(csvFormat);
        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        method.setAccessible(true);
        method.invoke(spyFormat, val, val, 0, val.length(), out, false);
        // Should call printAndEscape internally and not quote
        verify(spyFormat).getQuoteMode();
        // We cannot verify printAndEscape directly since private, but no quote char appended
        verify(out, never()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EmptyLen_NewRecordTrue() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String val = "";
        invokePrintAndQuote(null, val, 0, 0, out, true);
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EmptyLen_NewRecordFalse() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String val = "";
        invokePrintAndQuote(null, val, 0, 0, out, false);
        // Should not quote, so append called with empty value but no quotes
        verify(out).append(val, 0, 0);
        verify(out, never()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_LeadingControlCharNewRecordTrue() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        // char < 0x20 (space) at start and newRecord true triggers quote
        String val = "\u001Fvalue";
        invokePrintAndQuote(null, val, 0, val.length(), out, true);
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_LeadingCommentChar() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        // COMMENT char is 35 '#'
        String val = "#comment";
        invokePrintAndQuote(null, val, 0, val.length(), out, false);
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ContainsDelimiter() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        // Contains delimiter ',' triggers quote
        String val = "a,b";
        invokePrintAndQuote(null, val, 0, val.length(), out, false);
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out).append(val, 0, val.length());
        verify(out).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_EndsWithSpace() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String val = "abc ";
        invokePrintAndQuote(null, val, 0, val.length(), out, false);
        verify(out).append(csvFormat.getQuoteCharacter());
        verify(out).append(val, 0, val.length());
        verify(out).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_NoQuoteNeeded() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        String val = "abc";
        invokePrintAndQuote(null, val, 0, val.length(), out, false);
        verify(out).append(val, 0, val.length());
        verify(out, never()).append(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_QuoteCharInsideValue() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        char quoteChar = csvFormat.getQuoteCharacter();
        String val = "abc" + quoteChar + "def";
        invokePrintAndQuote(null, val, 0, val.length(), out, false);
        verify(out).append(quoteChar);
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append(quoteChar);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_QuoteCharAtEnd() throws Exception {
        csvFormat = csvFormat.withQuoteMode(QuoteMode.MINIMAL);
        char quoteChar = csvFormat.getQuoteCharacter();
        String val = "abc" + quoteChar;
        invokePrintAndQuote(null, val, 0, val.length(), out, false);
        verify(out).append(quoteChar);
        verify(out, atLeastOnce()).append(any(CharSequence.class), anyInt(), anyInt());
        verify(out).append(quoteChar);
    }

    @Test
    @Timeout(8000)
    void testQuoteModeMinimal_ThrowsIllegalStateException() throws Exception {
        CSVFormat format = csvFormat.withQuoteMode(null);
        Exception ex = assertThrows(Exception.class, () -> {
            Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
            method.setAccessible(true);
            method.invoke(format, "abc", "abc", 0, 3, out, false);
        });
        Throwable cause = ex.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertTrue(cause.getMessage().contains("Unexpected Quote value"));
    }

}