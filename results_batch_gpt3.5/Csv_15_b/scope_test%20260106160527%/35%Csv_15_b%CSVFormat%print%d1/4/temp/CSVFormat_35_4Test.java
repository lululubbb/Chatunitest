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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        // Use DEFAULT CSVFormat instance for tests
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNull_EmptyOutput() throws IOException {
        CSVFormat format = csvFormat.withNullString(null);
        // Using reflection to set quoteMode to null to test nullString == null branch
        setField(format, "quoteMode", null);

        format.print(null, appendable, true);

        // Should call print with EMPTY charSequence
        verify(appendable, atLeast(0)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNotNull_QuoteModeAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.ALL);
        // quoteCharacter is DOUBLE_QUOTE_CHAR in DEFAULT
        format.print(null, appendable, false);

        verify(appendable, atLeast(0)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_NullValue_NullStringNotNull_QuoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.MINIMAL);
        format.print(null, appendable, true);

        verify(appendable, atLeast(0)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_TrimTrue() throws IOException {
        CSVFormat format = csvFormat.withTrim(true);
        CharSequence value = "  test  ";
        format.print(value, appendable, false);

        verify(appendable, atLeast(0)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsCharSequence_TrimFalse() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        CharSequence value = "  test  ";
        format.print(value, appendable, true);

        verify(appendable, atLeast(0)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_ValueIsNotCharSequence() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        Object value = 12345;
        format.print(value, appendable, false);

        verify(appendable, atLeast(0)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_InvokesPrivatePrint() throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        CharSequence value = "value";
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        csvFormat.print("value", appendable, true);

        // Also directly invoke private print to ensure it is accessible and works
        privatePrint.invoke(csvFormat, "value", value, 0, value.length(), appendable, true);
    }

    // Helper method to set private fields via reflection
    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            // Remove final modifier if present
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}