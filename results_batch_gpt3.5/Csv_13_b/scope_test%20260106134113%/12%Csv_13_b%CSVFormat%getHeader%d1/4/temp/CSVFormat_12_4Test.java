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

public class CSVFormat_12_4Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set field on the target instance (not null)
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsCloneWhenHeaderIsNotNull() throws Exception {
        String[] originalHeader = new String[]{"col1", "col2"};

        // Create a new CSVFormat instance with the header set using the withHeader method
        CSVFormat formatWithHeader = CSVFormat.DEFAULT.withHeader(originalHeader);

        // Use reflection to replace the header field of the formatWithHeader instance instead of DEFAULT
        setFinalField(formatWithHeader, "header", formatWithHeader.getHeader());

        String[] returnedHeader = formatWithHeader.getHeader();

        assertNotNull(returnedHeader);
        assertArrayEquals(originalHeader, returnedHeader);
        assertNotSame(originalHeader, returnedHeader, "Returned header should be a clone, not the same instance");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with no header
        CSVFormat formatWithoutHeader = CSVFormat.DEFAULT.withHeader((String[]) null);

        // Use reflection to set header field to null on the new instance
        setFinalField(formatWithoutHeader, "header", null);

        String[] returnedHeader = formatWithoutHeader.getHeader();

        assertNull(returnedHeader, "Returned header should be null when header field is null");
    }
}