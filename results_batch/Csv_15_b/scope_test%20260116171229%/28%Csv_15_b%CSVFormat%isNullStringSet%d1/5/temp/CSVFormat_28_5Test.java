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

public class CSVFormat_28_5Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSetWhenNullStringIsNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString("dummy");

        // Use reflection to set private final field nullString to null
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier using reflection (works for Java 8)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        nullStringField.set(csvFormat, null);

        // Invoke isNullStringSet and assert false
        assertFalse(csvFormat.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSetWhenNullStringIsNotNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withNullString(null);

        // Use reflection to set private final field nullString to a non-null value
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier using reflection (works for Java 8)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        nullStringField.set(csvFormat, "NULL");

        // Invoke isNullStringSet and assert true
        assertTrue(csvFormat.isNullStringSet());
    }
}