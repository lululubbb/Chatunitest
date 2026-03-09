package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

public class CSVFormat_28_1Test {

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_NullStringIsNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString("dummy"); // create a modifiable instance
        // Using reflection to set nullString to null
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier (works in JDK 8, may not work in later versions)
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // In Java 12+, the 'modifiers' field doesn't exist; ignore
        }

        nullStringField.set(csvFormat, null);

        boolean result = csvFormat.isNullStringSet();
        assertFalse(result, "Expected isNullStringSet() to return false when nullString is null");
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_NullStringIsNotNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString(null);
        // Using reflection to set nullString to a non-null value
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier (works in JDK 8, may not work in later versions)
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // In Java 12+, the 'modifiers' field doesn't exist; ignore
        }

        nullStringField.set(csvFormat, "NULL");

        boolean result = csvFormat.isNullStringSet();
        assertTrue(result, "Expected isNullStringSet() to return true when nullString is not null");
    }
}