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

public class CSVFormat_26_6Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // Use reflection to set private final field nullString to null
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        nullStringField.set(format, null);

        boolean result = format.isNullStringSet();
        assertFalse(result, "Expected isNullStringSet() to return false when nullString is null");
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNonNull() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        boolean result = format.isNullStringSet();
        assertTrue(result, "Expected isNullStringSet() to return true when nullString is non-null");
    }
}