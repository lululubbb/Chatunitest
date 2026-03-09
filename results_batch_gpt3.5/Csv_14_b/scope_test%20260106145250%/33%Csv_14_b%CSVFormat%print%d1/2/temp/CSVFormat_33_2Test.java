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
import java.lang.reflect.Constructor;
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

    private CSVFormat createFormatWithNullString(String nullString) throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(',', '"', null, null, null, false, true, "\r\n",
                nullString, null, null, false, false, false, false, false);
    }

    @Test
    @Timeout(8000)
    void testPrintWithNullValueAndNullStringNull() throws Exception {
        CSVFormat format = createFormatWithNullString(null);
        StringBuilder out = new StringBuilder();
        format.print(null, out, true);
        // Since nullString is null, it uses Constants.EMPTY which is "" (empty string)
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintWithNullValueAndNullStringSet() throws Exception {
        CSVFormat format = createFormatWithNullString("NULL");
        StringBuilder out = new StringBuilder();
        format.print(null, out, true);
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintWithCharSequenceValueAndTrimTrue() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        StringBuilder out = new StringBuilder();
        String value = "  test  ";
        format.print(value, out, false);
        // The trimmed value should be printed, so "test"
        assertTrue(out.length() > 0);
        assertTrue(out.toString().contains("test"));
        assertFalse(out.toString().contains("  "));
    }

    @Test
    @Timeout(8000)
    void testPrintWithCharSequenceValueAndTrimFalse() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        StringBuilder out = new StringBuilder();
        String value = "  test  ";
        format.print(value, out, false);
        assertTrue(out.length() > 0);
        // Since trim is false, output should contain the spaces
        assertTrue(out.toString().contains("  test  "));
    }

    @Test
    @Timeout(8000)
    void testPrintWithNonCharSequenceValue() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        StringBuilder out = new StringBuilder();
        Integer value = 123;
        format.print(value, out, true);
        assertTrue(out.length() > 0);
        assertTrue(out.toString().contains("123"));
    }

    @Test
    @Timeout(8000)
    void testPrintInvokesPrivatePrint() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder out = new StringBuilder();
        String value = "value";
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class,
                int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // call private print directly
        privatePrint.invoke(csvFormat, value, value, 0, value.length(), out, true);

        assertTrue(out.length() > 0);
        assertTrue(out.toString().contains("value"));
    }

    @Test
    @Timeout(8000)
    void testPrintWithEmptyString() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        StringBuilder out = new StringBuilder();
        String value = "";
        format.print(value, out, false);
        // Should print empty string
        assertEquals("", out.toString());
    }

}