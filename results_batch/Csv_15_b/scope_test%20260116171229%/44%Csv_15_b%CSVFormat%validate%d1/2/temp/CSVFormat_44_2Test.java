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

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;
    private Method validateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        // Using DEFAULT as base, will modify fields via reflection if needed
        csvFormat = CSVFormat.DEFAULT;

        // Access private validate() method
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        validateMethod.invoke(format);
    }

    @Test
    @Timeout(8000)
    public void testValidate_validDefaults() throws Exception {
        // DEFAULT is valid, should not throw
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }

    @Test
    @Timeout(8000)
    public void testValidate_delimiterIsLineBreak_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat('\n');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        // message check
        assert(cause.getMessage().contains("delimiter cannot be a line break"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote(',');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        assert(cause.getMessage().contains("quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withEscape(',');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        assert(cause.getMessage().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_commentMarkerEqualsDelimiter_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withCommentMarker(',');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        assert(cause.getMessage().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_quoteCharEqualsCommentMarker_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withQuote('\'').withCommentMarker('\'');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        assert(cause.getMessage().contains("comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharEqualsCommentMarker_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withEscape('!').withCommentMarker('!');
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        assert(cause.getMessage().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_escapeCharNullAndQuoteModeNone_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withEscape((Character) null).withQuoteMode(QuoteMode.NONE);
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        assert(cause.getMessage().contains("No quotes mode set but no escape character is set"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerWithDuplicates_throws() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withHeader("one", "two", "one");
        Exception ex = assertThrows(Exception.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assertThrows(IllegalArgumentException.class, () -> { throw cause; });
        assert(cause.getMessage().contains("header contains a duplicate entry"));
        assert(cause.getMessage().contains("one"));
        assert(cause.getMessage().contains(Arrays.toString(new String[]{"one", "two", "one"})));
    }

    @Test
    @Timeout(8000)
    public void testValidate_headerUnique_noThrow() throws Exception {
        CSVFormat format = CSVFormat.newFormat(',');
        format = format.withHeader("a", "b", "c");
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}