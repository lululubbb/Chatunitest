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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_10_1Test {

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsClone() throws Exception {
        // Create header array
        String[] header = new String[]{"col1", "col2", "col3"};

        // Create CSVFormat instance with header using withHeader method
        CSVFormat format = CSVFormat.DEFAULT.withHeader(header);

        // Call getHeader
        String[] result = format.getHeader();

        // Verify returned array equals original header content
        assertArrayEquals(header, result);

        // Verify returned array is a clone, not the same reference
        assertNotSame(header, result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsNullWhenNoHeader() throws Exception {
        // Create CSVFormat instance with header to non-null first
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b");

        // Use reflection to set private final header field to null
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        // Remove final modifier using reflection hack
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerField, headerField.getModifiers() & ~Modifier.FINAL);

        // Set header to null
        headerField.set(format, null);

        // Call getHeader
        String[] result = format.getHeader();

        // Verify result is null
        assertNull(result);
    }
}