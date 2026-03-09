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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_31_6Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NewHeaderSet() throws Exception {
        // Arrange
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Act
        CSVFormat newFormat = baseFormat.withHeader("col1", "col2", "col3");

        // Assert
        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, newFormat.getHeader());

        // Original format header should remain null
        assertNull(baseFormat.getHeader());

        // Other fields remain unchanged
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(baseFormat.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(baseFormat.getCommentStart(), newFormat.getCommentStart());
        assertEquals(baseFormat.getEscape(), newFormat.getEscape());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() throws Exception {
        // Arrange
        CSVFormat baseFormat = CSVFormat.DEFAULT.withHeader("a", "b");

        // Act
        CSVFormat newFormat = baseFormat.withHeader((String[]) null);

        // Assert
        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertNull(newFormat.getHeader());

        // Previous header remains unchanged in baseFormat
        assertArrayEquals(new String[]{"a", "b"}, baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() throws Exception {
        // Arrange
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Act
        CSVFormat newFormat = baseFormat.withHeader(new String[0]);

        // Assert
        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertArrayEquals(new String[0], newFormat.getHeader());

        // Original format header should remain null
        assertNull(baseFormat.getHeader());
    }
}