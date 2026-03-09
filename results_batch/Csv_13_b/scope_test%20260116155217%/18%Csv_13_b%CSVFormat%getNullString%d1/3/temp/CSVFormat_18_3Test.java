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

public class CSVFormat_18_3Test {

    @Test
    @Timeout(8000)
    public void testGetNullString_DefaultNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_NonNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        // Use reflection to verify the private final field is set correctly
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullStringValue = (String) nullStringField.get(format);
        assertEquals("NULL", nullStringValue);
        assertEquals("NULL", format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_EmptyString() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullStringValue = (String) nullStringField.get(format);
        assertEquals("", nullStringValue);
        assertEquals("", format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_NullExplicit() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullStringValue = (String) nullStringField.get(format);
        assertNull(nullStringValue);
        assertNull(format.getNullString());
    }
}