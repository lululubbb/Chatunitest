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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_12_6Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Get the private constructor with Object[] headerComments and String[] header parameters
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Create a new CSVFormat instance with header == null by passing null for header and headerComments
        CSVFormat formatWithNullHeader = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                null,  // headerComments
                null,  // header
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase()
        );

        String[] header = formatWithNullHeader.getHeader();
        assertNull(header, "Header should be null when header field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNonNull() throws Exception {
        String[] originalHeader = new String[]{"col1", "col2"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(originalHeader);

        String[] header = format.getHeader();
        assertNotNull(header, "Header should not be null when header field is set");
        assertArrayEquals(originalHeader, header, "Returned header should match the original header");
        assertNotSame(header, originalHeader, "Returned header should be a clone, not the same instance");

        // Modifying returned header should not affect original header
        header[0] = "modified";
        String[] headerAfterModification = format.getHeader();
        assertEquals("col1", headerAfterModification[0], "Original header should not be affected by modifications to returned array");
    }
}