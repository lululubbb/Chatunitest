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

public class CSVFormat_12_5Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set the new value
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header set to null via withHeader (which sets header)
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        // Using reflection to forcibly set private final field header to null (redundant but for test)
        setFinalField(format, "header", null);

        String[] header = format.getHeader();
        assertNull(header, "Header should be null when header field is null");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNotNull() throws Exception {
        String[] originalHeader = new String[] {"col1", "col2", "col3"};

        // Create a new CSVFormat instance with header set to originalHeader via withHeader
        CSVFormat format = CSVFormat.DEFAULT.withHeader(originalHeader);

        // Using reflection to forcibly set private final field header to originalHeader (redundant but for test)
        setFinalField(format, "header", originalHeader);

        String[] header = format.getHeader();
        assertNotNull(header, "Header should not be null when header field is set");
        assertArrayEquals(originalHeader, header, "Returned header should match the original header");

        // Verify that the returned array is a clone (modifying returned array does not affect original)
        header[0] = "modified";
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] headerAfterModification = (String[]) headerField.get(format);
        assertArrayEquals(originalHeader, headerAfterModification, "Original header should not be affected by modifications to returned array");
    }
}