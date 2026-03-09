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
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class CSVFormatValidateTest {

    private void invokeValidate(CSVFormat format) throws Exception {
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);
        validate.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidateValidDefault() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() throws Exception {
        CSVFormat format = new CSVFormat('\n', null, QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, null,
                false, false, false, false, false, false);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertEquals("The delimiter cannot be a line break", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsQuoteCharacter() throws Exception {
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, null,
                false, false, false, false, false, false)
                .withQuote(',');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsEscapeCharacter() throws Exception {
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.MINIMAL, null, '\\',
                false, false, "\n", null, null, null,
                false, false, false, false, false, false)
                .withEscape(',');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsCommentMarker() throws Exception {
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.MINIMAL, '#', null,
                false, false, "\n", null, null, null,
                false, false, false, false, false, false)
                .withCommentMarker(',');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharacterEqualsCommentMarker() throws Exception {
        CSVFormat format = new CSVFormat(',', '#', QuoteMode.MINIMAL, '#', null,
                false, false, "\n", null, null, null,
                false, false, false, false, false, false);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharacterEqualsCommentMarker() throws Exception {
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.MINIMAL, '#', '#',
                false, false, "\n", null, null, null,
                false, false, false, false, false, false);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharacterNullQuoteModeNone() throws Exception {
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.NONE, null, null,
                false, false, "\n", null, null, null,
                false, false, false, false, false, false);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertEquals("No quotes mode set but no escape character is set", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderDuplicate() throws Exception {
        String[] header = new String[] {"a", "b", "a"};
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, header,
                false, false, false, false, false, false);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
        assertTrue(ex.getCause().getMessage().contains("The header contains a duplicate entry"));
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderUnique() throws Exception {
        String[] header = new String[] {"a", "b", "c"};
        CSVFormat format = new CSVFormat(',', '"', QuoteMode.MINIMAL, null, null,
                false, false, "\n", null, null, header,
                false, false, false, false, false, false);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}