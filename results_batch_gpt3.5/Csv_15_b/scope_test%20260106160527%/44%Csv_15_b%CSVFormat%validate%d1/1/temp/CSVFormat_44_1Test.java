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
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() throws Exception {
        // Use the default constructor via reflection to create instance with default values
        csvFormat = CSVFormat.DEFAULT;
    }

    private void setField(Object instance, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private void invokeValidate(CSVFormat instance) throws Exception {
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);
        validate.invoke(instance);
    }

    @Test
    @Timeout(8000)
    public void testValidateWithValidDefaults() throws Exception {
        // Should not throw any exception
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b", "c");
        invokeValidate(format);
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('\n');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("delimiter cannot be a line break");
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsQuoteCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        setField(format, "quoteCharacter", Character.valueOf(','));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("quoteChar character and the delimiter cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsEscapeCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        setField(format, "escapeCharacter", Character.valueOf(';'));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("escape character and the delimiter cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('#');
        setField(format, "commentMarker", Character.valueOf('#'));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("comment start character and the delimiter cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharacterEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        setField(format, "quoteCharacter", Character.valueOf('!'));
        setField(format, "commentMarker", Character.valueOf('!'));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("comment start character and the quoteChar cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharacterEqualsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        setField(format, "escapeCharacter", Character.valueOf('$'));
        setField(format, "commentMarker", Character.valueOf('$'));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("comment start and the escape character cannot be the same");
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharacterAndQuoteModeNone() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        setField(format, "escapeCharacter", null);
        setField(format, "quoteMode", QuoteMode.NONE);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("No quotes mode set but no escape character is set");
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderWithDuplicateEntries() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        String[] dupHeader = new String[] { "a", "b", "a" };
        setField(format, "header", dupHeader);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assert ex.getCause() instanceof IllegalArgumentException;
        assert ex.getCause().getMessage().contains("header contains a duplicate entry");
        assert ex.getCause().getMessage().contains("a");
        assert ex.getCause().getMessage().contains(Arrays.toString(dupHeader));
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderWithUniqueEntries() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        String[] uniqueHeader = new String[] { "a", "b", "c" };
        setField(format, "header", uniqueHeader);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}