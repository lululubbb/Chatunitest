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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_18_3Test {

    @Test
    @Timeout(8000)
    public void testGetNullString_DefaultNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        String nullString = format.getNullString();
        assertNull(nullString);
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_CustomNullString() throws Exception {
        // Find the private constructor and use it
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
                boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat customFormat = constructor.newInstance(
                format.getDelimiter(),
                format.getQuoteCharacter(),
                format.getQuoteMode(),
                format.getCommentMarker(),
                format.getEscapeCharacter(),
                format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(),
                format.getRecordSeparator(),
                "NULL",
                null,
                format.getHeader(),
                format.getSkipHeaderRecord(),
                format.getAllowMissingColumnNames(),
                format.getIgnoreHeaderCase()
        );

        assertEquals("NULL", customFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_NullStringEmpty() throws Exception {
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
                boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = CSVFormat.DEFAULT;

        CSVFormat customFormat = constructor.newInstance(
                format.getDelimiter(),
                format.getQuoteCharacter(),
                format.getQuoteMode(),
                format.getCommentMarker(),
                format.getEscapeCharacter(),
                format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(),
                format.getRecordSeparator(),
                "",
                null,
                format.getHeader(),
                format.getSkipHeaderRecord(),
                format.getAllowMissingColumnNames(),
                format.getIgnoreHeaderCase()
        );

        assertEquals("", customFormat.getNullString());
    }

}