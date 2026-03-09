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

public class CSVFormat_25_6Test {

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        // Create a new CSVFormat instance based on DEFAULT
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Use reflection to set the private final escapeCharacter field to null
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);

        // Remove final modifier using reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeCharacterField, escapeCharacterField.getModifiers() & ~Modifier.FINAL);

        escapeCharacterField.set(csvFormat, null);

        assertFalse(csvFormat.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() {
        // Create a new CSVFormat instance with escapeCharacter set to '\\'
        CSVFormat csvFormat = CSVFormat.DEFAULT.withEscape('\\');

        assertTrue(csvFormat.isEscapeCharacterSet());
    }
}