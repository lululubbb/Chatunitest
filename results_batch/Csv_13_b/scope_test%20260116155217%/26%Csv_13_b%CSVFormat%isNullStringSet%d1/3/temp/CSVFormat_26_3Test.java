package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_26_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_whenNullStringIsNull() throws Exception {
        // Use reflection to create a CSVFormat instance with nullString = null
        CSVFormat formatWithNullNullString = createCSVFormatWithNullString(null);
        assertFalse(formatWithNullNullString.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_whenNullStringIsNotNull() throws Exception {
        // Use reflection to create a CSVFormat instance with nullString = "NULL"
        CSVFormat formatWithNullString = createCSVFormatWithNullString("NULL");
        assertTrue(formatWithNullString.isNullStringSet());
    }

    private CSVFormat createCSVFormatWithNullString(String nullString) throws Exception {
        // CSVFormat constructor parameters:
        // (char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase)

        Class<CSVFormat> clazz = CSVFormat.class;

        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        return constructor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteCharacter(),
                defaultFormat.getQuoteMode(),
                defaultFormat.getCommentMarker(),
                defaultFormat.getEscapeCharacter(),
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                nullString,
                null,
                defaultFormat.getHeader(),
                defaultFormat.getSkipHeaderRecord(),
                defaultFormat.getAllowMissingColumnNames(),
                defaultFormat.getIgnoreHeaderCase());
    }
}