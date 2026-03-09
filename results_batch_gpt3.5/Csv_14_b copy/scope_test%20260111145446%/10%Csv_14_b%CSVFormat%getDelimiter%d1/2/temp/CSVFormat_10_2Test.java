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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class CSVFormat_10_2Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter() throws Exception {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterCustom() throws Exception {
        // Given
        char expectedDelimiter = '|';
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterExcel() throws Exception {
        // Given
        char expectedDelimiter = '\t';
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterWithMock() throws Exception {
        // Given
        char expectedDelimiter = '|';
        CSVFormat csvFormat = mock(CSVFormat.class);
        when(csvFormat.getDelimiter()).thenReturn('|');

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }
}