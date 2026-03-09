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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class CSVFormat_12_2Test {

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsCloneWhenHeaderIsNotNull() throws Exception {
        // Create a new CSVFormat instance without header
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader((String[]) null);

        // Use reflection to set the private final 'header' field to originalHeader
        String[] originalHeader = new String[]{"col1", "col2"};
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        // Remove final modifier using reflection on Field modifiers (Java 12+ requires different approach, 
        // but for simplicity assuming Java 8-11 here)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerField, headerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        headerField.set(csvFormat, originalHeader);

        String[] returnedHeader = csvFormat.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(originalHeader, returnedHeader, "Returned header should equal original header");
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same instance");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsNullWhenHeaderIsNull() {
        // Create a new CSVFormat instance with no header
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] returnedHeader = csvFormat.getHeader();

        assertNull(returnedHeader, "Returned header should be null when header field is null");
    }
}