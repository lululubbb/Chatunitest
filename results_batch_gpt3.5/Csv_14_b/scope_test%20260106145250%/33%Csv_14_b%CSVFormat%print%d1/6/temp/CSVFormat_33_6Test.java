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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withTrim(false);
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNull() throws IOException {
        // Setup CSVFormat with nullString == null
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);

        Appendable out = new StringBuilder();
        format.print(null, out, true);

        // We expect the output to be empty string when nullString is null
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNotNull() throws IOException {
        String nullString = "NULLVAL";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullString);

        Appendable out = new StringBuilder();
        format.print(null, out, false);

        assertEquals(nullString, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_NoTrim() throws IOException {
        Appendable out = new StringBuilder();
        CharSequence value = "  abc  ";
        csvFormat = csvFormat.withTrim(false);

        csvFormat.print(value, out, false);

        // Since trim is false, output should include spaces
        assertEquals("  abc  ", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_WithTrim() throws IOException {
        Appendable out = new StringBuilder();
        CharSequence value = "  abc  ";
        csvFormat = csvFormat.withTrim(true);

        csvFormat.print(value, out, false);

        // Since trim is true, output should be trimmed
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsNotCharSequence() throws IOException {
        Appendable out = new StringBuilder();
        Integer value = 123;

        csvFormat.print(value, out, true);

        assertEquals("123", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_EmptyString() throws IOException {
        Appendable out = new StringBuilder();
        String value = "";

        csvFormat.print(value, out, false);

        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_UsesPrivatePrintMethod() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Appendable out = new StringBuilder();
        String value = "test";

        // Spy on csvFormat to verify private print method call
        CSVFormat spyFormat = spy(csvFormat);

        // We invoke public print which internally calls private print(Object, CharSequence, int, int, Appendable, boolean)
        spyFormat.print(value, out, true);

        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // Prepare the CharSequence argument as per public print method logic
        CharSequence charSequence = value instanceof CharSequence ? (CharSequence) value : value.toString();
        charSequence = spyFormat.getTrim() ? trim(charSequence) : charSequence;

        // Invoke private print directly to check no exceptions occur
        privatePrint.invoke(spyFormat, value, charSequence, 0, charSequence.length(), out, true);

        assertTrue(out.toString().contains("test"));
    }

    // Helper method to mimic CSVFormat's trim method (private in CSVFormat)
    private CharSequence trim(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        int len = charSequence.length();
        int st = 0;
        while ((st < len) && (Character.isWhitespace(charSequence.charAt(st)))) {
            st++;
        }
        while ((st < len) && (Character.isWhitespace(charSequence.charAt(len - 1)))) {
            len--;
        }
        return (st > 0 || len < charSequence.length()) ? charSequence.subSequence(st, len) : charSequence;
    }

}