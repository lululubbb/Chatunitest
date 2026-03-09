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
        // Using default CSVFormat instance
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull_quoteModeNotAll() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Use reflection to create CSVFormat instance because constructor is private
        Method constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = (CSVFormat) constructor.invoke(null, ',', '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false, false);

        Appendable out = new StringBuilder();
        format.print(null, out, true);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet_quoteModeAll() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = (CSVFormat) constructor.invoke(null, ',', '"', QuoteMode.ALL, null, null, false, true, "\r\n",
                "NULL", null, null, false, false, false, false, false, false);

        Appendable out = new StringBuilder();
        format.print(null, out, false);
        assertEquals("\"NULL\"", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet_quoteModeNotAll() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = (CSVFormat) constructor.invoke(null, ',', '"', QuoteMode.MINIMAL, null, null, false, true, "\r\n",
                "NULL", null, null, false, false, false, false, false, false);

        Appendable out = new StringBuilder();
        format.print(null, out, true);
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimTrue() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = (CSVFormat) constructor.invoke(null, ',', '"', QuoteMode.MINIMAL, null, null, false, true, "\r\n",
                null, null, null, false, false, true, false, false, false);

        Appendable out = new StringBuilder();
        CharSequence value = "  abc  ";
        format.print(value, out, false);
        // The private print method appends output, so output should contain trimmed string
        assertTrue(out.toString().contains("abc"));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsNotCharSequence_trimFalse() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = (CSVFormat) constructor.invoke(null, ',', '"', QuoteMode.MINIMAL, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false, false);

        Appendable out = new StringBuilder();
        Integer value = 123;
        format.print(value, out, true);
        assertTrue(out.toString().contains("123"));
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintMethodInvocation() throws IOException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Appendable out = new StringBuilder();
        String testValue = "test";
        CharSequence charSequence = testValue;
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        CSVFormat format = CSVFormat.DEFAULT;
        printMethod.invoke(format, testValue, charSequence, 0, charSequence.length(), out, true);
        assertTrue(out.toString().contains(testValue));
    }

    @Test
    @Timeout(8000)
    void testPrint_withMockedAppendable() throws IOException {
        Appendable out = mock(Appendable.class);
        String value = "mocked";
        CSVFormat format = CSVFormat.DEFAULT;
        format.print(value, out, false);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_withEmptyStringValue() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        format.print("", out, true);
        // Should print empty string without exception
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_withQuoteCharacterSet() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"').withQuoteMode(QuoteMode.ALL);
        Appendable out = new StringBuilder();
        String value = "quoted";
        format.print(value, out, false);
        assertTrue(out.toString().startsWith("\""));
        assertTrue(out.toString().endsWith("\""));
    }
}