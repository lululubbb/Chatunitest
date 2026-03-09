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

public class CSVFormat_12_1Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header set to null using withHeader
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] result = format.getHeader();
        assertNull(result, "Expected getHeader() to return null when header is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNonNull() throws Exception {
        String[] headerValue = new String[]{"col1", "col2", "col3"};
        // Create a new CSVFormat instance with the header set to headerValue using withHeader
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headerValue);

        String[] result = format.getHeader();
        assertNotNull(result, "Expected getHeader() to return a non-null array when header is set");
        assertArrayEquals(headerValue, result, "Expected returned header array to equal the set header");
        assertNotSame(headerValue, result, "Expected returned header to be a clone, not the original array");

        // Modify returned array and verify original is not changed (defensive copy)
        result[0] = "modified";

        // Use reflection to get the private header field from the format instance
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] originalAfterModification = (String[]) headerField.get(format);

        assertEquals("col1", originalAfterModification[0], "Original header array should not be affected by modifications to the returned array");
    }
}