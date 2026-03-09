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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_17_5Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatWithNullString;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
        csvFormatWithNullString = CSVFormat.DEFAULT.withNullString("NULL");
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_DefaultInstance() {
        // The DEFAULT instance has nullString == null
        String nullString = csvFormatDefault.getNullString();
        assertNull(nullString, "Expected nullString to be null for DEFAULT CSVFormat");
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_WithNullStringSet() {
        // The csvFormatWithNullString has nullString set to "NULL"
        String nullString = csvFormatWithNullString.getNullString();
        assertEquals("NULL", nullString, "Expected nullString to be 'NULL'");
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_ReflectiveAccess() throws Exception {
        // Use reflection to set private final field nullString to a test value
        CSVFormat format = CSVFormat.DEFAULT;

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Remove final modifier from the field (works in Java 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        // Save original value to restore later
        String originalValue = (String) nullStringField.get(format);

        nullStringField.set(format, "REFLECTIVE_NULL");

        String result = format.getNullString();
        assertEquals("REFLECTIVE_NULL", result, "Expected nullString to be 'REFLECTIVE_NULL' after reflection set");

        // Reset field to original value to avoid side effects
        nullStringField.set(format, originalValue);
    }
}