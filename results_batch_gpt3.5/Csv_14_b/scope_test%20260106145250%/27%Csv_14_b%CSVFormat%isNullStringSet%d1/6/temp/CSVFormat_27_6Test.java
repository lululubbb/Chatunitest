package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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

class CSVFormat_27_6Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSet_NullNullString() throws Exception {
        // Create CSVFormat instance with non-null nullString initially
        CSVFormat format = CSVFormat.DEFAULT.withNullString("dummy");

        // Access private final field nullString via reflection
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        // Set nullString field to null
        nullStringField.set(format, null);

        // Invoke isNullStringSet()
        boolean result = format.isNullStringSet();

        assertFalse(result, "Expected isNullStringSet() to return false when nullString is null");
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_NonNullNullString() {
        // Create CSVFormat instance with non-null nullString using withNullString
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // Invoke isNullStringSet()
        boolean result = format.isNullStringSet();

        assertTrue(result, "Expected isNullStringSet() to return true when nullString is not null");
    }
}