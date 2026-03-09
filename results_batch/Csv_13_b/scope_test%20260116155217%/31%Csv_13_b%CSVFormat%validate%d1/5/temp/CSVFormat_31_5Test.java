package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_31_5Test {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a CSVFormat instance with default constructor via reflection
        csvFormat = CSVFormat.newFormat(',');

        // Access private validate method
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        method.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidate_noException_default() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        // No quoteCharacter, escapeCharacter, commentMarker, header
        // Should not throw exception
        assertDoesNotThrow(() -> {
            Method method = CSVFormat.class.getDeclaredMethod("validate");
            method.setAccessible(true);
            method.invoke(format);
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_delimiterIsLineBreak_throws() throws Exception {
        // delimiter = '\n' (LF)
        CSVFormat format = CSVFormat.newFormat('\n');
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> method.invoke(format));
        // InvocationTargetException wraps the cause, so unwrap
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = new CSVFormat(',', Character.valueOf(','), QuoteMode.MINIMAL, null, null, false, true,
                "\r\n", null, null, null, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), QuoteMode.MINIMAL, null, Character.valueOf(','), false,
                true, "\r\n", null, null, null, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() throws Exception {
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), QuoteMode.MINIMAL, Character.valueOf(','), null, false,
                true, "\r\n", null, null, null, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsQuoteChar_throws() throws Exception {
        Character c = Character.valueOf('"');
        CSVFormat format = new CSVFormat(',', c, QuoteMode.MINIMAL, c, null, false, true, "\r\n", null, null, null, false,
                false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsEscapeChar_throws() throws Exception {
        Character c = Character.valueOf('\\');
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), QuoteMode.MINIMAL, c, c, false, true, "\r\n", null,
                null, null, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_noEscapeCharacterAndQuoteModeNone_throws() throws Exception {
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), QuoteMode.NONE, null, null, false, true, "\r\n", null,
                null, null, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithDuplicates_throws() throws Exception {
        String[] header = new String[] { "a", "b", "a" };
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), QuoteMode.MINIMAL, null, null, false, true, "\r\n",
                null, null, header, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithoutDuplicates_noException() throws Exception {
        String[] header = new String[] { "a", "b", "c" };
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), QuoteMode.MINIMAL, null, null, false, true, "\r\n",
                null, null, header, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("validate");
        method.setAccessible(true);
        assertDoesNotThrow(() -> {
            try {
                method.invoke(format);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }
}