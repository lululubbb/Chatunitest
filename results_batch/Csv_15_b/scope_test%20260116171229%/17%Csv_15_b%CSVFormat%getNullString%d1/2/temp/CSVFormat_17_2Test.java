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

class CSVFormat_17_2Test {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultNull() {
        // DEFAULT has nullString == null
        assertNull(csvFormatDefault.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullString() {
        CSVFormat customFormat = CSVFormat.DEFAULT.withNullString("NULL_VALUE");
        assertEquals("NULL_VALUE", customFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithEmptyString() {
        CSVFormat customFormat = CSVFormat.DEFAULT.withNullString("");
        assertEquals("", customFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithWhitespaceString() {
        CSVFormat customFormat = CSVFormat.DEFAULT.withNullString("   ");
        assertEquals("   ", customFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_ReflectionAccess() throws Exception {
        // Access private final field nullString via reflection on DEFAULT instance
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Set the nullString field on the DEFAULT instance using reflection
        // Since CSVFormat is immutable and final fields cannot be changed easily,
        // use reflection to bypass final and set the field.

        // Remove final modifier if running on Java 8 or below
        // In Java 9+, this is not possible easily. This code assumes Java 8 or below.
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Java 12+ does not have 'modifiers' field, ignore this step
        }

        nullStringField.set(csvFormatDefault, "REFLECTED_NULL");

        String nullStringValue = csvFormatDefault.getNullString();

        assertEquals("REFLECTED_NULL", nullStringValue);
    }
}