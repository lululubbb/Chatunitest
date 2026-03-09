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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_13_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.newFormat(',').withQuote('"').withCommentMarker('#').withEscape('\\').withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false).withRecordSeparator("\r\n").withNullString("\\N").withHeaderComments(new Object[]{"Header"}).withHeader(new String[]{"Comment"}).withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true).withIgnoreHeaderCase(false).withTrim(true).withTrailingDelimiter(false);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsNotNull() {
        String[] expected = {"Comment"};
        String[] actual = csvFormat.getHeaderComments();
        assertArrayEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsNull() {
        CSVFormat csvFormatNullComments = CSVFormat.newFormat(',').withQuote('"').withCommentMarker('#').withEscape('\\').withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false).withRecordSeparator("\r\n").withNullString("\\N").withHeaderComments(new Object[]{"Header"}).withHeader((String[]) null).withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true).withIgnoreHeaderCase(false).withTrim(true).withTrailingDelimiter(false);
        String[] expected = null;
        String[] actual = csvFormatNullComments.getHeaderComments();
        assertArrayEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsEmpty() {
        CSVFormat csvFormatEmptyComments = CSVFormat.newFormat(',').withQuote('"').withCommentMarker('#').withEscape('\\').withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false).withRecordSeparator("\r\n").withNullString("\\N").withHeaderComments(new Object[]{"Header"}).withHeader(new String[0]).withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true).withIgnoreHeaderCase(false).withTrim(true).withTrailingDelimiter(false);
        String[] expected = new String[0];
        String[] actual = csvFormatEmptyComments.getHeaderComments();
        assertArrayEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsEmptyComments() {
        CSVFormat csvFormatEmptyComments = CSVFormat.newFormat(',').withQuote('"').withCommentMarker('#').withEscape('\\').withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false).withRecordSeparator("\r\n").withNullString("\\N").withHeaderComments((Object[]) null).withHeader(new String[]{"Comment"}).withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true).withIgnoreHeaderCase(false).withTrim(true).withTrailingDelimiter(false);
        String[] expected = null;
        String[] actual = csvFormatEmptyComments.getHeaderComments();
        assertArrayEquals(expected, actual);
    }
}