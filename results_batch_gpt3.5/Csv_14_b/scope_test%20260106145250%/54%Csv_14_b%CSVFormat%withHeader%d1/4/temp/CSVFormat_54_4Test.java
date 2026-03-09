package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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

class CSVFormat_54_4Test {

    @Test
    @Timeout(8000)
    void testWithHeader_nullAndEmpty() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        // withHeader with null
        CSVFormat formatWithNullHeader = baseFormat.withHeader((String[]) null);
        assertNotNull(formatWithNullHeader);
        assertNull(formatWithNullHeader.getHeader());

        // withHeader with empty array
        CSVFormat formatWithEmptyHeader = baseFormat.withHeader(new String[0]);
        assertNotNull(formatWithEmptyHeader);
        assertArrayEquals(new String[0], formatWithEmptyHeader.getHeader());

        // withHeader with non-empty array
        String[] headers = new String[] { "col1", "col2" };
        CSVFormat formatWithHeaders = baseFormat.withHeader(headers);
        assertNotNull(formatWithHeaders);
        assertArrayEquals(headers, formatWithHeaders.getHeader());

        // Ensure original is unchanged
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_immutabilityAndProperties() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'').withIgnoreEmptyLines(false)
                .withRecordSeparator("\n").withNullString("NULL").withSkipHeaderRecord(true).withTrim(true)
                .withTrailingDelimiter(true).withAllowMissingColumnNames(true).withIgnoreHeaderCase(true)
                .withIgnoreSurroundingSpaces(true);

        String[] headers = new String[] { "h1", "h2" };
        CSVFormat newFormat = baseFormat.withHeader(headers);

        // Verify header set correctly
        assertArrayEquals(headers, newFormat.getHeader());

        // Verify all other properties copied correctly
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getTrim(), newFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());

        // Original format header remains null
        assertNull(baseFormat.getHeader());
    }

}