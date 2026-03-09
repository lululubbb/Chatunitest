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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_12_1Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header not null
        CSVFormat format = CSVFormat.DEFAULT.withHeader("col1");

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);

        // Remove final modifier from the header field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(headerField, headerField.getModifiers() & ~Modifier.FINAL);

        // Set header to null
        headerField.set(format, null);

        String[] result = format.getHeader();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNotNull() throws Exception {
        String[] headerValue = new String[]{"col1", "col2"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headerValue);

        String[] result = format.getHeader();
        assertNotNull(result);
        assertArrayEquals(headerValue, result);
        assertNotSame(headerValue, result); // ensure clone returned, not original reference
    }
}