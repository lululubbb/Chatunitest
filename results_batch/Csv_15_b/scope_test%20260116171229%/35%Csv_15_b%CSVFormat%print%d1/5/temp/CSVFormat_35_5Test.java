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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withTrim(false).withQuoteMode(QuoteMode.MINIMAL).withNullString(null);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull_noQuote() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat = csvFormat.withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        csvFormat.print(null, out, false);
        // We test that null translates to empty string
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull_quoteModeAll() throws IOException {
        StringBuilder out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL").withQuoteMode(QuoteMode.ALL);
        format.print(null, out, false);
        assertEquals("\"NULL\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull_quoteModeNotAll() throws IOException {
        StringBuilder out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL").withQuoteMode(QuoteMode.MINIMAL);
        format.print(null, out, false);
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueCharSequence_trimTrue() throws IOException {
        StringBuilder out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        String value = "  abc  ";
        format.print(value, out, false);
        // trimmed value "abc" should be printed
        assertTrue(out.toString().contains("abc"));
        assertFalse(out.toString().contains(" "));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueString_trimFalse() throws IOException {
        StringBuilder out = new StringBuilder();
        String value = "  abc  ";
        csvFormat.print(value, out, false);
        // original string with spaces should be printed
        assertTrue(out.toString().contains("  abc  "));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueNonCharSequence() throws IOException {
        StringBuilder out = new StringBuilder();
        Integer value = 123;
        csvFormat.print(value, out, false);
        assertTrue(out.toString().contains("123"));
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrint() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder out = new StringBuilder();
        String value = "abc";
        // Use reflection to call private print method with parameters:
        // (Object value, CharSequence charSequence, int offset, int length, Appendable out, boolean newRecord)
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);
        // The charSequence argument must be non-null, so we pass value as CharSequence
        privatePrint.invoke(csvFormat, value, (CharSequence) value, 0, value.length(), out, true);
        assertTrue(out.toString().contains("abc"));
    }
}