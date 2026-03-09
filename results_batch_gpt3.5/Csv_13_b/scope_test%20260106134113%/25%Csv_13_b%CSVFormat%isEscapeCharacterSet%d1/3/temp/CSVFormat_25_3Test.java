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
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

class CSVFormat_25_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() throws Exception {
        // Create an instance of CSVFormat using reflection since constructor is private
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        // Initialize with escapeCharacter = null first
        csvFormat = constructor.newInstance(
                ',', // delimiter
                '"', // quoteCharacter
                null, // quoteMode
                null, // commentMarker
                null, // escapeCharacter
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // headerComments (Object[])
                null, // header (String[])
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false // ignoreHeaderCase
        );
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_WhenEscapeCharacterIsNull() throws Exception {
        // escapeCharacter is null by default in setUp
        assertFalse(csvFormat.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsEscapeCharacterSet_WhenEscapeCharacterIsSet() throws Exception {
        // Set private final field escapeCharacter to a non-null Character using reflection
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);

        // Remove final modifier using reflection (works in Java 8 and above)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(escapeCharacterField, escapeCharacterField.getModifiers() & ~Modifier.FINAL);

        escapeCharacterField.set(csvFormat, '\\');

        assertTrue(csvFormat.isEscapeCharacterSet());
    }
}