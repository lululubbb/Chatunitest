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

class CSVFormat_49_1Test {

    @Test
    @Timeout(8000)
    void testWithNullString() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String nullStr = "NULL";

        CSVFormat newFormat = baseFormat.withNullString(nullStr);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);

        // The newFormat should have the nullString set as nullStr
        assertEquals(nullStr, newFormat.getNullString());

        // Other properties should remain the same as baseFormat
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());

        // Test with null nullString (explicitly setting null)
        CSVFormat nullStringFormat = baseFormat.withNullString(null);
        assertNull(nullStringFormat.getNullString());
    }
}