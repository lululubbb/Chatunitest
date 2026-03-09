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

class CSVFormat_69_3Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NewInstanceWithGivenSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\n";

        CSVFormat modified = original.withRecordSeparator(newSeparator);

        assertNotSame(original, modified, "withRecordSeparator should return a new instance");
        assertEquals(newSeparator, modified.getRecordSeparator(), "Record separator should be updated");
        // All other properties should remain equal
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), modified.getQuoteMode());
        assertEquals(original.getCommentMarker(), modified.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), modified.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), modified.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), modified.getNullString());
        assertArrayEquals(original.getHeaderComments(), modified.getHeaderComments());
        assertArrayEquals(original.getHeader(), modified.getHeader());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), modified.getTrim());
        assertEquals(original.getTrailingDelimiter(), modified.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), modified.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_NullSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat modified = original.withRecordSeparator((String) null);

        assertNotSame(original, modified, "withRecordSeparator should return a new instance even if null");
        assertNull(modified.getRecordSeparator(), "Record separator should be null when set to null");
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_EmptyStringSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "";

        CSVFormat modified = original.withRecordSeparator(newSeparator);

        assertNotSame(original, modified, "withRecordSeparator should return a new instance");
        assertEquals(newSeparator, modified.getRecordSeparator(), "Record separator should be updated to empty string");
    }
}