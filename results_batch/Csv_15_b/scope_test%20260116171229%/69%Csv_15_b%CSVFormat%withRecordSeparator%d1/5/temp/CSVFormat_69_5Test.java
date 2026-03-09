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

class CSVFormat_69_5Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorString() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\n";

        CSVFormat result = original.withRecordSeparator(newRecordSeparator);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(newRecordSeparator, result.getRecordSeparator());

        // Check other fields remain the same
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), result.getTrim());
        assertEquals(original.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), result.getAutoFlush());

        // Test with null record separator
        CSVFormat nullSeparatorFormat = original.withRecordSeparator((String) null);
        assertNull(nullSeparatorFormat.getRecordSeparator());
    }
}