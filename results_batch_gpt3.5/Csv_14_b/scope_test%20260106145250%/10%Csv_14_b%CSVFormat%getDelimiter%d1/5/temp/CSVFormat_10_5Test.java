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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_10_5Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormatDefault = CSVFormat.DEFAULT;

        // Create a CSVFormat instance with a custom delimiter using the provided withDelimiter method
        csvFormatCustom = CSVFormat.DEFAULT.withDelimiter(';');
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Default() {
        // Default delimiter is COMMA (',')
        assertEquals(',', csvFormatDefault.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_CustomDelimiter() {
        // Custom delimiter set to ';'
        assertEquals(';', csvFormatCustom.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_ReflectionSetDelimiter() throws Exception {
        // Use reflection to create a CSVFormat instance with private final delimiter field set to a specific char

        // Create a new CSVFormat instance with original delimiter
        CSVFormat csvFormatCopy = CSVFormat.DEFAULT.withDelimiter(CSVFormat.DEFAULT.getDelimiter());

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);

        // Remove final modifier from the delimiter field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(delimiterField, delimiterField.getModifiers() & ~Modifier.FINAL);

        // Set delimiter field to '|'
        delimiterField.set(csvFormatCopy, '|');

        // Validate that getDelimiter returns the changed delimiter '|'
        assertEquals('|', csvFormatCopy.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Constants() {
        // Test delimiters from predefined CSVFormat constants
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals(',', CSVFormat.EXCEL.getDelimiter());
        assertEquals('|', CSVFormat.INFORMIX_UNLOAD.getDelimiter());
        assertEquals(',', CSVFormat.INFORMIX_UNLOAD_CSV.getDelimiter());
        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
        assertEquals(',', CSVFormat.RFC4180.getDelimiter());
        assertEquals('\t', CSVFormat.TDF.getDelimiter());
    }
}