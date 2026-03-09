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

public class CSVFormat_27_2Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() {
        // Create a new CSVFormat instance with quoteCharacter set to '"'
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\"');
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() {
        // Create a new CSVFormat instance with quoteCharacter set to '"' initially
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\"');
        try {
            Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
            quoteCharacterField.setAccessible(true);

            // The field is final, so we need to remove final modifier via reflection
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(quoteCharacterField, quoteCharacterField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

            // Set the quoteCharacter field to null
            quoteCharacterField.set(format, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        assertFalse(format.isQuoteCharacterSet());
    }
}