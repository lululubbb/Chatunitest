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

public class CSVFormat_29_1Test {

    private CSVFormat createCSVFormatWithQuoteCharacter(Character quoteCharacter) {
        try {
            // Create a new CSVFormat instance based on DEFAULT
            CSVFormat format = CSVFormat.DEFAULT;

            // Use reflection to set the private final quoteCharacter field
            Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
            quoteCharField.setAccessible(true);

            // Because quoteCharacter is final, use reflection to remove final modifier if needed
            // This is a workaround for testing purposes
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

            quoteCharField.set(format, quoteCharacter);

            return format;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() {
        CSVFormat format = createCSVFormatWithQuoteCharacter(null);

        boolean result = format.isQuoteCharacterSet();
        assertFalse(result, "Expected isQuoteCharacterSet() to return false when quoteCharacter is null");
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet_whenQuoteCharacterIsNotNull() {
        CSVFormat format = createCSVFormatWithQuoteCharacter('"');

        boolean result = format.isQuoteCharacterSet();
        assertTrue(result, "Expected isQuoteCharacterSet() to return true when quoteCharacter is not null");
    }
}