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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_24_4Test {

    @Test
    @Timeout(8000)
    void testGetAutoFlush_DefaultInstance() {
        // Default instance has autoFlush = false
        assertFalse(CSVFormat.DEFAULT.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_WithAutoFlushTrue() throws Exception {
        // Create CSVFormat instance with autoFlush = true via reflection on private field
        CSVFormat format = CSVFormat.DEFAULT;

        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);

        // The field is final, so we need to remove final modifier to set it via reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(autoFlushField, autoFlushField.getModifiers() & ~Modifier.FINAL);

        autoFlushField.setBoolean(format, true);

        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_WithAutoFlushFalse() throws Exception {
        // Create CSVFormat instance with autoFlush = false via reflection on private field
        CSVFormat format = CSVFormat.DEFAULT;

        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(autoFlushField, autoFlushField.getModifiers() & ~Modifier.FINAL);

        autoFlushField.setBoolean(format, false);

        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_NewInstanceWithAutoFlushTrue() throws Exception {
        // Use the constructor via reflection to create an instance with autoFlush = true
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',', // delimiter
                '"', // quoteChar
                null, // quoteMode
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // nullString
                null, // headerComments
                null, // header
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // ignoreHeaderCase
                false, // trim
                false, // trailingDelimiter
                true // autoFlush
        );

        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_NewInstanceWithAutoFlushFalse() throws Exception {
        // Use the constructor via reflection to create an instance with autoFlush = false
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',', // delimiter
                '"', // quoteChar
                null, // quoteMode
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // nullString
                null, // headerComments
                null, // header
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // ignoreHeaderCase
                false, // trim
                false, // trailingDelimiter
                false // autoFlush
        );

        assertFalse(format.getAutoFlush());
    }
}