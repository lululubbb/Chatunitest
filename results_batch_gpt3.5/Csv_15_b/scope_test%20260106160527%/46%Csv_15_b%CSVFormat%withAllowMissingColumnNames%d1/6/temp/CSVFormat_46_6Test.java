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

import java.lang.reflect.Constructor;

class CSVFormat_46_6Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat modified = base.withAllowMissingColumnNames(true);

        assertNotNull(modified);
        assertTrue(modified.getAllowMissingColumnNames());
        assertEquals(base.getDelimiter(), modified.getDelimiter());
        assertEquals(base.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), modified.getQuoteMode());
        assertEquals(base.getCommentMarker(), modified.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(base.getNullString(), modified.getNullString());
        assertArrayEquals(base.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(base.getHeader(), modified.getHeader());
        assertEquals(base.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(base.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), modified.getTrim());
        assertEquals(base.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), modified.getAutoFlush());
        assertNotSame(base, modified);
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat base = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);

        // Use reflection to call the private constructor to create a CSVFormat with allowMissingColumnNames = false
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat modified = constructor.newInstance(
                base.getDelimiter(),
                base.getQuoteCharacter(),
                base.getQuoteMode(),
                base.getCommentMarker(),
                base.getEscapeCharacter(),
                base.getIgnoreSurroundingSpaces(),
                base.getIgnoreEmptyLines(),
                base.getRecordSeparator(),
                base.getNullString(),
                base.getHeaderComments(),
                base.getHeader(),
                base.getSkipHeaderRecord(),
                false, // allowMissingColumnNames = false
                base.getIgnoreHeaderCase(),
                base.getTrim(),
                base.getTrailingDelimiter(),
                base.getAutoFlush());

        assertNotNull(modified);
        assertFalse(modified.getAllowMissingColumnNames());
        assertEquals(base.getDelimiter(), modified.getDelimiter());
        assertEquals(base.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), modified.getQuoteMode());
        assertEquals(base.getCommentMarker(), modified.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(base.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(base.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), modified.getRecordSeparator());
        assertEquals(base.getNullString(), modified.getNullString());
        assertArrayEquals(base.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(base.getHeader(), modified.getHeader());
        assertEquals(base.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(base.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), modified.getTrim());
        assertEquals(base.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), modified.getAutoFlush());
        assertNotSame(base, modified);
    }
}