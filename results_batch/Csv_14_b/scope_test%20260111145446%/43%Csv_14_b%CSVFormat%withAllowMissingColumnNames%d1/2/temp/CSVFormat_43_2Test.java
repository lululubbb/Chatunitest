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

public class CSVFormat_43_2Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_Static() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_Excel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_InformixUnload() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_InformixUnloadCSV() {
        // Given
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_MySQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_RFC4180() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames();

        // Then
        assertTrue(result.getAllowMissingColumnNames());
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteChar, QuoteMode quoteMode, Character commentStart,
                                      Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, Object[] headerComments,
                                      String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames,
                                      boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                    QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                    String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class,
                    boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                    ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                    allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}