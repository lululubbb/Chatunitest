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

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CSVFormat_50_5Test {

    @Test
    @Timeout(8000)
    public void testWithFirstRecordAsHeader() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withDelimiter('\0')
                .withEscape((Character) null).withHeader((String[]) null).withIgnoreEmptyLines(false).withSkipHeaderRecord(true)
                .withRecordSeparator("\r\n").withHeaderComments((Object[]) null).withAllowMissingColumnNames((Boolean) null)
                .withIgnoreHeaderCase((Boolean) null).withTrim(false).withTrailingDelimiter(false);

        // When
        CSVFormat result = csvFormat.withFirstRecordAsHeader();

        // Then
        assertTrue(result.getSkipHeaderRecord());
        assertArrayEquals(csvFormat.getHeader(), result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithFirstRecordAsHeader2() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withDelimiter('\0')
                .withEscape((Character) null).withHeader((String[]) null).withIgnoreEmptyLines(false).withSkipHeaderRecord(true)
                .withRecordSeparator("\r\n").withHeaderComments((Object[]) null).withAllowMissingColumnNames((Boolean) null)
                .withIgnoreHeaderCase((Boolean) null).withTrim(false).withTrailingDelimiter(false);

        // When
        CSVFormat result = csvFormat.withFirstRecordAsHeader();

        // Then
        assertTrue(result.getSkipHeaderRecord());
        assertArrayEquals(csvFormat.getHeader(), result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withDelimiter('\0')
                .withEscape((Character) null).withHeader((String[]) null).withIgnoreEmptyLines(false).withSkipHeaderRecord(true)
                .withRecordSeparator("\r\n").withHeaderComments((Object[]) null).withAllowMissingColumnNames((Boolean) null)
                .withIgnoreHeaderCase((Boolean) null).withTrim(false).withTrailingDelimiter(false);

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    // Add more test cases for other methods if needed
}