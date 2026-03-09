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
        csvFormat = CSVFormat.DEFAULT.withTrim(true);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws IOException {
        // Setup CSVFormat with nullString = null
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null).withTrim(false);

        Appendable out = mock(Appendable.class);

        // Call public print method with null value
        format.print(null, out, true);

        // Verify Appendable append calls
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull() throws IOException {
        String nullStr = "NULL";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr).withTrim(false);

        Appendable out = new StringBuilder();

        format.print(null, out, false);

        assertTrue(out.toString().contains(nullStr));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimTrue() throws IOException {
        String input = "  abc  ";
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);

        Appendable out = new StringBuilder();

        format.print(input, out, false);

        // The trimmed value "abc" should be printed
        assertTrue(out.toString().contains("abc"));
        assertFalse(out.toString().contains("  abc  "));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsNotCharSequence_trimFalse() throws IOException {
        Integer input = 123;

        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);

        Appendable out = new StringBuilder();

        format.print(input, out, true);

        // The string "123" should be printed
        assertTrue(out.toString().contains("123"));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimFalse() throws IOException {
        String input = "  abc  ";
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);

        Appendable out = new StringBuilder();

        format.print(input, out, false);

        // The original string with spaces should be printed
        assertTrue(out.toString().contains("  abc  "));
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintMethodInvocation() throws Throwable {
        Appendable out = mock(Appendable.class);

        CharSequence value = "value";

        // Use reflection to invoke private print method
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // Call private print method with parameters
        privatePrint.invoke(csvFormat, value, value, 0, value.length(), out, true);

        // Verify append called on Appendable
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}