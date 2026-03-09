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

class CSVFormat_19_3Test {

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('\"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_nullQuote() throws Exception {
        // Use reflection to create a CSVFormat instance with quoteCharacter set to null
        CSVFormat format = createCSVFormatWithQuoteCharacter(null);
        Character quoteChar = format.getQuoteCharacter();
        assertNull(quoteChar);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_customQuote() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\'');
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('\'', quoteChar.charValue());
    }

    // Helper method to create CSVFormat instance with specific quoteCharacter using reflection
    private CSVFormat createCSVFormatWithQuoteCharacter(Character quoteChar) throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        return ctor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                quoteChar,
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                null, // headerComments as Object[]
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase()
        );
    }

}