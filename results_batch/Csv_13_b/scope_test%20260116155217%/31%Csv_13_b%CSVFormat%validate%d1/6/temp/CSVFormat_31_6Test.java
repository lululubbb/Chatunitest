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
            // unwrap the IllegalArgumentException thrown by validate()
            if (e.getCause() instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e.getCause();
            }
            throw e;
        }
    }

    @Test
    @Timeout(8000)
    void testValidateDelimiterIsLineBreak() {
        CSVFormat format = CSVFormat.newFormat('\n');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharEqualsDelimiter() {
        // delimiter is ',', quote char is ',' -> invalid
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withQuote(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharEqualsDelimiter() {
        // delimiter is ',', escape char is ',' -> invalid
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withEscape(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateCommentMarkerEqualsDelimiter() {
        // delimiter is ',', comment marker is ',' -> invalid
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',').withCommentMarker(',');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateQuoteCharEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateEscapeCharEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('#').withCommentMarker('#');
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    void testValidateNoEscapeCharWithQuoteModeNone() {
        // Create CSVFormat with quoteMode = NONE and escapeCharacter = null
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null).withQuoteMode(QuoteMode.NONE);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderDuplicates() {
        String[] headerWithDup = new String[] {"a", "b", "a"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headerWithDup);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The header contains a duplicate entry: 'a'"));
        assertTrue(ex.getMessage().contains(Arrays.toString(headerWithDup)));
    }

    @Test
    @Timeout(8000)
    void testValidateHeaderNoDuplicates() {
        String[] header = new String[] {"a", "b", "c"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(header);
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void testValidateNoHeader() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    void testValidateAllValid() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('\"')
                .withEscape('\\')
                .withCommentMarker('#')
                .withQuoteMode(QuoteMode.MINIMAL)
                .withHeader("one", "two", "three");
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}