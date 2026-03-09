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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_18_4Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        String nullString = format.getNullString();
        assertNull(nullString);
    }

    @Test
    @Timeout(8000)
    void testGetNullString_CustomNullString() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        assertEquals("NULL", format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_ReflectionAccess() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("empty");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String fieldValue = (String) nullStringField.get(format);
        assertEquals("empty", fieldValue);
        // Also verify getNullString returns same value
        assertEquals(fieldValue, format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_NullStringSetAndUnset() {
        CSVFormat formatWithNull = CSVFormat.DEFAULT.withNullString("NA");
        assertEquals("NA", formatWithNull.getNullString());

        CSVFormat formatWithoutNull = CSVFormat.DEFAULT.withNullString(null);
        assertNull(formatWithoutNull.getNullString());
    }
}