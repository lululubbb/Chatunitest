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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_13_3Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        // headerComments is null in DEFAULT, verify returns null
        String[] result = format.getHeaderComments();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        // Create a new CSVFormat instance with headerComments set via constructor

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] headerComments = new String[]{"comment1", "comment2"};

        CSVFormat format = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                (Object[]) headerComments, // cast to Object[] to match parameter type
                null, // header
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase()
        );

        String[] result = format.getHeaderComments();

        assertNotNull(result);
        assertArrayEquals(headerComments, result);
        assertNotSame(headerComments, result);

        // Modify returned array and verify original not affected
        result[0] = "modified";
        String[] original = format.getHeaderComments();
        assertEquals("comment1", original[0]);
    }
}