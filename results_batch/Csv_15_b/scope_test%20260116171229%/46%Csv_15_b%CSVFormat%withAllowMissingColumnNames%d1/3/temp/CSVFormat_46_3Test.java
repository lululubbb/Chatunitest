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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_46_3Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames();

        assertNotSame(baseFormat, newFormat);
        assertTrue(newFormat.getAllowMissingColumnNames());
        // Original remains unchanged
        assertFalse(baseFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames();
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames(false);

        assertNotSame(baseFormat, newFormat);
        assertFalse(newFormat.getAllowMissingColumnNames());
        assertTrue(baseFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesDoesNotChangeOtherProperties() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withDelimiter(';')
                .withQuote('\'')
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\n")
                .withNullString("null")
                .withHeader("a", "b")
                .withSkipHeaderRecord(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreSurroundingSpaces(true)
                .withTrailingDelimiter(true)
                .withAutoFlush(true)
                .withAllowMissingColumnNames(false);

        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames();

        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(baseFormat.getAutoFlush(), newFormat.getAutoFlush());

        assertTrue(newFormat.getAllowMissingColumnNames());
        assertFalse(baseFormat.getAllowMissingColumnNames());
    }
}