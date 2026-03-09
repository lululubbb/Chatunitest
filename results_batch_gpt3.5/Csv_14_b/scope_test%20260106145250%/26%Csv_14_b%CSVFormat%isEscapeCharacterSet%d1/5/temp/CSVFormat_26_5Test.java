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

class CSVFormat_26_5Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertFalse(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetWhenEscapeCharacterIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(Character.valueOf('\\'));
        assertTrue(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSetUsingReflection() throws Exception {
        // Create a new CSVFormat instance based on DEFAULT but with escapeCharacter null, to avoid modifying a shared constant
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);

        // Remove final modifier from escapeCharacter field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeCharacterField, escapeCharacterField.getModifiers() & ~Modifier.FINAL);

        // Set escapeCharacter to null
        escapeCharacterField.set(format, null);
        assertFalse(format.isEscapeCharacterSet());

        // Set escapeCharacter to '\\'
        escapeCharacterField.set(format, Character.valueOf('\\'));
        assertTrue(format.isEscapeCharacterSet());
    }
}