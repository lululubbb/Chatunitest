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
import java.lang.reflect.Modifier;

public class CSVFormat_18_5Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        String nullString = format.getNullString();
        assertNull(nullString);
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullString() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        String nullString = format.getNullString();
        assertEquals("NULL", nullString);
    }

    @Test
    @Timeout(8000)
    void testGetNullString_ReflectionSetNullString() throws Exception {
        // Create a new CSVFormat instance with nullString initially null
        CSVFormat format = CSVFormat.DEFAULT;

        // Access the private final field "nullString" in CSVFormat
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier from the field (for Java 12+ this may not work as expected)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        // Set the nullString field to "REF_NULL"
        nullStringField.set(format, "REF_NULL");

        String result = format.getNullString();
        assertEquals("REF_NULL", result);
    }

}