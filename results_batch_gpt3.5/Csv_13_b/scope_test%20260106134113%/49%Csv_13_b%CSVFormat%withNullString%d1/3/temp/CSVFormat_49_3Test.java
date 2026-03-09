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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_49_3Test {

    @Test
    @Timeout(8000)
    void testWithNullString_NewInstanceHasSamePropertiesExceptNullString() {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('\'')
                .withQuoteMode(QuoteMode.ALL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withHeader("a", "b")
                .withHeaderComments("header comment")
                .withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true)
                .withIgnoreHeaderCase(true);

        String newNullString = "NULL";

        // Act
        CSVFormat updated = original.withNullString(newNullString);

        // Assert
        assertNotSame(original, updated, "withNullString should return a new instance");
        assertEquals(newNullString, updated.getNullString(), "NullString should be updated");
        assertEquals(original.getDelimiter(), updated.getDelimiter(), "Delimiter should be same");
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter(), "QuoteCharacter should be same");
        assertEquals(original.getQuoteMode(), updated.getQuoteMode(), "QuoteMode should be same");
        assertEquals(original.getCommentMarker(), updated.getCommentMarker(), "CommentMarker should be same");
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter(), "EscapeCharacter should be same");
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces(), "IgnoreSurroundingSpaces should be same");
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines(), "IgnoreEmptyLines should be same");
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator(), "RecordSeparator should be same");
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments(), "HeaderComments should be same");
        assertArrayEquals(original.getHeader(), updated.getHeader(), "Header should be same");
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord(), "SkipHeaderRecord should be same");
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames(), "AllowMissingColumnNames should be same");
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase(), "IgnoreHeaderCase should be same");
    }

    @Test
    @Timeout(8000)
    void testWithNullString_NullValue() {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT.withNullString("oldNull");

        // Act
        CSVFormat updated = original.withNullString(null);

        // Assert
        assertNotSame(original, updated);
        assertNull(updated.getNullString(), "NullString should be null");
        // Other properties remain unchanged
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), updated.getQuoteMode());
        assertEquals(original.getCommentMarker(), updated.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), updated.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), updated.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
        assertArrayEquals(original.getHeaderComments(), updated.getHeaderComments());
        assertArrayEquals(original.getHeader(), updated.getHeader());
        assertEquals(original.getSkipHeaderRecord(), updated.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), updated.getIgnoreHeaderCase());
    }
}