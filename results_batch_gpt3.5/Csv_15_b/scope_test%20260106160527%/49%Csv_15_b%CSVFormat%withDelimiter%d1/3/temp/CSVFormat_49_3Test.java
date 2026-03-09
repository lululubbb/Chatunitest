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

public class CSVFormat_49_3Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());
        // other properties remain unchanged
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
        assertEquals(original.getTrim(), result.getTrim());
        assertEquals(original.getTrailingDelimiter(), result.getTrailingDelimiter());
        assertEquals(original.getAutoFlush(), result.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiterCR_throwsIllegalArgumentException() {
        CSVFormat original = CSVFormat.DEFAULT;
        char delimiter = '\r';

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(delimiter);
        });
        assertEquals("The delimiter cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiterLF_throwsIllegalArgumentException() {
        CSVFormat original = CSVFormat.DEFAULT;
        char delimiter = '\n';

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(delimiter);
        });
        assertEquals("The delimiter cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiterCRLF_throwsIllegalArgumentException() {
        // CRLF is a String, but delimiter is char, so test only char delimiters
        // No char delimiter can be CRLF, so no test needed here.
        // This test method is just a placeholder for completeness.
    }

}