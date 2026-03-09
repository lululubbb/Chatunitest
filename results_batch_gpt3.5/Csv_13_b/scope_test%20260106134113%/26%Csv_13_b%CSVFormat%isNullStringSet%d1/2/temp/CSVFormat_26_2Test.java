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

public class CSVFormat_26_2Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set the field value on the target instance
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_nullStringIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("dummy"); // create a modifiable instance

        setFinalField(format, "nullString", null);

        boolean result = format.isNullStringSet();

        assertFalse(result, "Expected isNullStringSet() to return false when nullString is null");
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_nullStringIsNotNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null); // create a modifiable instance

        setFinalField(format, "nullString", "NULL");

        boolean result = format.isNullStringSet();

        assertTrue(result, "Expected isNullStringSet() to return true when nullString is not null");
    }
}