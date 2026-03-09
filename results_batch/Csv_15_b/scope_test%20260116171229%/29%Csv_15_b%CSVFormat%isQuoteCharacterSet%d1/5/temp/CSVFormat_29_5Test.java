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

public class CSVFormat_29_5Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() throws Exception {
        // Create a new CSVFormat instance with quoteCharacter set to null
        CSVFormat format = CSVFormat.DEFAULT.withQuote(null);

        // Use reflection to set private final field quoteCharacter to non-null Character
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharacterField, quoteCharacterField.getModifiers() & ~Modifier.FINAL);

        quoteCharacterField.set(format, Character.valueOf('"'));

        boolean result = format.isQuoteCharacterSet();

        assertTrue(result, "Expected isQuoteCharacterSet() to return true when quoteCharacter is set");
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        // Create a new CSVFormat instance with quoteCharacter set to non-null Character
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');

        // Use reflection to set private final field quoteCharacter to null
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);

        // Remove final modifier via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharacterField, quoteCharacterField.getModifiers() & ~Modifier.FINAL);

        quoteCharacterField.set(format, null);

        boolean result = format.isQuoteCharacterSet();

        assertFalse(result, "Expected isQuoteCharacterSet() to return false when quoteCharacter is null");
    }
}