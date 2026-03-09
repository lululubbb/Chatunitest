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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_22_2Test {

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '"', QuoteMode.ALL, '#', '\\', true, false, "\r\n",
                "\\N", new Object[]{"Header1", "Header2"}, new String[]{"Comment1", "Comment2"},
                true, true, false, true, true);

        // When
        boolean result = csvFormat.getTrailingDelimiter();

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_Default() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean result = csvFormat.getTrailingDelimiter();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_Excel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        boolean result = csvFormat.getTrailingDelimiter();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_InformixUnload() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;

        // When
        boolean result = csvFormat.getTrailingDelimiter();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_InformixUnloadCSV() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;

        // When
        boolean result = csvFormat.getTrailingDelimiter();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_MySQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        boolean result = csvFormat.getTrailingDelimiter();

        // Then
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_RFC4180() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        boolean result = csvFormat.getTrailingDelimiter();

        // Then
        assertTrue(result);
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteChar, QuoteMode quoteMode, char commentStart,
                                      char escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, Object[] headerComments, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames, boolean ignoreHeaderCase,
                                      boolean trim, boolean trailingDelimiter) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    char.class, char.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                    String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                    ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                    ignoreHeaderCase, trim, trailingDelimiter);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}