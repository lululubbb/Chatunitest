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

class CSVFormat_49_5Test {

    @Test
    @Timeout(8000)
    void testWithDelimiter_validDelimiter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newDelimiter = ';';

        CSVFormat result = original.withDelimiter(newDelimiter);

        assertNotNull(result);
        assertEquals(newDelimiter, result.getDelimiter());
        // Other properties should remain unchanged
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
    void testWithDelimiter_lineBreakDelimiterCR() {
        CSVFormat original = CSVFormat.DEFAULT;
        char lineBreak = '\r';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(lineBreak);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiterLF() {
        CSVFormat original = CSVFormat.DEFAULT;
        char lineBreak = '\n';

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(lineBreak);
        });
        assertEquals("The delimiter cannot be a line break", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiterCRLF() throws Exception {
        // CRLF is two chars, but since delimiter is char, test both chars separately
        CSVFormat original = CSVFormat.DEFAULT;

        char cr = '\r';
        char lf = '\n';

        IllegalArgumentException thrownCr = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(cr);
        });
        assertEquals("The delimiter cannot be a line break", thrownCr.getMessage());

        IllegalArgumentException thrownLf = assertThrows(IllegalArgumentException.class, () -> {
            original.withDelimiter(lf);
        });
        assertEquals("The delimiter cannot be a line break", thrownLf.getMessage());
    }
}