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

class CSVFormat_27_1Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        // Using DEFAULT which has escapeCharacter == null
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set escapeCharacter to null explicitly to be sure
        Field escapeField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeField.setAccessible(true);

        // Because escapeCharacter is final, remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        escapeField.set(format, null);

        assertFalse(format.isEscapeCharacterSet(), "Escape character should not be set");
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() {
        // Create a CSVFormat with escape character set
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscapeCharacterSet(), "Escape character should be set");
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsSetToNullExplicitly() {
        // Create a CSVFormat explicitly setting escape character to null
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertFalse(format.isEscapeCharacterSet(), "Escape character should not be set when explicitly set to null");
    }
}