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
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintNullValueWithNullStringNullAndQuoteModeAll() throws IOException {
        CSVFormat format = csvFormat.withNullString(null).withQuoteMode(QuoteMode.ALL);
        Appendable out = mock(Appendable.class);

        Method privatePrint = getPrivatePrintMethod();

        format.print(null, out, true);

        try {
            // When nullString is null and quoteMode ALL,
            // charSequence is EMPTY string, no quotes added.
            privatePrint.invoke(format, null, "", 0, 0, out, true);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Reflection invocation failed: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    void testPrintNullValueWithNullStringSetAndQuoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.MINIMAL);
        Appendable out = mock(Appendable.class);

        format.print(null, out, false);

        Method privatePrint = getPrivatePrintMethod();
        try {
            // When quoteMode is not ALL, charSequence is just nullString without quotes
            privatePrint.invoke(format, null, "NULL", 0, 4, out, false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Reflection invocation failed: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    void testPrintValueIsCharSequenceWithTrimTrue() throws IOException {
        CSVFormat format = csvFormat.withTrim(true);
        Appendable out = mock(Appendable.class);

        CharSequence value = "  trimmed  ";
        format.print(value, out, false);

        Method privatePrint = getPrivatePrintMethod();
        try {
            privatePrint.invoke(format, value, "trimmed", 0, 7, out, false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Reflection invocation failed: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    void testPrintValueIsNotCharSequenceWithTrimFalse() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        Appendable out = mock(Appendable.class);

        Object value = 12345;
        format.print(value, out, true);

        Method privatePrint = getPrivatePrintMethod();
        try {
            privatePrint.invoke(format, value, "12345", 0, 5, out, true);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Reflection invocation failed: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    void testPrintWithEmptyStringValue() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        Appendable out = mock(Appendable.class);

        String value = "";
        format.print(value, out, false);

        Method privatePrint = getPrivatePrintMethod();
        try {
            privatePrint.invoke(format, value, "", 0, 0, out, false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Reflection invocation failed: " + e.getCause());
        }
    }

    private Method getPrivatePrintMethod() {
        try {
            Method method = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                    int.class, Appendable.class, boolean.class);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            fail("Private print method not found: " + e.getMessage());
            return null;
        }
    }
}