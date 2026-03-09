package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.TAB;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_10_3Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_Default() {
        // Using predefined DEFAULT format, delimiter should be COMMA
        assertEquals(COMMA, CSVFormat.DEFAULT.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Excel() {
        // Excel inherits from DEFAULT, delimiter should be COMMA
        assertEquals(COMMA, CSVFormat.EXCEL.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_InformixUnload() {
        // Informix unload uses PIPE delimiter
        assertEquals(PIPE, CSVFormat.INFORMIX_UNLOAD.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_InformixUnloadCsv() {
        // Informix unload csv uses COMMA delimiter
        assertEquals(COMMA, CSVFormat.INFORMIX_UNLOAD_CSV.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_MySQL() {
        // MySQL uses TAB delimiter
        assertEquals(TAB, CSVFormat.MYSQL.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_PostgreSQLCsv() {
        // PostgreSQL CSV uses COMMA delimiter
        assertEquals(COMMA, CSVFormat.POSTGRESQL_CSV.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_PostgreSQLText() {
        // PostgreSQL Text uses TAB delimiter
        assertEquals(TAB, CSVFormat.POSTGRESQL_TEXT.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Rfc4180() {
        // RFC4180 inherits from DEFAULT, delimiter should be COMMA
        assertEquals(COMMA, CSVFormat.RFC4180.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Tdf() {
        // TDF uses TAB delimiter
        assertEquals(TAB, CSVFormat.TDF.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_CustomDelimiterUsingReflection() throws Exception {
        // Create a CSVFormat instance with a custom delimiter using reflection to invoke the constructor
        CSVFormat customFormat = createCSVFormatWithDelimiter(';');

        assertEquals(';', customFormat.getDelimiter());
    }

    // Helper method to create CSVFormat instance with custom delimiter via reflection
    private CSVFormat createCSVFormatWithDelimiter(char delimiter) throws Exception {
        // Use the DEFAULT instance as base
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Access private fields via reflection
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        String[] headerComments = (String[]) headerCommentsField.get(defaultFormat);

        // Find the private constructor with all params
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,                            // delimiter
                defaultFormat.getQuoteCharacter(),   // quoteChar
                defaultFormat.getQuoteMode(),        // quoteMode
                defaultFormat.getCommentMarker(),    // commentStart
                defaultFormat.getEscapeCharacter(),  // escape
                defaultFormat.getIgnoreSurroundingSpaces(), // ignoreSurroundingSpaces
                defaultFormat.getIgnoreEmptyLines(), // ignoreEmptyLines
                defaultFormat.getRecordSeparator(),  // recordSeparator
                defaultFormat.getNullString(),       // nullString
                headerComments,                      // headerComments (String[])
                header,                             // header (String[])
                defaultFormat.getSkipHeaderRecord(), // skipHeaderRecord
                defaultFormat.getAllowMissingColumnNames(), // allowMissingColumnNames
                defaultFormat.getIgnoreHeaderCase(), // ignoreHeaderCase
                defaultFormat.getTrim(),              // trim
                defaultFormat.getTrailingDelimiter(),// trailingDelimiter
                defaultFormat.getAutoFlush()          // autoFlush
        );
    }
}