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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;

public class CSVFormat_6_1Test {

    @Test
    @Timeout(8000)
    public void testEquals_SameObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("Not a CSVFormat object"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EqualObjects() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withDelimiter('|');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentQuoteMode() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withQuoteMode(QuoteMode.ALL);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentCommentMarker() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withCommentMarker('#');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentEscapeCharacter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withEscape('\\');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentNullString() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withNullString("NULL");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentHeader() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withHeader("Header1", "Header2");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreSurroundingSpaces() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withIgnoreSurroundingSpaces();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreEmptyLines() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withIgnoreEmptyLines();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentSkipHeaderRecord() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withSkipHeaderRecord();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentRecordSeparator() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withRecordSeparator("\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentAllowMissingColumnNames() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withAllowMissingColumnNames();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreHeaderCase() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withIgnoreHeaderCase();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentTrim() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withTrim();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentTrailingDelimiter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withTrailingDelimiter();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiterAndQuoteMode() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withDelimiter('|').withQuoteMode(QuoteMode.ALL);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentCommentMarkerAndEscapeCharacter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withCommentMarker('#').withEscape('\\');
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentRecordSeparatorAndNullString() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withRecordSeparator("\n").withNullString("NULL");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentHeaderAndIgnoreSurroundingSpaces() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withHeader("Header1", "Header2").withIgnoreSurroundingSpaces();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreEmptyLinesAndSkipHeaderRecord() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withIgnoreEmptyLines().withSkipHeaderRecord();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentRecordSeparatorAndAllowMissingColumnNames() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withRecordSeparator("\n").withAllowMissingColumnNames();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreHeaderCaseAndTrim() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withIgnoreHeaderCase().withTrim();
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentTrailingDelimiterAndDelimiter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;
        format2 = format2.withTrailingDelimiter().withDelimiter('|');
        assertFalse(format1.equals(format2));
    }
}