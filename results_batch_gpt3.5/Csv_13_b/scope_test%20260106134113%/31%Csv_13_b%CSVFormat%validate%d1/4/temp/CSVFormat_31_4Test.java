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

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private Method validateMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // unwrap IllegalArgumentException
            if (e.getCause() instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e.getCause();
            }
            throw e;
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterIsLineBreak() throws Exception {
        CSVFormat format = CSVFormat.newFormat('\n');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharSameAsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithQuote = format.withQuote(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithQuote));
        assertEquals("The quoteChar character and the delimiter cannot be the same ('" + ',' + "')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharSameAsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithEscape = format.withEscape(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithEscape));
        assertEquals("The escape character and the delimiter cannot be the same ('" + ',' + "')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateCommentMarkerSameAsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithComment = format.withCommentMarker(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithComment));
        assertEquals("The comment start character and the delimiter cannot be the same ('" + ',' + "')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharSameAsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithQuoteAndComment = format.withQuote('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithQuoteAndComment));
        assertEquals("The comment start character and the quoteChar cannot be the same ('" + '#' + "')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharSameAsCommentMarker() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithEscapeAndComment = format.withEscape('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithEscapeAndComment));
        assertEquals("The comment start and the escape character cannot be the same ('" + '#' + "')", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateNoEscapeCharacterWithQuoteModeNone() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithNoEscapeAndQuoteNone = format.withEscape(null).withQuoteMode(QuoteMode.NONE);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithNoEscapeAndQuoteNone));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderWithDuplicate() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithDuplicateHeader = format.withHeader("a", "b", "a");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithDuplicateHeader));
        assertTrue(ex.getMessage().startsWith("The header contains a duplicate entry: 'a' in "));
        assertTrue(ex.getMessage().contains(Arrays.toString(new String[]{"a", "b", "a"})));
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderNoDuplicates() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithHeader = format.withHeader("a", "b", "c");
        assertDoesNotThrow(() -> invokeValidate(formatWithHeader));
    }

    @Test
    @Timeout(8000)
    void testValidateDefaultFormat() throws Exception {
        // DEFAULT format should pass validation
        CSVFormat format = CSVFormat.DEFAULT;
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}