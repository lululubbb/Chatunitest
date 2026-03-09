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
    void testPrint_nullValue_nullString_null() throws IOException {
        // Setup CSVFormat with nullString = null
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        Appendable out = new StringBuilder();

        // Invoke public print
        format.print(null, out, true);

        // Output should be empty string
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        Appendable out = new StringBuilder();
        format.print(null, out, false);
        // Output should contain "NULL"
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimFalse() throws IOException {
        Appendable out = new StringBuilder();
        String value = "  abc  ";
        csvFormat = csvFormat.withTrim(false);
        csvFormat.print(value, out, false);
        // Output should contain original value
        assertEquals("  abc  ", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimTrue() throws IOException {
        Appendable out = new StringBuilder();
        String value = "  abc  ";
        csvFormat = csvFormat.withTrim(true);
        csvFormat.print(value, out, false);
        // Output should contain trimmed value
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsNotCharSequence() throws IOException {
        Appendable out = new StringBuilder();
        Integer value = 123;
        csvFormat.print(value, out, true);
        // Output should contain string representation of value
        assertEquals("123", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_trimNullCharSequence() throws IOException {
        // Use reflection to invoke private trim with null
        try {
            Method trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
            trimMethod.setAccessible(true);
            CharSequence result = (CharSequence) trimMethod.invoke(csvFormat, (CharSequence) null);
            assertNull(result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection failure: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintInvocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Appendable out = new StringBuilder();
        String value = "abc";
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // Prepare the CharSequence argument as the substring of value (whole string)
        CharSequence charSequence = value;

        // Invoke private print directly
        privatePrint.invoke(csvFormat, value, charSequence, 0, charSequence.length(), out, true);

        // Output should contain "abc"
        assertEquals("abc", out.toString());
    }
}