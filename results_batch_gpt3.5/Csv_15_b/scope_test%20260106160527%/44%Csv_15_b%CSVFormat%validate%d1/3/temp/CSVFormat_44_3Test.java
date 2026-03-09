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
    public void setUp() {
        // Create a CSVFormat instance with default values using reflection since constructor is private
        csvFormat = CSVFormat.DEFAULT;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);
        validate.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidate_noException_default() throws Exception {
        // Default CSVFormat should pass validation
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }

    @Test
    @Timeout(8000)
    public void testValidate_delimiterIsLineBreak_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat('\n'); // line break delimiter
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        // Cause is IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setField(format, "quoteCharacter", Character.valueOf(','));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setField(format, "escapeCharacter", Character.valueOf(','));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setField(format, "commentMarker", Character.valueOf(','));
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsCommentMarker_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        Character c = Character.valueOf('#');
        setField(format, "quoteCharacter", c);
        setField(format, "commentMarker", c);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsCommentMarker_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        Character c = Character.valueOf('#');
        setField(format, "escapeCharacter", c);
        setField(format, "commentMarker", c);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharNullQuoteModeNone_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        setField(format, "escapeCharacter", null);
        setField(format, "quoteMode", QuoteMode.NONE);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerDuplicates_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        String[] header = new String[] {"a", "b", "a"};
        setField(format, "header", header);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertThrows(IllegalArgumentException.class, () -> { throw ex.getCause(); });
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerNoDuplicates_noException() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        String[] header = new String[] {"a", "b", "c"};
        setField(format, "header", header);
        assertDoesNotThrow(() -> invokeValidate(format));
    }

}