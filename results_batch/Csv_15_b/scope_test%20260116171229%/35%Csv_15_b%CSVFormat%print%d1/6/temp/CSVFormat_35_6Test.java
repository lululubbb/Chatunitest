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
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNull_QuoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        format.print(null, out, true);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringSet_QuoteModeAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.ALL);
        StringBuilder out = new StringBuilder();
        format.print(null, out, true);
        assertEquals("\"NULL\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringSet_QuoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder out = new StringBuilder();
        format.print(null, out, true);
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_TrimTrue() throws IOException {
        CSVFormat format = csvFormat.withTrim(true);
        StringBuilder out = new StringBuilder();
        CharSequence value = "  test  ";
        format.print(value, out, false);
        assertTrue(out.toString().contains("test"));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_TrimFalse() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        StringBuilder out = new StringBuilder();
        CharSequence value = "  test  ";
        format.print(value, out, false);
        assertTrue(out.toString().contains("  test  "));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsNotCharSequence() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        StringBuilder out = new StringBuilder();
        Integer value = 123;
        format.print(value, out, true);
        assertTrue(out.toString().contains("123"));
    }

    @Test
    @Timeout(8000)
    void testPrint_PrivatePrintMethodInvocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder out = new StringBuilder();
        Object value = "value";
        CharSequence charSequence = "value";
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);
        privatePrint.invoke(csvFormat, value, charSequence, 0, charSequence.length(), out, true);
        assertTrue(out.length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_WithMockedAppendable() throws IOException {
        Appendable out = mock(Appendable.class);
        String val = "mocked";
        csvFormat.print(val, out, true);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}