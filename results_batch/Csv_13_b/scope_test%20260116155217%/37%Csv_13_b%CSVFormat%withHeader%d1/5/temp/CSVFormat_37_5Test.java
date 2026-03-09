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
import java.lang.reflect.Method;

class CSVFormat_37_5Test {

    @Test
    @Timeout(8000)
    void testWithHeader() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;
        String[] newHeader = new String[] {"col1", "col2", "col3"};

        // Act
        CSVFormat result = original.withHeader(newHeader);

        // Assert
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(newHeader, result.getHeader());

        // Ensure other fields remain the same
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderNull() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader((String[]) null);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderEmpty() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader(new String[0]);
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[0], result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderUsingReflection() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] header = new String[]{"a", "b"};

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", String[].class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(original, (Object) header);

        assertNotNull(result);
        assertArrayEquals(header, result.getHeader());
    }
}