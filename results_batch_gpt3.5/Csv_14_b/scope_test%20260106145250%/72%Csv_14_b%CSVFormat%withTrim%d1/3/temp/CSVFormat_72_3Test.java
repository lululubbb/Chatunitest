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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatWithTrimTest {

    @Test
    @Timeout(8000)
    void testWithTrimReturnsNewInstanceWithTrimTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getTrim(), "Original format should have trim=false");

        CSVFormat trimmed = original.withTrim();
        assertNotSame(original, trimmed, "withTrim() should return a new CSVFormat instance");
        assertTrue(trimmed.getTrim(), "Trim should be true in the new CSVFormat instance");
    }

    @Test
    @Timeout(8000)
    void testWithTrimDoesNotAffectOriginalInstance() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat trimmed = original.withTrim();

        // Original instance remains unchanged
        assertFalse(original.getTrim(), "Original instance trim should remain false");
        // New instance has trim = true
        assertTrue(trimmed.getTrim(), "New instance trim should be true");
    }

    @Test
    @Timeout(8000)
    void testWithTrimChainDoesNotModifyOtherProperties() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat trimmed = original.withTrim();

        // Verify other properties remain equal
        assertEquals(original.getDelimiter(), trimmed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), trimmed.getQuoteCharacter());
        assertEquals(original.getCommentMarker(), trimmed.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), trimmed.getEscapeCharacter());
        assertEquals(original.getHeader() == null, trimmed.getHeader() == null);
        assertEquals(original.getIgnoreEmptyLines(), trimmed.getIgnoreEmptyLines());
        assertEquals(original.getIgnoreHeaderCase(), trimmed.getIgnoreHeaderCase());
        assertEquals(original.getIgnoreSurroundingSpaces(), trimmed.getIgnoreSurroundingSpaces());
        assertEquals(original.getNullString(), trimmed.getNullString());
        assertEquals(original.getQuoteMode(), trimmed.getQuoteMode());
        assertEquals(original.getRecordSeparator(), trimmed.getRecordSeparator());
        assertEquals(original.getSkipHeaderRecord(), trimmed.getSkipHeaderRecord());
        assertEquals(original.getTrailingDelimiter(), trimmed.getTrailingDelimiter());
        assertEquals(original.getAllowMissingColumnNames(), trimmed.getAllowMissingColumnNames());
    }
}