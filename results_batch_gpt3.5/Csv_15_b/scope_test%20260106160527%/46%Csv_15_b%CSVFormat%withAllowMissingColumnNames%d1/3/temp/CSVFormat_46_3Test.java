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

class CSVFormat_46_3Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames(true);

        assertNotSame(baseFormat, newFormat, "withAllowMissingColumnNames should return a new CSVFormat instance");
        assertTrue(newFormat.getAllowMissingColumnNames(), "allowMissingColumnNames should be true");
        // Other properties should remain equal
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());

        String[] baseHeaderComments = baseFormat.getHeaderComments();
        String[] newHeaderComments = newFormat.getHeaderComments();
        if (baseHeaderComments == null && newHeaderComments == null) {
            // both null - ok
        } else {
            assertArrayEquals(baseHeaderComments, newHeaderComments);
        }

        String[] baseHeader = baseFormat.getHeader();
        String[] newHeader = newFormat.getHeader();
        if (baseHeader == null && newHeader == null) {
            // both null - ok
        } else {
            assertArrayEquals(baseHeader, newHeader);
        }

        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), newFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(baseFormat.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames(false);

        assertNotSame(baseFormat, newFormat, "withAllowMissingColumnNames should return a new CSVFormat instance");
        assertFalse(newFormat.getAllowMissingColumnNames(), "allowMissingColumnNames should be false");
        // Other properties should remain equal
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());

        String[] baseHeaderComments = baseFormat.getHeaderComments();
        String[] newHeaderComments = newFormat.getHeaderComments();
        if (baseHeaderComments == null && newHeaderComments == null) {
            // both null - ok
        } else {
            assertArrayEquals(baseHeaderComments, newHeaderComments);
        }

        String[] baseHeader = baseFormat.getHeader();
        String[] newHeader = newFormat.getHeader();
        if (baseHeader == null && newHeader == null) {
            // both null - ok
        } else {
            assertArrayEquals(baseHeader, newHeader);
        }

        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), newFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(baseFormat.getAutoFlush(), newFormat.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesOnPredefinedFormat() {
        CSVFormat excelFormat = CSVFormat.EXCEL;
        assertTrue(excelFormat.getAllowMissingColumnNames(), "EXCEL format should have allowMissingColumnNames true");

        CSVFormat defaultFormat = CSVFormat.DEFAULT;
        assertFalse(defaultFormat.getAllowMissingColumnNames(), "DEFAULT format should have allowMissingColumnNames false");
    }
}