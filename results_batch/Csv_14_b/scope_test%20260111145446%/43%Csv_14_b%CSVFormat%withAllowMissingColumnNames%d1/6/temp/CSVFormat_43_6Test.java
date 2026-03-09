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

import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_43_6Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames();

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames(false);

        // Then
        assertFalse(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesExcel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesInformixUnload() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesInformixUnloadCSV() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesMySQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNamesRFC4180() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }
}