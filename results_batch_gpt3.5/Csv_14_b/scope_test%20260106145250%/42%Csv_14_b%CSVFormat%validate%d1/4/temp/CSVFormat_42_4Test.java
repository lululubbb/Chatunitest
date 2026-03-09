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
import java.io.IOException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private void invokeValidate(CSVFormat format) throws Throwable {
        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterLineBreak() throws Throwable {
        CSVFormat format = new CSVFormat('\n', null, QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, null,
                false, false, false, false, false);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("delimiter cannot be a line break")) {
            fail("Expected message about delimiter cannot be a line break");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsQuoteCharacter() throws Throwable {
        CSVFormat format = new CSVFormat(',', '\"', QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, null,
                false, false, false, false, false);
        // forcibly set delimiter same as quoteChar
        CSVFormat format2 = new CSVFormat('\"', '\"', QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, null,
                false, false, false, false, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format2));
        if (!thrown.getMessage().contains("quoteChar character and the delimiter cannot be the same")) {
            fail("Expected message about quoteChar and delimiter cannot be the same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsEscapeCharacter() throws Throwable {
        CSVFormat format = new CSVFormat(',', '\"', QuoteMode.MINIMAL, null, '\\',
                false, false, "\n", null, null, null,
                false, false, false, false, false);
        CSVFormat format2 = new CSVFormat('\\', '\"', QuoteMode.MINIMAL, null, '\\',
                false, false, "\n", null, null, null,
                false, false, false, false, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format2));
        if (!thrown.getMessage().contains("escape character and the delimiter cannot be the same")) {
            fail("Expected message about escape character and delimiter cannot be the same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsCommentMarker() throws Throwable {
        CSVFormat format = new CSVFormat(',', '\"', QuoteMode.MINIMAL, '#', null,
                false, false, "\n", null, null, null,
                false, false, false, false, false);
        CSVFormat format2 = new CSVFormat('#', '\"', QuoteMode.MINIMAL, '#', null,
                false, false, "\n", null, null, null,
                false, false, false, false, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format2));
        if (!thrown.getMessage().contains("comment start character and the delimiter cannot be the same")) {
            fail("Expected message about comment start character and delimiter cannot be the same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteEqualsCommentMarker() throws Throwable {
        CSVFormat format = new CSVFormat(',', '#', QuoteMode.MINIMAL, '#', null,
                false, false, "\n", null, null, null,
                false, false, false, false, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start character and the quoteChar cannot be the same")) {
            fail("Expected message about comment start character and quoteChar cannot be the same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeEqualsCommentMarker() throws Throwable {
        CSVFormat format = new CSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '#',
                false, false, "\n", null, null, null,
                false, false, false, false, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("comment start and the escape character cannot be the same")) {
            fail("Expected message about comment start and escape character cannot be the same");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharacterAndQuoteModeNone() throws Throwable {
        CSVFormat format = new CSVFormat(',', null, QuoteMode.NONE, null, null,
                false, false, "\n", null, null, null,
                false, false, false, false, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("No quotes mode set but no escape character is set")) {
            fail("Expected message about no quotes mode and no escape character");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDuplicateHeader() throws Throwable {
        String[] headers = new String[] { "A", "B", "A" };
        CSVFormat format = new CSVFormat(',', '\"', QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, headers,
                false, false, false, false, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        if (!thrown.getMessage().contains("header contains a duplicate entry")) {
            fail("Expected message about duplicate header entries");
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateValid() throws Throwable {
        String[] headers = new String[] { "A", "B", "C" };
        CSVFormat format = new CSVFormat(',', '\"', QuoteMode.MINIMAL, '#', '\\',
                false, false, "\n", null, null, headers,
                false, false, false, false, false);

        // Should not throw
        invokeValidate(format);
    }
}