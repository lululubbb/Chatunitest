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

class CSVFormat_11_6Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacterWhenSet() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar);
        assertEquals(Character.valueOf('\\'), escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacterWhenNull() throws Exception {
        // Create a CSVFormat instance with escapeCharacter set to a value first
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');

        // Use reflection to get the private final field escapeCharacter
        Field escapeField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeField.setAccessible(true);

        // Remove final modifier using reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        // Set the field to null on the instance
        escapeField.set(format, null);

        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacterDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar);
    }
}