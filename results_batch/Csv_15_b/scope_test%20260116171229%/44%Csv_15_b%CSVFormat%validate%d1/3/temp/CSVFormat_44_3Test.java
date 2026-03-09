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
import java.io.IOException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormat = CSVFormat.DEFAULT;

        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // For primitive char field, handle value unboxing
        if (field.getType() == char.class && value instanceof Character) {
            field.setChar(csvFormat, (Character) value);
        } else {
            field.set(csvFormat, value);
        }
    }

    private void invokeValidate() throws Throwable {
        try {
            validateMethod.invoke(csvFormat);
        } catch (InvocationTargetException e) {
            // unwrap the underlying exception thrown by validate()
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Exception {
        setField("delimiter", '\n'); // LF line break
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsDelimiter() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf(','));
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsDelimiter() throws Exception {
        setField("delimiter", ',');
        setField("escapeCharacter", Character.valueOf(','));
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateCommentMarkerEqualsDelimiter() throws Exception {
        setField("delimiter", ',');
        setField("commentMarker", Character.valueOf(','));
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsCommentMarker() throws Exception {
        setField("quoteCharacter", Character.valueOf('Q'));
        setField("commentMarker", Character.valueOf('Q'));
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsCommentMarker() throws Exception {
        setField("escapeCharacter", Character.valueOf('E'));
        setField("commentMarker", Character.valueOf('E'));
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharNullAndQuoteModeNone() throws Exception {
        setField("escapeCharacter", null);
        setField("quoteMode", QuoteMode.NONE);
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderWithDuplicates() throws Exception {
        String[] header = new String[] {"col1", "col2", "col1"};
        setField("header", header);
        assertThrows(IllegalArgumentException.class, this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderNoDuplicates() throws Exception {
        String[] header = new String[] {"col1", "col2", "col3"};
        setField("header", header);
        assertDoesNotThrow(this::invokeValidate);
    }

    @Test
    @Timeout(8000)
    public void testValidateNoExceptions() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf('"'));
        setField("escapeCharacter", Character.valueOf('\\'));
        setField("commentMarker", Character.valueOf('#'));
        setField("quoteMode", QuoteMode.MINIMAL);
        setField("header", new String[] {"a", "b", "c"});
        assertDoesNotThrow(this::invokeValidate);
    }
}