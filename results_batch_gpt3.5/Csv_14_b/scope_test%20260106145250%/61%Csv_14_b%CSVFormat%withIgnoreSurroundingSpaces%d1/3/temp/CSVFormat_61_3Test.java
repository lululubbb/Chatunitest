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
import org.junit.jupiter.api.Test;

public class CSVFormat_61_3Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_true() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withIgnoreSurroundingSpaces(true);

        assertNotSame(original, modified);
        assertTrue(modified.getIgnoreSurroundingSpaces());
        // Original should remain unchanged
        assertFalse(original.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_false() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat modified = original.withIgnoreSurroundingSpaces(false);

        assertNotSame(original, modified);
        assertFalse(modified.getIgnoreSurroundingSpaces());
        // Original should remain unchanged
        assertTrue(original.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_preservesOtherFields() {
        CSVFormat original = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('\'')
                .withIgnoreEmptyLines(false)
                .withNullString("NULL")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(true);

        CSVFormat modified = original.withIgnoreSurroundingSpaces(true);

        assertNotSame(original, modified);
        assertTrue(modified.getIgnoreSurroundingSpaces());

        // Check other fields remain the same
        assertEquals(original.getDelimiter(), modified.getDelimiter());
        assertEquals(original.getQuoteCharacter(), modified.getQuoteCharacter());
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), modified.getNullString());
        assertEquals(original.getSkipHeaderRecord(), modified.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), modified.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), modified.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), modified.getTrim());
        assertEquals(original.getTrailingDelimiter(), modified.getTrailingDelimiter());
    }
}