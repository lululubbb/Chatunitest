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
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;

class CSVFormat_34_2Test {

    @Test
    @Timeout(8000)
    void testWithDelimiter_validDelimiter_shouldCreateNewInstanceWithDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());
        // Other properties should remain the same
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertArrayEquals(original.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(original.getHeader(), result.getHeader());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());

        // Original instance should not be modified
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiter_CR_shouldThrowIllegalArgumentException() {
        CSVFormat format = CSVFormat.DEFAULT;
        char delimiter = '\r';

        Executable executable = () -> format.withDelimiter(delimiter);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiter_LF_shouldThrowIllegalArgumentException() {
        CSVFormat format = CSVFormat.DEFAULT;
        char delimiter = '\n';

        Executable executable = () -> format.withDelimiter(delimiter);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiter_CRLF_shouldThrowIllegalArgumentException() throws Exception {
        // CRLF is a String, but withDelimiter accepts char, so test '\r' and '\n' separately above.
        // This test ensures private static isLineBreak(char) returns true for CR and LF.
        // Use reflection to test private static isLineBreak(char)

        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.TYPE);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));
        assertFalse((Boolean) method.invoke(null, ';'));
    }

}