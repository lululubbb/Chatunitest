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

class CSVFormat_49_6Test {

    @Test
    @Timeout(8000)
    void testWithNullString() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String nullStr1 = null;
        CSVFormat result1 = baseFormat.withNullString(nullStr1);
        assertNotNull(result1);
        assertEquals(nullStr1, result1.getNullString());
        // It should create a new instance different from the original
        assertNotSame(baseFormat, result1);
        // Other properties should be the same
        assertEquals(baseFormat.getDelimiter(), result1.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result1.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), result1.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result1.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result1.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result1.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result1.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result1.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeaderComments(), result1.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), result1.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), result1.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result1.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result1.getIgnoreHeaderCase());

        // Test with a non-null string
        String nullStr2 = "NULL";
        CSVFormat result2 = baseFormat.withNullString(nullStr2);
        assertNotNull(result2);
        assertEquals(nullStr2, result2.getNullString());
        assertNotSame(baseFormat, result2);

        // Also verify that changing nullString does not affect original
        assertNull(baseFormat.getNullString());
    }
}