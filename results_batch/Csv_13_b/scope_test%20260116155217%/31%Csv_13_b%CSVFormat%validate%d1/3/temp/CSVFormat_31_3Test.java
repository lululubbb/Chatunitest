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
    public void setUp() throws NoSuchMethodException {
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Throwable {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // Unwrap the IllegalArgumentException thrown by validate()
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() {
        CSVFormat format = CSVFormat.newFormat('\n');
        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterSameAsQuoteChar() {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithQuote = format.withQuote(',');
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithQuote));
        assertEquals("The quoteChar character and the delimiter cannot be the same ('" + formatWithQuote.getQuoteCharacter() + "')", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterSameAsEscapeCharacter() {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithEscape = format.withEscape(',');
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithEscape));
        assertEquals("The escape character and the delimiter cannot be the same ('" + formatWithEscape.getEscapeCharacter() + "')", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterSameAsCommentMarker() {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithComment = format.withCommentMarker(',');
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithComment));
        assertEquals("The comment start character and the delimiter cannot be the same ('" + formatWithComment.getCommentMarker() + "')", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharSameAsCommentMarker() {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithQuote = format.withQuote('#');
        CSVFormat formatWithQuoteAndComment = formatWithQuote.withCommentMarker('#');
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithQuoteAndComment));
        assertEquals("The comment start character and the quoteChar cannot be the same ('" + formatWithQuoteAndComment.getCommentMarker() + "')", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharacterSameAsCommentMarker() {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithEscape = format.withEscape('#');
        CSVFormat formatWithEscapeAndComment = formatWithEscape.withCommentMarker('#');
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithEscapeAndComment));
        assertEquals("The comment start and the escape character cannot be the same ('" + formatWithEscapeAndComment.getCommentMarker() + "')", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharacterWithQuoteModeNone() {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithQuoteModeNone = format.withQuoteMode(QuoteMode.NONE).withEscape(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithQuoteModeNone));
        assertEquals("No quotes mode set but no escape character is set", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderWithDuplicate() {
        CSVFormat format = CSVFormat.newFormat(',');
        CSVFormat formatWithHeader = format.withHeader("a", "b", "a");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> invokeValidate(formatWithHeader));
        assertTrue(thrown.getMessage().startsWith("The header contains a duplicate entry: 'a' in "));
        assertTrue(thrown.getMessage().contains(Arrays.toString(formatWithHeader.getHeader())));
    }

    @Test
    @Timeout(8000)
    public void testValidateSuccessMinimal() throws Throwable {
        CSVFormat format = CSVFormat.newFormat(',');
        // Should not throw
        invokeValidate(format);
    }

    @Test
    @Timeout(8000)
    public void testValidateSuccessComplex() throws Throwable {
        CSVFormat format = CSVFormat.newFormat(';')
                .withQuote('"')
                .withEscape('\\')
                .withCommentMarker('#')
                .withQuoteMode(QuoteMode.ALL)
                .withHeader("a", "b", "c");
        // Should not throw
        invokeValidate(format);
    }
}