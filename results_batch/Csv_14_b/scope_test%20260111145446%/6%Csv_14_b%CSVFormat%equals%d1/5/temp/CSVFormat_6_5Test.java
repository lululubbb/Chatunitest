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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class CSVFormat_6_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testEquals_SameObject() {
        assertTrue(csvFormat.equals(csvFormat));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullObject() {
        assertFalse(csvFormat.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentClass() {
        assertFalse(csvFormat.equals("Not a CSVFormat object"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EqualObjects() {
        CSVFormat other = CSVFormat.DEFAULT;
        assertTrue(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiter() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',');
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentQuoteMode() {
        CSVFormat other = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentQuoteCharacter() {
        CSVFormat other = CSVFormat.DEFAULT.withQuote('"');
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentCommentMarker() {
        CSVFormat other = CSVFormat.DEFAULT.withCommentMarker('#');
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentEscapeCharacter() {
        CSVFormat other = CSVFormat.DEFAULT.withEscape('\\');
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentNullString() {
        CSVFormat other = CSVFormat.DEFAULT.withNullString("NULL");
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentHeader() {
        CSVFormat other = CSVFormat.DEFAULT.withHeader("Header1", "Header2");
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreSurroundingSpaces() {
        CSVFormat other = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreEmptyLines() {
        CSVFormat other = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentSkipHeaderRecord() {
        CSVFormat other = CSVFormat.DEFAULT.withSkipHeaderRecord();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentRecordSeparator() {
        CSVFormat other = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentAllowMissingColumnNames() {
        CSVFormat other = CSVFormat.DEFAULT.withAllowMissingColumnNames();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreHeaderCase() {
        CSVFormat other = CSVFormat.DEFAULT.withIgnoreHeaderCase();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentTrailingDelimiter() {
        CSVFormat other = CSVFormat.DEFAULT.withTrailingDelimiter();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterQuoteMode() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withQuoteMode(QuoteMode.ALL);
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterQuoteCharacter() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withQuote('"');
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterCommentMarker() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withCommentMarker('#');
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterEscapeCharacter() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withEscape('\\');
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterNullString() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withNullString("NULL");
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterHeader() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withHeader("Header1", "Header2");
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterIgnoreSurroundingSpaces() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withIgnoreSurroundingSpaces();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterIgnoreEmptyLines() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withIgnoreEmptyLines(false);
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterSkipHeaderRecord() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withSkipHeaderRecord();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterRecordSeparator() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withRecordSeparator("\n");
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterAllowMissingColumnNames() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withAllowMissingColumnNames();
        assertFalse(csvFormat.equals(other));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterIgnoreHeaderCase() {
        CSVFormat other = CSVFormat.DEFAULT.withDelimiter(',').withIgnoreHeaderCase();
        assertFalse(csvFormat.equals(other));
    }

}