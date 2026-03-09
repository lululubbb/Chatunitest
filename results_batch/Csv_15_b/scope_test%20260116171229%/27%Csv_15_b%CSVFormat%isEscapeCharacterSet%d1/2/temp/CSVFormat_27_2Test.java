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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_27_2Test {

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        // Use reflection to set the private final escapeCharacter field to null forcibly
        Field escapeField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeField.setAccessible(true);

        // Remove final modifier using reflection (works in Java 8+)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeField, escapeField.getModifiers() & ~Modifier.FINAL);

        escapeField.set(format, null);

        assertFalse(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_whenEscapeCharacterIsSet() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscapeCharacterSet());
    }
}