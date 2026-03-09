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

import java.lang.reflect.Constructor;

class CSVFormat_10_2Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        CSVFormat format = createCSVFormatWithHeader(null);
        String[] header = format.getHeader();
        assertNull(header, "Header should be null when internal header is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsClone() throws Exception {
        String[] internalHeader = new String[]{"A", "B", "C"};
        CSVFormat format = createCSVFormatWithHeader(internalHeader);

        String[] returnedHeader = format.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(internalHeader, returnedHeader, "Returned header should equal internal header");
        assertNotSame(internalHeader, returnedHeader, "Returned header should be a clone, not the same instance");

        // Modify returnedHeader and check internalHeader is not affected
        returnedHeader[0] = "X";
        String[] headerAfterModification = format.getHeader();
        assertEquals("A", headerAfterModification[0], "Internal header should remain unchanged after modifying returned clone");
    }

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);

        constructor.setAccessible(true);

        Object[] params = new Object[]{
                format.getDelimiter(),
                format.getQuoteCharacter(),
                format.getQuoteMode(),
                format.getCommentMarker(),
                format.getEscapeCharacter(),
                format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(),
                format.getRecordSeparator(),
                format.getNullString(),
                header,
                format.getSkipHeaderRecord(),
                format.getAllowMissingColumnNames()
        };

        return constructor.newInstance(params);
    }
}