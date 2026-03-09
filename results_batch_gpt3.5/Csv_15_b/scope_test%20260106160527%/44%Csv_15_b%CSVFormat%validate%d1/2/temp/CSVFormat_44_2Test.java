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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a CSVFormat instance using reflection to access private constructor
        // Using DEFAULT and then setting fields by reflection since constructor is private
        csvFormat = CSVFormat.DEFAULT;

        // Access private validate method
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(csvFormat, value);
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Exception {
        // Set delimiter to line break char '\n'
        setField("delimiter", '\n');

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
        // No need to check message here, reflection wraps exceptions as InvocationTargetException
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsQuoteCharacter() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf(','));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsEscapeCharacter() throws Exception {
        setField("delimiter", ',');
        setField("escapeCharacter", Character.valueOf(','));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsCommentMarker() throws Exception {
        setField("delimiter", ',');
        setField("commentMarker", Character.valueOf(','));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharacterEqualsCommentMarker() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf('#'));
        setField("commentMarker", Character.valueOf('#'));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharacterEqualsCommentMarker() throws Exception {
        setField("delimiter", ',');
        setField("escapeCharacter", Character.valueOf('#'));
        setField("commentMarker", Character.valueOf('#'));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharacterAndQuoteModeNone() throws Exception {
        setField("delimiter", ',');
        setField("escapeCharacter", null);
        setField("quoteMode", QuoteMode.NONE);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderWithDuplicates() throws Exception {
        setField("delimiter", ',');
        setField("quoteMode", QuoteMode.MINIMAL);
        String[] header = new String[] {"col1", "col2", "col1"};
        setField("header", header);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderNoDuplicates() throws Exception {
        setField("delimiter", ',');
        setField("quoteMode", QuoteMode.MINIMAL);
        String[] header = new String[] {"col1", "col2", "col3"};
        setField("header", header);

        // Should not throw
        try {
            validateMethod.invoke(csvFormat);
        } catch (Exception e) {
            fail("validate() threw unexpected exception: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateAllValid() throws Exception {
        setField("delimiter", ',');
        setField("quoteCharacter", Character.valueOf('"'));
        setField("escapeCharacter", Character.valueOf('\\'));
        setField("commentMarker", Character.valueOf('#'));
        setField("quoteMode", QuoteMode.MINIMAL);
        setField("header", new String[] {"a","b","c"});

        // Should not throw
        try {
            validateMethod.invoke(csvFormat);
        } catch (Exception e) {
            fail("validate() threw unexpected exception: " + e.getCause());
        }
    }
}