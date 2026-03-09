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
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_3_3Test {

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertNull(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testEquals() {
        CSVFormat csvFormat1 = CSVFormat.newFormat(',');
        CSVFormat csvFormat2 = CSVFormat.newFormat(',');

        assertTrue(csvFormat1.equals(csvFormat2));
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertEquals(',', csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertFalse(csvFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testGetHeader() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderComments() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertFalse(csvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertFalse(csvFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertNull(csvFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertFalse(csvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertFalse(csvFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetTrim() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        assertFalse(csvFormat.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        csvFormat = csvFormat.withAllowMissingColumnNames();

        assertTrue(csvFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        csvFormat = csvFormat.withIgnoreEmptyLines();

        assertTrue(csvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        csvFormat = csvFormat.withIgnoreHeaderCase();

        assertTrue(csvFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        csvFormat = csvFormat.withIgnoreSurroundingSpaces();

        assertTrue(csvFormat.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        csvFormat = csvFormat.withSkipHeaderRecord();

        assertTrue(csvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        csvFormat = csvFormat.withTrailingDelimiter();

        assertTrue(csvFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithTrim() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        csvFormat = csvFormat.withTrim();

        assertTrue(csvFormat.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        char newDelimiter = ';';
        csvFormat = csvFormat.withDelimiter(newDelimiter);

        assertEquals(newDelimiter, csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        char quoteChar = '"';
        csvFormat = csvFormat.withQuote(quoteChar);

        assertEquals(quoteChar, csvFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        String recordSeparator = "\r\n";
        csvFormat = csvFormat.withRecordSeparator(recordSeparator);

        assertEquals(recordSeparator, csvFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        String nullString = "NULL";
        csvFormat = csvFormat.withNullString(nullString);

        assertEquals(nullString, csvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteMode() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        QuoteMode quoteMode = QuoteMode.ALL;
        csvFormat = csvFormat.withQuoteMode(quoteMode);

        assertEquals(quoteMode, csvFormat.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        char commentMarker = '#';
        csvFormat = csvFormat.withCommentMarker(commentMarker);

        assertEquals(commentMarker, csvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        String[] header = {"Header1", "Header2"};
        csvFormat = csvFormat.withHeader(header);

        assertArrayEquals(header, csvFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        Object[] headerComments = {"Comment1", "Comment2"};
        csvFormat = csvFormat.withHeaderComments(headerComments);

        assertArrayEquals(headerComments, csvFormat.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testValueOf() {
        String formatName = "EXCEL";
        CSVFormat csvFormat = CSVFormat.valueOf(formatName);

        assertEquals(CSVFormat.EXCEL, csvFormat);
    }

    @Test
    @Timeout(8000)
    public void testParse() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        CSVParser csvParser = csvFormat.parse(new StringReader("1,2,3"));

        assertNotNull(csvParser);
    }

    @Test
    @Timeout(8000)
    public void testPrint() {
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        CSVPrinter csvPrinter = csvFormat.print(new StringBuilder());

        assertNotNull(csvPrinter);
    }
}