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

class CSVFormat_33_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_validChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character commentMarker = '#';
        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertEquals(commentMarker, newFormat.getCommentMarker());
        // Other properties should remain unchanged
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertArrayEquals(baseFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_nullCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character commentMarker = null;
        CSVFormat newFormat = baseFormat.withCommentMarker(commentMarker);

        assertNotNull(newFormat);
        assertNull(newFormat.getCommentMarker());
        // Other properties remain unchanged
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_lineBreakCharThrows() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        Character[] lineBreaks = { '\n', '\r' };

        for (Character lb : lineBreaks) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                baseFormat.withCommentMarker(lb);
            });
            assertEquals("The comment start marker character cannot be a line break", ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_withCharOverload() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentMarker = '#';

        // Invoke public withCommentMarker(char) method using reflection
        java.lang.reflect.Method method = CSVFormat.class.getMethod("withCommentMarker", char.class);
        CSVFormat newFormat = (CSVFormat) method.invoke(baseFormat, commentMarker);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentMarker), newFormat.getCommentMarker());
    }
}