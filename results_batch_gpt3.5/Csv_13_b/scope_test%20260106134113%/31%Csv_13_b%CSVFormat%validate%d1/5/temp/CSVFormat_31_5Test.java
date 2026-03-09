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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;

    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Use the public factory method to create an instance with default values
        csvFormat = CSVFormat.newFormat(','); // delimiter comma by default

        // Access private validate method via reflection
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        // invoke private validate method
        validateMethod.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Exception {
        CSVFormat format = CSVFormat.newFormat('\n'); // LF is line break
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote(','); // quoteChar same as delimiter
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The quoteChar character and the delimiter cannot be the same (',')", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote('"').withEscape(','); // escapeChar same as delimiter
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The escape character and the delimiter cannot be the same (',')", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateCommentMarkerEqualsDelimiter() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote('"').withCommentMarker(','); // commentMarker same as delimiter
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start character and the delimiter cannot be the same (',')", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsCommentMarker() throws Exception {
        Character marker = '#';
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote(marker).withCommentMarker(marker);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start character and the quoteChar cannot be the same ('#')", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharEqualsCommentMarker() throws Exception {
        Character marker = '#';
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote('"').withCommentMarker(marker).withEscape(marker);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The comment start and the escape character cannot be the same ('#')", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharWithQuoteModeNone() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote(null).withQuoteMode(CSVFormat.QuoteMode.NONE);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderDuplicates() throws Exception {
        String[] header = new String[] { "a", "b", "a" };
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote('"').withHeader(header);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getCause().getMessage().startsWith("The header contains a duplicate entry: 'a' in "));
    }

    @Test
    @Timeout(8000)
    public void testValidateSuccessMinimal() throws Exception {
        String[] header = new String[] { "col1", "col2" };
        CSVFormat format = CSVFormat.newFormat(',')
                .withQuote('"')
                .withCommentMarker('#')
                .withEscape('\\')
                .withHeader(header);
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateSuccessNoHeader() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',')
                .withQuote('"')
                .withCommentMarker('#')
                .withEscape('\\');
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}