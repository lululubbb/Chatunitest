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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Constructor;

public class CSVFormat_10_3Test {

    private CSVFormat csvFormatWithHeader;
    private CSVFormat csvFormatWithoutHeader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create CSVFormat instance with header using withHeader method
        csvFormatWithHeader = CSVFormat.DEFAULT.withHeader("col1", "col2", "col3");

        // Create CSVFormat instance without header (header field = null) via constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        csvFormatWithoutHeader = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                (Object) null,
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneNotSameReference() throws Exception {
        String[] header1 = csvFormatWithHeader.getHeader();
        String[] header2 = csvFormatWithHeader.getHeader();

        assertNotNull(header1, "Header should not be null");
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, header1, "Header content mismatch");
        assertNotSame(header1, header2, "getHeader() should return a clone, not the same array instance");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsNullWhenHeaderIsNull() {
        String[] header = csvFormatWithoutHeader.getHeader();
        assertNull(header, "Header should be null when header field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWithEmptyHeaderArray() throws Exception {
        // Create CSVFormat instance with empty header array via reflection
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormatEmptyHeader = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                (Object) new String[0],
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames());

        String[] header = csvFormatEmptyHeader.getHeader();
        assertNotNull(header, "Header should not be null when empty array is set");
        assertEquals(0, header.length, "Header array length should be zero");
    }
}