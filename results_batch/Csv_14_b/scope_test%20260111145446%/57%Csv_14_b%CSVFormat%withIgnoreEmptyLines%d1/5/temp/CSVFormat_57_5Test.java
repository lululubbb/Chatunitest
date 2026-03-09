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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CSVFormat_57_5Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        boolean ignoreEmptyLines = true;
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals('"', newCsvFormat.getQuoteCharacter());
        assertEquals(null, newCsvFormat.getQuoteMode());
        assertEquals(null, newCsvFormat.getCommentMarker());
        assertEquals(null, newCsvFormat.getEscapeCharacter());
        assertEquals(false, newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", newCsvFormat.getRecordSeparator());
        assertEquals(null, newCsvFormat.getNullString());
        assertEquals(null, newCsvFormat.getHeaderComments());
        assertEquals(null, newCsvFormat.getHeader());
        assertEquals(false, newCsvFormat.getSkipHeaderRecord());
        assertEquals(true, newCsvFormat.getAllowMissingColumnNames());
        assertEquals(false, newCsvFormat.getIgnoreHeaderCase());
        assertEquals(false, newCsvFormat.getTrim());
        assertEquals(false, newCsvFormat.getTrailingDelimiter());
    }
}