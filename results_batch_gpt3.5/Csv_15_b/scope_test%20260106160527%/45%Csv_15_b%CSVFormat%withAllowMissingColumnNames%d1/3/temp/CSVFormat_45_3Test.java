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

class CSVFormat_45_3Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_defaultTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set allowMissingColumnNames to false on DEFAULT instance temporarily
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);

        // Remove final modifier (works only on Java <= 11, for newer Java versions this is not possible)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Backup original value
        boolean originalValue = (boolean) field.get(format);

        try {
            // Set to false to match test expectation that default is false
            field.set(format, false);

            CSVFormat result = format.withAllowMissingColumnNames();
            assertNotNull(result);
            // The returned instance should have allowMissingColumnNames set to true
            assertTrue(result.getAllowMissingColumnNames());
            // The original instance should remain unchanged
            assertFalse(format.getAllowMissingColumnNames());
        } finally {
            // Restore original value
            field.set(format, originalValue);
        }
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_explicitFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set allowMissingColumnNames to false on DEFAULT instance temporarily
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Backup original value
        boolean originalValue = (boolean) field.get(format);

        try {
            // Set to false to match test expectation that default is false
            field.set(format, false);

            CSVFormat result = format.withAllowMissingColumnNames(false);
            assertNotNull(result);
            assertFalse(result.getAllowMissingColumnNames());
            assertFalse(format.getAllowMissingColumnNames());
        } finally {
            // Restore original value
            field.set(format, originalValue);
        }
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_explicitTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        CSVFormat result = format.withAllowMissingColumnNames(true);
        assertNotNull(result);
        assertTrue(result.getAllowMissingColumnNames());
        assertFalse(format.getAllowMissingColumnNames());
    }
}