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

class CSVFormat_18_6Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_CustomNull() throws Exception {
        String customNull = "NULL_VALUE";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(customNull);
        // Use reflection to access private final field nullString
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String value = (String) nullStringField.get(format);
        assertEquals(customNull, value);
        assertEquals(customNull, format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_EmptyString() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String value = (String) nullStringField.get(format);
        assertEquals("", value);
        assertEquals("", format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_NullSetExplicitly() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String value = (String) nullStringField.get(format);
        assertNull(value);
        assertNull(format.getNullString());
    }
}