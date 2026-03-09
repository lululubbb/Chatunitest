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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_22_6Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultFalse() throws Exception {
        // Create a new CSVFormat instance with skipHeaderRecord = false using reflection and constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class,
                boolean.class, boolean.class,
                String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        CSVFormat format = constructor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteCharacter(),
                defaultFormat.getQuoteMode(),
                defaultFormat.getCommentMarker(),
                defaultFormat.getEscapeCharacter(),
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                defaultFormat.getNullString(),
                null,
                defaultFormat.getHeader(),
                false, // skipHeaderRecord set to false explicitly
                defaultFormat.getAllowMissingColumnNames(),
                defaultFormat.getIgnoreHeaderCase()
        );

        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord());
    }
}