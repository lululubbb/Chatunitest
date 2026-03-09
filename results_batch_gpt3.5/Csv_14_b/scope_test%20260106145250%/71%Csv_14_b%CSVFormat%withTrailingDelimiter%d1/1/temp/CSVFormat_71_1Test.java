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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_71_1Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withTrailingDelimiter(true);
        assertNotSame(original, modified);
        assertTrue(modified.getTrailingDelimiter());
        // Original should remain unchanged
        assertFalse(original.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat modified = original.withTrailingDelimiter(false);
        assertNotSame(original, modified);
        assertFalse(modified.getTrailingDelimiter());
        // Original should remain unchanged
        assertTrue(original.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiterIdempotent() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withTrailingDelimiter(false);

        // Use reflection to access the private constructor and create an identical instance
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat modified = constructor.newInstance(
                original.getDelimiter(),
                original.getQuoteCharacter(),
                original.getQuoteMode(),
                original.getCommentMarker(),
                original.getEscapeCharacter(),
                original.getIgnoreSurroundingSpaces(),
                original.getIgnoreEmptyLines(),
                original.getRecordSeparator(),
                original.getNullString(),
                null,
                original.getHeader(),
                original.getSkipHeaderRecord(),
                original.getAllowMissingColumnNames(),
                original.getIgnoreHeaderCase(),
                original.getTrim(),
                original.getTrailingDelimiter() // trailingDelimiter value from original
        );

        // When the value is the same, withTrailingDelimiter returns the same instance
        assertSame(original, original.withTrailingDelimiter(false));
        assertFalse(modified.getTrailingDelimiter());
    }
}