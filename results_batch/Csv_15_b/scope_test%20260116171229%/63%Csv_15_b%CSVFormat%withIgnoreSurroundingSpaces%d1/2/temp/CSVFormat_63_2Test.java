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

public class CSVFormat_63_2Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withIgnoreSurroundingSpaces(true);

        assertNotNull(result);
        assertTrue(result.getIgnoreSurroundingSpaces());
        // Other properties should remain unchanged
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeaderComments() == null ? new Object[0] : base.getHeaderComments(),
                          result.getHeaderComments() == null ? new Object[0] : result.getHeaderComments());
        assertArrayEquals(base.getHeader() == null ? new String[0] : base.getHeader(),
                          result.getHeader() == null ? new String[0] : result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesFalse() {
        CSVFormat base = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(base.getIgnoreSurroundingSpaces());

        CSVFormat result = base.withIgnoreSurroundingSpaces(false);

        assertNotNull(result);
        assertFalse(result.getIgnoreSurroundingSpaces());
        // Other properties should remain unchanged
        assertEquals(base.getDelimiter(), result.getDelimiter());
        assertEquals(base.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(base.getQuoteMode(), result.getQuoteMode());
        assertEquals(base.getCommentMarker(), result.getCommentMarker());
        assertEquals(base.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(base.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(base.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(base.getNullString(), result.getNullString());
        assertArrayEquals(base.getHeaderComments() == null ? new Object[0] : base.getHeaderComments(),
                          result.getHeaderComments() == null ? new Object[0] : result.getHeaderComments());
        assertArrayEquals(base.getHeader() == null ? new String[0] : base.getHeader(),
                          result.getHeader() == null ? new String[0] : result.getHeader());
        assertEquals(base.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(base.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(base.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(base.getTrim(), result.getTrim());
        assertEquals(base.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(base.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpacesOnPredefinedConstant() {
        CSVFormat format = CSVFormat.TDF;
        // TDF has ignoreSurroundingSpaces enabled by default
        assertTrue(format.getIgnoreSurroundingSpaces());

        CSVFormat changed = format.withIgnoreSurroundingSpaces(false);
        assertFalse(changed.getIgnoreSurroundingSpaces());
        // Ensure original constant unchanged
        assertTrue(format.getIgnoreSurroundingSpaces());
    }
}