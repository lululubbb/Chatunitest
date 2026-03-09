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

public class CSVFormat_14_3Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        
        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(true, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesAfterSetting() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        
        // When
        csvFormat = csvFormat.withIgnoreEmptyLines(false);
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(false, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesAfterSettingTwice() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        
        // When
        csvFormat = csvFormat.withIgnoreEmptyLines(false);
        csvFormat = csvFormat.withIgnoreEmptyLines(true);
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(true, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesWithExcelFormat() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;
        
        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(false, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesWithInformixUnloadFormat() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;
        
        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(true, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesWithInformixUnloadCSVFormat() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;
        
        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(true, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesWithMySQLFormat() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;
        
        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(false, ignoreEmptyLines);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLinesWithRFC4180Format() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;
        
        // When
        boolean ignoreEmptyLines = csvFormat.getIgnoreEmptyLines();
        
        // Then
        assertEquals(false, ignoreEmptyLines);
    }
}