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

public class CSVFormat_27_4Test {

    private void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsSet() throws Exception {
        // Backup original DEFAULT instance
        Field defaultField = CSVFormat.class.getDeclaredField("DEFAULT");
        defaultField.setAccessible(true);
        CSVFormat originalDefault = (CSVFormat) defaultField.get(null);

        try {
            // Create a new CSVFormat by modifying the existing DEFAULT instance using withQuote()
            CSVFormat formatWithQuote = originalDefault.withQuote('\"');

            setFinalStatic(defaultField, formatWithQuote);

            CSVFormat format = CSVFormat.DEFAULT;

            assertTrue(format.isQuoteCharacterSet());
        } finally {
            // Restore original DEFAULT instance
            setFinalStatic(defaultField, originalDefault);
        }
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsNull() throws Exception {
        // Backup original DEFAULT instance
        Field defaultField = CSVFormat.class.getDeclaredField("DEFAULT");
        defaultField.setAccessible(true);
        CSVFormat originalDefault = (CSVFormat) defaultField.get(null);

        try {
            // Create a new CSVFormat by modifying the existing DEFAULT instance using withQuote(null)
            CSVFormat formatWithNullQuote = originalDefault.withQuote((Character) null);

            setFinalStatic(defaultField, formatWithNullQuote);

            CSVFormat format = CSVFormat.DEFAULT;

            assertFalse(format.isQuoteCharacterSet());
        } finally {
            // Restore original DEFAULT instance
            setFinalStatic(defaultField, originalDefault);
        }
    }
}