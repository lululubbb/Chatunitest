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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_10_6Test {

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
        if (header == null) {
            // Create a CSVFormat.DEFAULT instance and set the private final header field to null via reflection
            CSVFormat csvFormat = CSVFormat.DEFAULT;
            Field headerField = CSVFormat.class.getDeclaredField("header");
            headerField.setAccessible(true);

            // Remove final modifier from the header field
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(headerField, headerField.getModifiers() & ~Modifier.FINAL);

            headerField.set(csvFormat, null);
            return csvFormat;
        } else {
            return CSVFormat.DEFAULT.withHeader(header);
        }
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        CSVFormat csvFormat = createCSVFormatWithHeader(null);

        // Invoke getHeader()
        String[] result = csvFormat.getHeader();

        assertNull(result, "Expected getHeader() to return null when header field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsCloneOfHeader() throws Exception {
        // Prepare header array
        String[] header = new String[] { "col1", "col2", "col3" };

        // Create CSVFormat instance with header set via withHeader method
        CSVFormat csvFormat = createCSVFormatWithHeader(header);

        // Invoke getHeader()
        String[] result = csvFormat.getHeader();

        // Verify result is not null
        assertNotNull(result, "Expected getHeader() to not return null when header field is set");

        // Verify result equals header content
        assertArrayEquals(header, result, "Expected getHeader() to return clone of header array");

        // Verify result is a new array (clone)
        assertNotSame(header, result, "Expected getHeader() to return a clone, not the same array instance");
    }
}