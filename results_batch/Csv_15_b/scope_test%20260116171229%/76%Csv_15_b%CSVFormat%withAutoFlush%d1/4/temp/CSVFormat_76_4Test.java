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
import org.junit.jupiter.api.Test;

class CSVFormat_76_4Test {

    @Test
    @Timeout(8000)
    void testWithAutoFlushTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withAutoFlush(true);
        assertNotNull(updated);
        assertTrue(updated.getAutoFlush());
        // Original should remain unchanged
        assertFalse(original.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithAutoFlushFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withAutoFlush(true);
        CSVFormat updated = original.withAutoFlush(false);
        assertNotNull(updated);
        assertFalse(updated.getAutoFlush());
        // Original should remain unchanged
        assertTrue(original.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithAutoFlushDoesNotAffectOtherProperties() {
        CSVFormat original = CSVFormat.DEFAULT.withDelimiter(';')
            .withQuote('\'')
            .withIgnoreEmptyLines(false)
            .withTrim(true)
            .withTrailingDelimiter(true)
            .withNullString("NULL")
            .withCommentMarker('#')
            .withEscape('\\')
            .withQuoteMode(QuoteMode.ALL);
        CSVFormat updated = original.withAutoFlush(true);

        assertEquals(';', updated.getDelimiter());
        assertEquals(Character.valueOf('\''), updated.getQuoteCharacter());
        assertFalse(updated.getIgnoreEmptyLines());
        assertTrue(updated.getTrim());
        assertTrue(updated.getTrailingDelimiter());
        assertEquals("NULL", updated.getNullString());
        assertEquals(Character.valueOf('#'), updated.getCommentMarker());
        assertEquals(Character.valueOf('\\'), updated.getEscapeCharacter());
        assertEquals(QuoteMode.ALL, updated.getQuoteMode());
        assertTrue(updated.getAutoFlush());

        // Original remains unchanged
        assertFalse(original.getAutoFlush());
    }
}