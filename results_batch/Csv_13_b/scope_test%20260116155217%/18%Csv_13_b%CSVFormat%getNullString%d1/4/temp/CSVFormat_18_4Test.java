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

public class CSVFormat_18_4Test {

    @Test
    @Timeout(8000)
    public void testGetNullString_DefaultIsNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_WithNullStringSet() {
        String nullStr = "NULL";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr);
        assertEquals(nullStr, format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_WithNullStringSetToEmpty() {
        String nullStr = "";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr);
        assertEquals(nullStr, format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_WithNullStringSetToWhitespace() {
        String nullStr = "   ";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr);
        assertEquals(nullStr, format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_ReflectionSetNullString() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier from the nullString field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        // Using reflection to set nullString on the existing instance (for testing only)
        nullStringField.set(format, "REFLECTED_NULL");

        assertEquals("REFLECTED_NULL", format.getNullString());
    }
}