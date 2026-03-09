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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_59_2Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreEmptyLines(true);

        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreEmptyLines());
        // Original format remains unchanged
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreEmptyLines(false);

        assertNotNull(newFormat);
        assertFalse(newFormat.getIgnoreEmptyLines());
        // Original format remains unchanged
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesDoesNotAffectOtherFields() {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(';')
                .withQuote('\'')
                .withAllowMissingColumnNames(true)
                .withIgnoreSurroundingSpaces(true)
                .withNullString("NULL")
                .withRecordSeparator("\n")
                .withHeader("A", "B")
                .withSkipHeaderRecord(true)
                .withIgnoreHeaderCase(true)
                .withTrim(true)
                .withTrailingDelimiter(true)
                .withAutoFlush(true);

        CSVFormat newFormat = format.withIgnoreEmptyLines(false);

        assertNotNull(newFormat);
        assertFalse(newFormat.getIgnoreEmptyLines());
        assertEquals(';', newFormat.getDelimiter());
        assertEquals(Character.valueOf('\''), newFormat.getQuoteCharacter());
        assertTrue(newFormat.getAllowMissingColumnNames());
        assertTrue(newFormat.getIgnoreSurroundingSpaces());
        assertEquals("NULL", newFormat.getNullString());
        assertEquals("\n", newFormat.getRecordSeparator());
        assertArrayEquals(new String[]{"A", "B"}, newFormat.getHeader());
        assertTrue(newFormat.getSkipHeaderRecord());
        assertTrue(newFormat.getIgnoreHeaderCase());
        assertTrue(newFormat.getTrim());
        assertTrue(newFormat.getTrailingDelimiter());
        assertTrue(newFormat.getAutoFlush());

        // Original format remains unchanged
        assertTrue(format.getIgnoreEmptyLines());
    }
}