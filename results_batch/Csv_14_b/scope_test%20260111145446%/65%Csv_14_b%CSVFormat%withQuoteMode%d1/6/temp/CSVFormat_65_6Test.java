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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_65_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteMode() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        QuoteMode quoteMode = QuoteMode.ALL;

        // When
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(quoteMode);

        // Then
        assertEquals(quoteMode, newCsvFormat.getQuoteMode());
        assertEquals(',', newCsvFormat.getDelimiter());
        assertEquals('"', newCsvFormat.getQuoteCharacter());
        assertEquals(null, newCsvFormat.getCommentMarker());
        assertEquals('\\', newCsvFormat.getEscapeCharacter());
        assertEquals(true, newCsvFormat.getIgnoreSurroundingSpaces());
        assertEquals(false, newCsvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", newCsvFormat.getRecordSeparator());
        assertEquals(null, newCsvFormat.getNullString());
        assertEquals("Header1", newCsvFormat.getHeader()[0]);
        assertEquals("Header2", newCsvFormat.getHeader()[1]);
        assertEquals("Comment1", newCsvFormat.getHeaderComments()[0]);
        assertEquals("Comment2", newCsvFormat.getHeaderComments()[1]);
        assertEquals(true, newCsvFormat.getSkipHeaderRecord());
        assertEquals(false, newCsvFormat.getAllowMissingColumnNames());
        assertEquals(false, newCsvFormat.getIgnoreHeaderCase());
        assertEquals(true, newCsvFormat.getTrim());
        assertEquals(false, newCsvFormat.getTrailingDelimiter());
    }
}