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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_62_5Test {

    @Test
    @Timeout(8000)
    void testWithNullString() throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class,
                boolean.class,
                boolean.class
        );
        constructor.setAccessible(true);

        CSVFormat baseFormat = constructor.newInstance(
                ',',
                Character.valueOf('"'),
                QuoteMode.MINIMAL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                true,
                false,
                "\n",
                "NULL",
                new Object[]{"comment1"},
                new String[]{"header1", "header2"},
                true,
                true,
                true,
                true,
                true
        );

        String newNullString = "newNull";
        CSVFormat newFormat = baseFormat.withNullString(newNullString);

        // Check that a new instance is returned
        assertNotSame(baseFormat, newFormat);

        // Check that nullString is updated
        assertEquals(newNullString, newFormat.getNullString());

        // Check that other fields remain unchanged
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), newFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());

        // Test with null nullString
        CSVFormat nullStringFormat = baseFormat.withNullString(null);
        assertNull(nullStringFormat.getNullString());
    }
}