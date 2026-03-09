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
import static org.mockito.Mockito.mock;

public class CSVFormat_57_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        boolean ignoreEmptyLines = true;
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withIgnoreEmptyLines();

        // When
        CSVFormat result = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_Default() {
        // Given
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat result = CSVFormat.DEFAULT.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_Excel() {
        // Given
        boolean ignoreEmptyLines = false;

        // When
        CSVFormat result = CSVFormat.EXCEL.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
    }

    // Add more test cases as needed for other predefined CSVFormats

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_Mock() {
        // Given
        boolean ignoreEmptyLines = true;
        CSVFormat csvFormat = mock(CSVFormat.class);

        // When
        CSVFormat result = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, result.getIgnoreEmptyLines());
    }
}