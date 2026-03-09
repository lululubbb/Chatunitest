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

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_56_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedCsvFormat = csvFormat.withIgnoreEmptyLines();

        // Then
        assertTrue(updatedCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);

        // When
        CSVFormat updatedCsvFormat = csvFormat.withIgnoreEmptyLines();

        // Then
        assertFalse(updatedCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesTwice() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedCsvFormat = csvFormat.withIgnoreEmptyLines().withIgnoreEmptyLines();

        // Then
        assertTrue(updatedCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesExcel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        CSVFormat updatedCsvFormat = csvFormat.withIgnoreEmptyLines();

        // Then
        assertFalse(updatedCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesInformixUnload() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;

        // When
        CSVFormat updatedCsvFormat = csvFormat.withIgnoreEmptyLines();

        // Then
        assertTrue(updatedCsvFormat.getIgnoreEmptyLines());
    }
}