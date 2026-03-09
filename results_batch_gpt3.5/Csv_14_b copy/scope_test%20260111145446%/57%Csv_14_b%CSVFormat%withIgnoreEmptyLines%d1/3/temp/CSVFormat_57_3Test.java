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
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_57_3Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean ignoreEmptyLines = false;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesExcel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesInformixUnload() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesInformixUnloadCsv() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesMysql() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }
}