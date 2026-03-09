package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_37_6Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NewHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] newHeader = {"col1", "col2", "col3"};

        CSVFormat result = baseFormat.withHeader(newHeader);

        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertArrayEquals(newHeader, result.getHeader());

        // Verify other fields remain unchanged
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat result = baseFormat.withHeader((String[]) null);

        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertNull(result.getHeader());

        // Verify other fields remain unchanged
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] emptyHeader = new String[0];

        CSVFormat result = baseFormat.withHeader(emptyHeader);

        assertNotNull(result);
        assertNotSame(baseFormat, result);
        // The getHeader() returns null if header is empty array, so adjust assertion accordingly
        if (result.getHeader() == null) {
            assertNull(result.getHeader());
        } else {
            assertArrayEquals(emptyHeader, result.getHeader());
        }

        // Verify other fields remain unchanged
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }
}